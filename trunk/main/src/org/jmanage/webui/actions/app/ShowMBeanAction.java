package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebContext;
import org.jmanage.core.connector.MBeanServerConnectionFactory;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.management.*;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ShowMBeanAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        final ObjectName objectName = context.getObjectName();
        MBeanServer mbeanServer = context.getMBeanServer();
        MBeanInfo mbeanInfo = mbeanServer.getMBeanInfo(objectName);

        // TODO: sort the attributes in alphabatic order
        MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
        String[] attributeNames = new String[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            attributeNames[i] = attributes[i].getName();
        }

        AttributeList attrList =
                mbeanServer.getAttributes(objectName, attributeNames);

        request.setAttribute("mbeanInfo", mbeanInfo);
        request.setAttribute("attributeList", attrList);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
