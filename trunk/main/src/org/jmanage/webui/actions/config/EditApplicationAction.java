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
import org.jmanage.webui.forms.ApplicationForm;
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
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class EditApplicationAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        ApplicationForm appForm = (ApplicationForm)actionForm;
        ApplicationConfig config =
                ApplicationConfigManager.getApplicationConfig(
                        appForm.getApplicationId());
        assert config != null;

        config.setName(appForm.getName());
        config.setHost(appForm.getHost());
        if(appForm.getPort() != null)
            config.setPort(new Integer(appForm.getPort()));
            config.setURL(appForm.getURL());
            config.setUsername(appForm.getUsername());
        final String password = appForm.getPassword();
        if(password != null && !password.equals(config.getPassword())){
            config.setPassword(password);
        }

        ApplicationConfigManager.updateApplication(config);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Updated application "+"\""+config.getName()+"\"");
        return mapping.findForward(Forwards.SUCCESS);
    }
}