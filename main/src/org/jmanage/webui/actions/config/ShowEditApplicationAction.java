package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.webui.forms.WeblogicApplicationForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.WeblogicApplicationConfig;
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
public class ShowEditApplicationAction extends BaseAction {

    /**
     * Gets the application for given application id and sets it on request.
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

        ApplicationConfig config = context.getApplicationConfig();
        WeblogicApplicationForm appForm = (WeblogicApplicationForm)actionForm;
        /* populate the form */
        appForm.setApplicationId(config.getApplicationId());
        appForm.setName(config.getName());
        appForm.setHost(config.getHost());
        appForm.setPort(String.valueOf(config.getPort()));
        appForm.setUsername(config.getUsername());
        appForm.setPassword(config.getPassword());
        appForm.setServerName((String)config.getParamValues().
                get(WeblogicApplicationConfig.SERVER_NAME));

        return mapping.findForward(Forwards.SUCCESS);
    }

}
