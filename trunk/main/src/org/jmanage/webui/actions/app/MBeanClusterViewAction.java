package org.jmanage.webui.actions.app;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.*;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * date:  Sep 17, 2004
 * @author	Rakesh Kalra
 */
public class MBeanClusterViewAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        final ObjectName objectName = context.getObjectName();
        final ApplicationConfig config = context.getApplicationConfig();
        assert config.isCluster();

        final List applications = config.getApplications();
        /* the ObjectInfo for the mbean that is being viewed */
        ObjectInfo objInfo = null;
        /* array that will be initialized with all attribute names for this
            mbean */
        String[] attributeNames = null;
        /* a list which constains list of attribute values for each application
            in the cluster. */
        final List appAttrList = new ArrayList(applications.size());
        for(Iterator it=applications.iterator(); it.hasNext(); ){
            ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
            ServerConnection serverConnection =
                    ServerConnector.getServerConnection(childAppConfig);
            /* assuming that all servers in this cluster have exact same
                object info, we will get the ObjectInfo from the first
                server in the list */
            if(objInfo == null){
                objInfo = serverConnection.getObjectInfo(objectName);
                assert objInfo != null;
                ObjectAttributeInfo[] attributes = objInfo.getAttributes();
                attributeNames = new String[attributes.length];
                for (int i = 0; i < attributes.length; i++) {
                    attributeNames[i] = attributes[i].getName();
                }
            }
            /* add attribute values of this application to the list*/
            appAttrList.add(
                    serverConnection.getAttributes(objectName, attributeNames));

        }

        request.setAttribute("objInfo", objInfo);
        request.setAttribute("appAttributeList", appAttrList);

        return mapping.findForward(Forwards.SUCCESS);
    }


}
