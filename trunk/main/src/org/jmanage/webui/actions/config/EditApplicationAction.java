package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.util.UserActivityLogger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra
 */
public class EditApplicationAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        ApplicationForm appForm = (ApplicationForm)actionForm;
        ApplicationConfig config =
                ApplicationConfigManager.getApplicationConfig(
                        appForm.getApplicationId());
        assert config != null;

        config.setName(appForm.getName());
        config.setHost(appForm.getHost());
        if(appForm.getPort() != null)
            config.setPort(new Integer(appForm.getPort()));
            config.setUsername(appForm.getUsername());
        final String password = appForm.getPassword();
        if(password != null && !password.equals(config.getPassword())){
            config.setPassword(password);
        }

        ApplicationConfigManager.updateApplication(config);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Updated application "+"\""+config.getName()+"\"");
        return mapping.findForward(Forwards.SUCCESS);
    }
}
