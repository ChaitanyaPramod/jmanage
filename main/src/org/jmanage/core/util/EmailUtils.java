package org.jmanage.core.util;

import org.jmanage.core.config.JManageProperties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 19, 2005
 * Time: 12:03:03 PM
 * To change this template use Options | File Templates.
 */
public class EmailUtils {

    public static void sendEmail(String to, String subject, String content) throws MessagingException{
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
