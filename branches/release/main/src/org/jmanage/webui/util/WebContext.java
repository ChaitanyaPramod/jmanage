package org.jmanage.webui.util;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.connector.MBeanServerConnectionFactory;
import org.jmanage.core.auth.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.security.auth.Subject;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class WebContext {

    private ApplicationConfig appConfig;
    private MBeanServer mbeanServer;
    private HttpServletRequest request;
    private HttpSession session;

    private WebContext(HttpServletRequest request) {

        this.request = request;
        this.session = request.getSession();
        final String appId = request.getParameter(RequestParams.APPLICATION_ID);
        if (appId != null) {
            appConfig =
                    ApplicationConfigManager.getApplicationConfig(appId);
            request.setAttribute(RequestAttributes.APPLICATION_CONFIG,
                    appConfig);
        }
    }

    public ApplicationConfig getApplicationConfig() {
        return appConfig;
    }

    public MBeanServer getMBeanServer() {
        assert appConfig != null;
        if (mbeanServer == null) {
            mbeanServer =
                    MBeanServerConnectionFactory.getConnection(appConfig);
        }
        return mbeanServer;
    }

    public ObjectName getObjectName() {
        try {
            final String objectNameString =
                    request.getParameter(RequestParams.OBJECT_NAME);
            return new ObjectName(objectNameString);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebContext get(HttpServletRequest request) {
        WebContext context =
                (WebContext) request.getAttribute(RequestAttributes.WEB_CONTEXT);
        if (context == null) {
            context = new WebContext(request);
            request.setAttribute(RequestAttributes.WEB_CONTEXT, context);
        }
        return context;
    }

    public Subject getSubject() {
        return (Subject) session.getAttribute(RequestAttributes.SUBJECT);
    }

    public void setSubject(Subject subject) {
        session.setAttribute(RequestAttributes.SUBJECT, subject);
    }

    public void removeSubject() {
        session.removeAttribute(RequestAttributes.SUBJECT);
    }

    public User getUser(){
        User user = null;
        final Subject subject = getSubject();
        if(subject != null){
            user = (User)subject.getPrincipals().iterator().next();
        }
        return user;
    }

}