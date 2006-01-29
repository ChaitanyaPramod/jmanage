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
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.GraphForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.GraphConfig;
import org.jmanage.core.config.GraphAttributeConfig;
import org.jmanage.core.util.Expression;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Iterator;

/**
 * Date: Sep 14, 2005 3:52:33 PM
 * @author Bhavana
 */
public class ShowEditGraphAction extends BaseAction{
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        AccessController.checkAccess(Utils.getServiceContext(context),ACL_EDIT_GRAPH);
        GraphForm form = (GraphForm)actionForm;
        String graphId = form.getGraphId();
        ApplicationConfig appConfig = context.getApplicationConfig();
        GraphConfig graphConfig = appConfig.findGraph(graphId);
        if(graphConfig!=null){
            form.setGraphName(graphConfig.getName());
            form.setPollInterval(String.valueOf(graphConfig.getPollingInterval()));
            form.setYAxisLabel(graphConfig.getYAxisLabel());
            if(graphConfig.getScaleFactor() != null){
                form.setScaleFactor(graphConfig.getScaleFactor().toString());
                form.setScaleUp(graphConfig.isScaleUp().booleanValue());
            }

            List attributes = graphConfig.getAttributes();
            String[] attributeNames = new String[attributes.size()];
            String[] objectNames = new String[attributes.size()];
            String[] displayNames = new String[attributes.size()];
            int i=0;
            for(Iterator itr=attributes.iterator(); itr.hasNext();){
                GraphAttributeConfig graphAttrConfig =
                        (GraphAttributeConfig)itr.next();
                attributeNames[i] = graphAttrConfig.getAttribute();
                displayNames[i] = graphAttrConfig.getDisplayName();
                objectNames[i++] = graphAttrConfig.getMBean();
            }
            request.setAttribute("attributeNames", attributeNames);
            request.setAttribute("objectNames",objectNames);
            request.setAttribute("displayNames",displayNames);
        }
        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Edit Graph");
        return mapping.findForward(Forwards.SUCCESS);
    }
}
