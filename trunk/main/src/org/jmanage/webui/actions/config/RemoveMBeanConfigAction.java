package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Jul 21, 2004
 * @author	Rakesh Kalra
 */
public class RemoveMBeanConfigAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        ApplicationConfig applicationConfig = context.getApplicationConfig();
        applicationConfig.removeMBean(mbeanConfigForm.getObjectName());
        ApplicationConfigManager.updateApplication(applicationConfig);
        return mapping.findForward(Forwards.SUCCESS);
    }

}
