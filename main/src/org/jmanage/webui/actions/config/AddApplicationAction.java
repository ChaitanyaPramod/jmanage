package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra
 */
public class AddApplicationAction extends BaseAction {

    /**
     * Add a new application
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        ApplicationForm appForm = (ApplicationForm)actionForm;
        //todo: Need something better
        String appId = String.valueOf(System.currentTimeMillis());
        Integer port = appForm.getPort() != null && !"".equals(appForm.getPort()) ?
                new Integer(appForm.getPort()) :
                null;
        ApplicationConfig config =
                ApplicationConfigFactory.create(appId, appForm.getName(),
                        appForm.getType(),
                        appForm.getHost(),
                        port,
                        null,
                        appForm.getUsername(),
                        appForm.getPassword(),
                        null);

        ApplicationConfigManager.addApplication(config);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
