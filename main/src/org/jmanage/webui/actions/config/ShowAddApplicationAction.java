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
import org.jmanage.core.module.ModuleRegistry;
import org.jmanage.core.module.ModuleConfig;
import org.jmanage.core.auth.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This action class creates MetaApplicationConfig instance based on the type of
 * application being added and sets in the request to make the ApplicationForm
 * dynamic.
 *
 * Date: Nov 3, 2004 12:46:00 AM
 * @author Shashank Bellary 
 */
public class ShowAddApplicationAction extends BaseAction {
    /**
     *
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
        AccessController.canAccess(context.getUser(), ACL_ADD_APPLICATIONS);
        ApplicationForm appForm = (ApplicationForm)actionForm;
        ModuleConfig moduleConfig = ModuleRegistry.getModule(appForm.getType());
        request.setAttribute(RequestAttributes.META_APP_CONFIG,
                moduleConfig.getMetaApplicationConfig());
        return mapping.findForward(Forwards.SUCCESS);
    }
}
