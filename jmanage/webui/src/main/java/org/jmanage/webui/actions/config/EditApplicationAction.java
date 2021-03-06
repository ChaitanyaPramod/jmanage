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
import org.jmanage.core.config.event.ApplicationChangedEvent;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.alert.AlertEngine;
import org.jmanage.event.EventSystem;
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
public class EditApplicationAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        AccessController.checkAccess(context.getServiceContext(),
                ACL_EDIT_APPLICATIONS);
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
        if(!ApplicationForm.FORM_PASSWORD.equals(password)){
            config.setPassword(password);
        }

        Map<String, String> paramValues = config.getParamValues();
        if(appForm.getJndiFactory() != null){
            paramValues.put(ApplicationConfig.JNDI_FACTORY, appForm.getJndiFactory());
        }else{
            paramValues.remove(ApplicationConfig.JNDI_FACTORY);
        }

        if(appForm.getJndiURL() != null){
            paramValues.put(ApplicationConfig.JNDI_URL, appForm.getJndiURL());
        }else{
            paramValues.remove(ApplicationConfig.JNDI_URL);
        }

        ApplicationConfigManager.updateApplication(config);
        // TODO: this should be moved down to ApplicationConfigManager -rk
        EventSystem.getInstance().fireEvent(new ApplicationChangedEvent(config));
     
        // TODO: Use the event above to do this update - rk
        /* update the AlertEngine */
        AlertEngine.getInstance().updateApplication(config);

        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Updated application "+"\""+config.getName()+"\"");
        return mapping.findForward(Forwards.SUCCESS);
    }
}
