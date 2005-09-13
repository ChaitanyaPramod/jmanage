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
package org.jmanage.webui.forms;

import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.webui.util.WebErrorCodes;
import org.jmanage.webui.validator.Validator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: May 26, 2005 4:46:35 PM
 * @author Bhavana
 */
public class AlertForm extends BaseForm{

    private String alertId;
    private String alertName;
    private String[] alertDelivery;
    private String subject;
    private String emailAddress;
    private String alertSourceType = AlertSourceConfig.SOURCE_TYPE_NOTIFICATION;
    private String expression;
    private String minAttributeValue;
    private String maxAttributeValue;
    private String stringAttributeValue;

    public String getStringAttributeValue() {
        return stringAttributeValue;
    }

    public void setStringAttributeValue(String stringAttributeValue) {
        this.stringAttributeValue = stringAttributeValue;
    }

    public String getMinAttributeValue() {
        return minAttributeValue;
    }

    public void setMinAttributeValue(String minAttributeValue) {
        this.minAttributeValue = minAttributeValue;
    }

    public String getMaxAttributeValue() {
        return maxAttributeValue;
    }

    public void setMaxAttributeValue(String maxAttributeValue) {
        this.maxAttributeValue = maxAttributeValue;
    }

    //private String application;

    /**public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }  */

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String[] getAlertDelivery() {
        return alertDelivery;
    }

    public void setAlertDelivery(String[] alertDelivery) {
        this.alertDelivery = alertDelivery;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAlertSourceType() {
        return alertSourceType;
    }

    public void setAlertSourceType(String alertSourceType) {
        this.alertSourceType = alertSourceType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * this method is needed becuase in the generic flow for attribute selection
     * the parameter used in attributes
     * @param attribute
     */
    public void setAttributes(String attribute){
        setExpression(attribute);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
        ActionErrors errors = super.validate(mapping, request);
        if(errors==null || errors.isEmpty()){
            if(alertDelivery[0] !=null && alertDelivery[0].equals("email")){
                Validator.validateRequired(emailAddress, "Email Address", errors);
            }
            if(alertSourceType.equals(AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR)){
                boolean validMin = Validator.validateRequired(minAttributeValue,
                        "minimum attribute value", errors);
                boolean validMax = Validator.validateRequired(maxAttributeValue,
                        "maximum attribute value", errors);
                if(validMin && validMax){
                    if(Double.valueOf(maxAttributeValue).doubleValue()<
                            Double.valueOf(minAttributeValue).doubleValue()){
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                                WebErrorCodes.MAX_ATTRIBUTE_VALUE_GREATER));
                    }
                }
            }if(alertSourceType.equals(AlertSourceConfig.SOURCE_TYPE_STRING_MONITOR)){
                Validator.validateRequired(stringAttributeValue,
                        "Attribute Value", errors);
            }
        }
        return errors;
    }
}
