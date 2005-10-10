/**
* Copyright (c) 2004-2005 jManage.org
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

import org.jmanage.core.util.EmailUtils;
import org.jmanage.core.config.AlertConfig;
import org.jmanage.core.alert.AlertDelivery;
import org.jmanage.core.alert.AlertInfo;

import javax.mail.MessagingException;

/**
 *
 * Date:  Jul 1, 2005
 * @author	Rakesh Kalra
 */
public class EmailDelivery implements AlertDelivery {

    public void deliver(AlertInfo alertInfo) {
        try {
            EmailUtils.sendEmail(
                    alertInfo.getEmailAddress(),
                    alertInfo.getSubject(),
                    getEmailContent(alertInfo));
        } catch (MessagingException e) {
            // todo: need to queue up the message
            throw new RuntimeException(e);
        }
    }

    private String getEmailContent(AlertInfo alertInfo){
        // todo: implement
        return "Alert Content for " + alertInfo.getAlertName();
    }
}
