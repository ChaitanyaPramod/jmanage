/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.webui.actions.app;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.MBeanConfig;
import org.jmanage.core.management.*;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.services.ServiceUtils;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.MBeanConfigForm;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ShowMBeanAction extends BaseAction {

    private static final Logger logger = Loggers.getLogger(ShowMBeanAction.class);

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        makeResponseNotCacheable(response);

        final ObjectName objectName = context.getObjectName();
        final ApplicationConfig config = context.getApplicationConfig();
        final MBeanConfig configuredMBean =
                config.findMBeanByObjectName(objectName.getCanonicalName());
        AccessController.checkAccess(context.getServiceContext(),
                ACL_VIEW_APPLICATIONS);
        if(configuredMBean != null)
            AccessController.checkAccess(context.getServiceContext(),
                    ACL_VIEW_MBEANS);
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
        /* a Map which constains list of attribute values for each application
            in the cluster.
            ApplicationConfig is the key and attribute List is the value*/
        final Map appConfigToAttrListMap = new HashMap(applications.size());
        for(Iterator it=applications.iterator(); it.hasNext(); ){
            ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
            ServerConnection serverConnection = null;
            try {
                serverConnection =
                        ServerConnector.getServerConnection(childAppConfig);
                /* assuming that all servers in this cluster have exact same
                    object info, we will get the ObjectInfo from the first
                    server in the list (could be further down in the list,
                    if the first server(s) is down */
                if(objInfo == null){
                    objInfo = serverConnection.getObjectInfo(objectName);
                    assert objInfo != null;
                    ObjectAttributeInfo[] attributes = objInfo.getAttributes();
                    attributeNames = new String[attributes.length];
                    for (int i = 0; i < attributes.length; i++) {
                        // TODO: we should only add the readable attributes here
                        attributeNames[i] = attributes[i].getName();
                    }
                }
                /* add attribute values of this application to the map*/
                appConfigToAttrListMap.put(childAppConfig,
                        serverConnection.getAttributes(objectName, attributeNames));
            } catch (ConnectionFailedException e){
                logger.log(Level.FINE, "Error retrieving attributes for:" +
                        childAppConfig.getName(), e);
                /* add null, indicating that the server is down */
                appConfigToAttrListMap.put(childAppConfig, null);
            } finally{
                ServiceUtils.close(serverConnection);
            }
        }
        /* if objInfo is null, that means that we couldn't get connection to
            any server */
        if(objInfo == null){
            throw new ConnectionFailedException(null);
        }

        request.setAttribute("objInfo", objInfo);
        request.setAttribute("appConfigToAttrListMap", appConfigToAttrListMap);

        /* setup the form to be used in the html form */
        MBeanConfigForm mbeanConfigForm = (MBeanConfigForm)actionForm;
        mbeanConfigForm.setObjectName(objectName.getCanonicalName());

        ApplicationConfig appConfig = context.getApplicationConfig();
        MBeanConfig mbeanConfig =
                appConfig.findMBeanByObjectName(objectName.getCanonicalName());
        if(mbeanConfig != null){
            if(appConfig.isCluster()){
                request.setAttribute("mbeanIncludedIn", "cluster");
            }else{
                request.setAttribute("mbeanIncludedIn", "application");
            }
            request.setAttribute("mbeanConfig", mbeanConfig);
        }else{
            ApplicationConfig clusterConfig = appConfig.getClusterConfig();
            if(clusterConfig != null){
                mbeanConfig =
                    clusterConfig.findMBeanByObjectName(objectName.getCanonicalName());
            }
            if(mbeanConfig != null){
                request.setAttribute("mbeanIncludedIn", "cluster");
                request.setAttribute("mbeanConfig", mbeanConfig);
            }
        }

        return mapping.findForward(Forwards.SUCCESS);
    }
}
