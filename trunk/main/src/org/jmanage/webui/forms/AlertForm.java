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
}
