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
import java.util.ArrayList;
import java.util.Iterator;

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
        final ApplicationConfig config = context.getApplicationConfig();

        List applications = null;
        if(config.isCluster()){
            applications = config.getApplications();
        }else{
            applications = new ArrayList(1);
            applications.add(config);
        }

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

        /* setup the form to be used in the html form */
        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        mbeanConfigForm.setObjectName(objectName.getCanonicalName());

        ApplicationConfig appConfig = context.getApplicationConfig();
        if(appConfig.containsMBean(objectName.getCanonicalName())){
            if(appConfig.isCluster()){
                request.setAttribute("mbeanIncludedIn", "cluster");
            }else{
                request.setAttribute("mbeanIncludedIn", "application");
            }
        }else{
            ApplicationConfig clusterConfig = appConfig.getClusterConfig();
            if(clusterConfig != null &&
                    clusterConfig.containsMBean(objectName.getCanonicalName())){
                request.setAttribute("mbeanIncludedIn", "cluster");
            }
        }

        return mapping.findForward(Forwards.SUCCESS);
    }
}
