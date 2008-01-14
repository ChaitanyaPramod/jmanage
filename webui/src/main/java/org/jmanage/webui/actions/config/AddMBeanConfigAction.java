/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.MBeanConfig;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Jul 21, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class AddMBeanConfigAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        AccessController.checkAccess(context.getServiceContext(), ACL_ADD_MBEAN_CONFIG);
        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        ApplicationConfig applicationConfig = context.getApplicationConfig();

        if(mbeanConfigForm.isApplicationCluster()){
            /* mbean has to be added to the cluster */
            applicationConfig = applicationConfig.getClusterConfig();
            assert applicationConfig != null: "application not part of cluster";
        }
        String logMsg = null;
        //TODO: Usage of DynaForm should clean up this.
        if(request.getParameter(RequestParams.MULTI_MBEAN_CONFIG) != null){
            final String[] objectNames = mbeanConfigForm.getName();
            for(int mbeanCtr=0; mbeanCtr < objectNames.length; mbeanCtr++ ){
                final String configName = request.getParameter(objectNames[mbeanCtr]);
                if(GenericValidator.isBlankOrNull(configName) ||
                        applicationConfig.containsMBean(objectNames[mbeanCtr]))
                    continue;
                applicationConfig.addMBean(new MBeanConfig(configName,
                        objectNames[mbeanCtr]));
                ApplicationConfigManager.updateApplication(applicationConfig);

                if(mbeanConfigForm.isApplicationCluster()){
                    logMsg = "Added "+objectNames[mbeanCtr]+" to " +
                            "application cluster " + applicationConfig.getName();
                }else{
                    logMsg = "Added "+objectNames[mbeanCtr]+" to " +
                            "application " + applicationConfig.getName();
                }
                logMsg += "\n";
            }
        }else{
            final String configName = mbeanConfigForm.getName()[0];
            applicationConfig.addMBean(new MBeanConfig(configName,
                    mbeanConfigForm.getObjectName()));
            ApplicationConfigManager.updateApplication(applicationConfig);

            if(mbeanConfigForm.isApplicationCluster()){
                logMsg = "Added "+mbeanConfigForm.getObjectName()+" to " +
                        "application cluster " + applicationConfig.getName();
            }else{
                logMsg = "Added "+mbeanConfigForm.getObjectName()+" to " +
                        "application " + applicationConfig.getName();
            }
        }
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                logMsg);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
