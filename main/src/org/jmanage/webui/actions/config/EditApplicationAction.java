package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.WeblogicApplicationForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

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

        WeblogicApplicationForm appForm = (WeblogicApplicationForm)actionForm;
        ApplicationConfig config =
                ApplicationConfigManager.getApplicationConfig(
                        appForm.getApplicationId());
        assert config != null;

        config.setName(appForm.getName());
        config.setHost(appForm.getHost());
        config.setPort(new Integer(appForm.getPort()));
        config.setUsername(appForm.getUsername());
        final String password = appForm.getPassword();
        if(!password.equals(config.getPassword())){
            config.setPassword(password);
        }

        ApplicationConfigManager.updateApplication(config);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
