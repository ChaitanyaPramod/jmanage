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
package org.jmanage.core.util;

import org.jmanage.core.config.JManageProperties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Bhavana
 * @author Rakesh Kalra
 */
public class EmailUtils {

    private static final Logger logger = Loggers.getLogger(EmailUtils.class);

    public static void sendEmail(String to, String subject, String content)
            throws MessagingException{

        logger.fine("Sending email to: " + to);

        Properties properties = new Properties();
        properties.put("mail.user", JManageProperties.getAlertEmailFromName());
        properties.put("mail.host", JManageProperties.getEmailHost());
        properties.put("mail.from", JManageProperties.getAlertEmailFrom());
        properties.put("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setText(content);
        Transport.send(message);
    }
}
