package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.MetaApplicationConfig;
import org.jmanage.core.modules.ModuleConfig;
import org.jmanage.core.modules.ModuleRegistry;
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
        ApplicationForm appForm = (ApplicationForm)actionForm;
        ModuleConfig moduleConfig = ModuleRegistry.getModule(config.getType());
        MetaApplicationConfig metaAppConfig =
                moduleConfig.getMetaApplicationConfig();

        /* populate the form */
        appForm.setApplicationId(config.getApplicationId());
        appForm.setName(config.getName());
        appForm.setType(config.getType());
        if(metaAppConfig.isDisplayHost())
            appForm.setHost(config.getHost());
        if(metaAppConfig.isDisplayPort())
            appForm.setPort(String.valueOf(config.getPort()));
        if(metaAppConfig.isDisplayUsername())
            appForm.setUsername(config.getUsername());
        if(metaAppConfig.isDisplayPassword() && config.getPassword() != null)
            appForm.setPassword(ApplicationForm.FORM_PASSWORD);

        request.setAttribute(RequestAttributes.META_APP_CONFIG, metaAppConfig);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
