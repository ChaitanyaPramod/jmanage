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

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.GraphForm;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.GraphConfig;
import org.jmanage.core.config.GraphAttributeConfig;
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.Expression;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/**
 * Date: Jun 23, 2005 8:03:09 PM
 * @author Bhavana
 */
public class AddGraphAction extends BaseAction{
    /**
     * @param context
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        ApplicationConfig appConfig = context.getApplicationConfig();
        GraphForm form = (GraphForm)actionForm;
        String[] attributes = form.getAttributes();
        String[] displayNames = request.getParameterValues("displayNames");
        List graphAttrConfigs = new ArrayList(attributes.length);
        for(int i=0; i<attributes.length; i++){
            Expression expression = new Expression(attributes[i]);
            GraphAttributeConfig graphAttrConfig = new GraphAttributeConfig(
                    expression.getMBeanName(),expression.getTargetName(),
                    displayNames[i]);
            graphAttrConfigs.add(graphAttrConfig);
        }
        String graphId = request.getParameter(RequestParams.GRAPH_ID);
        GraphConfig graphConfig = null;
        if(graphId==null || graphId.equals("")){
            graphConfig = new GraphConfig(GraphConfig.getNextGraphId(),
                    form.getGraphName(), Long.parseLong(form.getPollInterval()),
                    appConfig, graphAttrConfigs);
            graphConfig.setYAxisLabel(form.getYAxisLabel());
            if(form.getScaleFactor() != null){
                graphConfig.setScaleFactor(new Double(form.getScaleFactor()));
                graphConfig.setScaleUp(Boolean.valueOf(form.getScaleUp()));
            }
            appConfig.addGraph(graphConfig);
        }else{
            graphConfig = appConfig.findGraph(graphId);
            graphConfig.setName(form.getGraphName());
            graphConfig.setAttributes(graphAttrConfigs);
            graphConfig.setPollingInterval(Long.parseLong(form.getPollInterval()));
            graphConfig.setYAxisLabel(form.getYAxisLabel());
            if(form.getScaleFactor() != null){
                graphConfig.setScaleFactor(new Double(form.getScaleFactor()));
                graphConfig.setScaleUp(Boolean.valueOf(form.getScaleUp()));
            }else{
                graphConfig.setScaleFactor(null);
                graphConfig.setScaleUp(null);
            }
            graphConfig.setAppConfig(appConfig);
        }

        ConfigurationService service = ServiceFactory.getConfigurationService();
        service.addGraph(Utils.getServiceContext(context), graphConfig);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
