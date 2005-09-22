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
import org.jmanage.webui.util.*;
import org.jmanage.webui.forms.AlertForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.AlertConfig;
import org.jmanage.core.config.AlertSourceConfig;
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
 * Date: Jun 21, 2005 10:57:31 AM
 * @author Bhavana
 */
public class ShowEditAlertAction extends BaseAction{
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        AccessController.checkAccess(Utils.getServiceContext(context), ACL_EDIT_ALERT);
        AlertForm form = (AlertForm)actionForm;
        String alertId = request.getParameter(RequestParams.ALERT_ID);
        ApplicationConfig appConfig = context.getApplicationConfig();
        AlertConfig alertConfig = appConfig.findAlertById(alertId);
        if(alertConfig!=null){
            form.setAlertName(alertConfig.getAlertName());
            form.setSubject(alertConfig.getSubject());
            form.setAlertDelivery(alertConfig.getAlertDelivery());
            form.setEmailAddress(alertConfig.getEmailAddress());
            AlertSourceConfig alertSrcConfig = alertConfig.getAlertSourceConfig();
            String sourceType = alertSrcConfig.getSourceType();
            form.setAlertSourceType(alertSrcConfig.getSourceType());
            request.setAttribute("alertSourceType",sourceType);
            request.setAttribute("sourceMBean", alertSrcConfig.getObjectName());
            // expression
            Expression expression = null;
            if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_NOTIFICATION)){
                expression = new Expression(null, alertSrcConfig.getObjectName(),
                        alertSrcConfig.getNotificationType());
                request.setAttribute("notificationType",
                        alertSrcConfig.getNotificationType());
            }else if(sourceType.equals(
                    AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR)){
                expression = new Expression(null, alertSrcConfig.getObjectName(),
                        alertSrcConfig.getAttributeName());
                request.setAttribute("attribute", alertSrcConfig.getAttributeName());
                form.setMinAttributeValue(alertSrcConfig.getLowThreshold()
                        .toString());
                form.setMaxAttributeValue(alertSrcConfig.getHighThreshold()
                        .toString());
            }else if(sourceType.equals(
                    AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR)){
                expression = new Expression(null, alertSrcConfig.getObjectName(),
                        alertSrcConfig.getAttributeName());
                request.setAttribute("attribute", alertSrcConfig.getAttributeName());
                form.setStringAttributeValue(alertSrcConfig.getStringAttributeValue());
            }
            form.setExpression(expression.toString());
        }
        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Edit Alert");
        return mapping.findForward(Forwards.SUCCESS);
    }
}
