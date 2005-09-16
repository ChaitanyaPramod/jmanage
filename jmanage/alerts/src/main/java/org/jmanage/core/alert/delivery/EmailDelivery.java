/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
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
