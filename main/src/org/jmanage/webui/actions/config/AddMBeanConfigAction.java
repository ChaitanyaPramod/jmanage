package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.MBeanConfig;
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
public class AddMBeanConfigAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        makeResponseNotCacheable(response);

        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        ApplicationConfig applicationConfig = context.getApplicationConfig();

        if(mbeanConfigForm.isApplicationCluster()){
            /* mbean has to be added to the cluster */
            applicationConfig = applicationConfig.getClusterConfig();
            assert applicationConfig != null: "application not part of cluster";
        }
        applicationConfig.addMBean(new MBeanConfig(mbeanConfigForm.getName(),
                mbeanConfigForm.getObjectName()));
        ApplicationConfigManager.updateApplication(applicationConfig);

        String logMsg = null;
        if(mbeanConfigForm.isApplicationCluster()){
            logMsg = "Added "+mbeanConfigForm.getObjectName()+" to " +
                    "application cluster " + applicationConfig.getName();
        }else{
            logMsg = "Added "+mbeanConfigForm.getObjectName()+" to " +
                    "application " + applicationConfig.getName();
        }
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                logMsg);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
