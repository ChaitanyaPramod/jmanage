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
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.forms.AlertForm;
import org.jmanage.core.auth.AccessController;
import org.jmanage.core.auth.RoleManager;
import org.jmanage.core.config.AlertConfig;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.alert.AlertEngine;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Date: May 25, 2005 3:43:21 PM
 * @author Bhavana
 */
public class AddAlertAction extends BaseAction{
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
        AlertForm form = (AlertForm)actionForm;
        ApplicationConfig appConfig = context.getApplicationConfig();
        AlertConfig alertConfig = null;
        String alertId = request.getParameter(RequestParams.ALERT_ID);
        if(alertId==null || alertId.equals("")){
            alertConfig = new AlertConfig(AlertConfig.getNextAlertId(),
                    form.getAlertName(),
                    form.getAlertDelivery(),
                    form.getEmailAddress(),
                    form.getSubject());
            alertConfig.setAlertSourceConfig(getAlertSourceConfig(context,form));
            appConfig.addAlert(alertConfig);
        }else{
            alertConfig = appConfig.findAlertById(form.getAlertId());
            alertConfig.setAlertName(form.getAlertName());
            alertConfig.setSubject(form.getSubject());
            alertConfig.setAlertDelivery(form.getAlertDelivery());
            if(form.getEmailAddress()!=null){
                alertConfig.setEmailAddress(form.getEmailAddress());
            }
            alertConfig.setAlertSourceConfig(getAlertSourceConfig(context,form));
        }
        ApplicationConfigManager.updateApplication(appConfig);

        /* tell the AlertEngine about the new or modified AlertConfig*/
        AlertEngine.getInstance().updateAlertConfig(alertConfig);

        return mapping.findForward(Forwards.SUCCESS);
    }

    private AlertSourceConfig getAlertSourceConfig(WebContext context,
                                                   AlertForm form){
        AlertSourceConfig sourceConfig = null;
        if(AlertSourceConfig.SOURCE_TYPE_NOTIFICATION.equals(
                form.getAlertSourceType())){
            sourceConfig = new AlertSourceConfig(form.getObjectName(),
                    form.getNotificationType());
            sourceConfig.setApplicationConfig(context.getApplicationConfig());
        }else{
            assert false: "not supported type";
        }
        return sourceConfig;
    }
}
