package org.jmanage.webui.actions.app;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ObjectAttributeInfo;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

        ServerConnection serverConnection = context.getServerConnection();


        ObjectInfo objInfo = serverConnection.getObjectInfo(objectName);

        ObjectAttributeInfo[] attributes = objInfo.getAttributes();
        String[] attributeNames = new String[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            attributeNames[i] = attributes[i].getName();
        }

        List attrList =
                serverConnection.getAttributes(objectName, attributeNames);

        request.setAttribute("objInfo", objInfo);
        request.setAttribute("attributeList", attrList);

        /* setup the form to be used in the html form */
        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        mbeanConfigForm.setObjectName(objectName.getCanonicalName());

        ApplicationConfig appConfig = context.getApplicationConfig();
        if(appConfig.containsMBean(objectName.getCanonicalName())){
            request.setAttribute("mbeanIncluded", "true");
        }

        return mapping.findForward(Forwards.SUCCESS);
    }
}
