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
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Jul 21, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class RemoveMBeanConfigAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        AccessController.checkAccess(context.getServiceContext(),
                ACL_EDIT_MBEAN_CONFIG);
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