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
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.forms.ApplicationClusterForm;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.auth.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * date:  Oct 17, 2004
 * @author	Rakesh Kalra
 */
public class ShowApplicationClusterAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        AccessController.canAccess(context.getUser(), ACL_ADD_APPLICATIONS);
        ApplicationClusterForm clusterForm =
                (ApplicationClusterForm)actionForm;
        String applicationId = clusterForm.getApplicationId();
        List selectedApplications = null;
        if(applicationId != null){
            ApplicationConfig clusterConfig =
                    ApplicationConfigManager.getApplicationConfig(applicationId);
            assert clusterConfig.isCluster();
            clusterForm.setName(clusterConfig.getName());
            selectedApplications = clusterConfig.getApplications();
        }else{
            selectedApplications = new LinkedList();
        }

        List applications = new LinkedList();
        for(Iterator it=ApplicationConfigManager.getApplications().iterator();
            it.hasNext();){
            ApplicationConfig config = (ApplicationConfig)it.next();
            if(!config.isCluster() && !selectedApplications.contains(config)){
                applications.add(config);
            }
        }
        request.setAttribute("applications", applications);
        request.setAttribute("selectedApplications", selectedApplications);

        /*set current page for navigation*/
        if(applicationId != null){
            request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Edit Cluster");
        }else{
            request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Add Cluster");
        }

        return mapping.findForward(Forwards.SUCCESS);
    }
}
