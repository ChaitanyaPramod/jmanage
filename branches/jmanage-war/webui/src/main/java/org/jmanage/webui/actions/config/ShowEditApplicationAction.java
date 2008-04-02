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
import org.jmanage.webui.forms.ApplicationForm;
import org.jmanage.core.config.*;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra, Shashank Bellary
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
                                 HttpServletResponse response)
            throws Exception{
        AccessController.checkAccess(context.getServiceContext(),
                ACL_EDIT_APPLICATIONS);
        ApplicationConfig config = context.getApplicationConfig();
        ApplicationForm appForm = (ApplicationForm)actionForm;
        ApplicationType appType = config.getApplicationType();
        MetaApplicationConfig metaAppConfig =
                appType.getModule().getMetaApplicationConfig();

        /* populate the form */
        appForm.setApplicationId(config.getApplicationId());
        appForm.setName(config.getName());
        appForm.setType(config.getType());
        if(metaAppConfig.isDisplayHost())
            appForm.setHost(config.getHost());
        if(metaAppConfig.isDisplayPort())
            appForm.setPort(String.valueOf(config.getPort()));
        if(metaAppConfig.isDisplayURL())
            appForm.setURL(config.getURL());
        if(metaAppConfig.isDisplayUsername())
            appForm.setUsername(config.getUsername());
        if(metaAppConfig.isDisplayPassword() && config.getPassword() != null
                && config.getPassword().length()>0)
            appForm.setPassword(ApplicationForm.FORM_PASSWORD);
        appForm.setHeartBeatCheckIntervalInSeconds(
        		config.getHeartBeatCheckIntervalInSeconds() + "");
        
        // for jsr160
        Map paramValues = config.getParamValues();
        appForm.setJndiFactory((String)paramValues.get(ApplicationConfig.JNDI_FACTORY));
        appForm.setJndiURL((String)paramValues.get(ApplicationConfig.JNDI_URL));

        request.setAttribute(RequestAttributes.META_APP_CONFIG, metaAppConfig);

        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Edit Application");

        return mapping.findForward(Forwards.SUCCESS);
    }
}
