/**
* Copyright (c) 2004-2005 jManage.org. All rights reserved.
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.core.alert.delivery;

import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.alert.AlertInfo;

import java.io.File;

/**
 * Queue for Email Alerts. This queue is used when jManage is not able
 * to deliver emails.
 *
 * <p>
 * Date:  Nov 6, 2005
 * @author	Rakesh Kalra
 */
public class EmailAlerts extends PersistedAlerts{

    private static final String EMAIL_ALERTS_FILE =
            CoreUtils.getDataDir() + File.separator + "email-alerts.xml";

    private static final EmailAlerts instance = new EmailAlerts();

    public static EmailAlerts getInstance(){
        return instance;
    }

    private EmailAlerts(){
        new EmailDeliveryThread().start();
    }

    protected String getPersistedFileName() {
        return EMAIL_ALERTS_FILE;
    }

    private class EmailDeliveryThread extends Thread{

        EmailDelivery delivery = new EmailDelivery();

        public void run(){
            while(true){
                AlertInfo alertInfo = EmailAlerts.this.remove();
                while(alertInfo != null){
                    delivery.deliver(alertInfo);
                    alertInfo = EmailAlerts.this.remove();
                }
                try {
                    /* sleep for a minute */
                    sleep(60*1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
