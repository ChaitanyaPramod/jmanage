package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.MBeanConfigForm;
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

        String logMsg = null;
        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        ApplicationConfig applicationConfig = context.getApplicationConfig();
        if(applicationConfig.removeMBean(mbeanConfigForm.getObjectName())
                != null){
            ApplicationConfigManager.updateApplication(applicationConfig);
            logMsg = "Removed mbean " + mbeanConfigForm.getObjectName() +
                        " from application "+ applicationConfig.getName();
        }else{
            ApplicationConfig clusterConfig =
                    applicationConfig.getClusterConfig();
            if(clusterConfig != null){
                clusterConfig.removeMBean(mbeanConfigForm.getObjectName());
                ApplicationConfigManager.updateApplication(clusterConfig);
                logMsg = "Removed mbean " + mbeanConfigForm.getObjectName() +
                    " from application cluster "+ clusterConfig.getName();
            }
        }

        if(logMsg != null){
            UserActivityLogger.getInstance().logActivity(
                    context.getUser().getUsername(),
                    logMsg);
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}