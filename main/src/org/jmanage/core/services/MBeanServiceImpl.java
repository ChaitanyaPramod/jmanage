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
package org.jmanage.core.services;

import org.jmanage.core.management.*;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.MBeanConfig;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.core.util.Loggers;
import org.jmanage.webui.util.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * date:  Feb 21, 2005
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class MBeanServiceImpl implements MBeanService {

    private static final String DEFAULT_FILTER = "*:*";
    private static final Logger logger =
            Loggers.getLogger(MBeanServiceImpl.class);

    public List getMBeans(ServiceContext context,
                          String applicationName,
                          String filter)
            throws ServiceException {

        ServerConnection serverConnection =
                ServiceUtils.getServerConnection(applicationName);

        if(filter == null){
            filter = DEFAULT_FILTER;
        }
        Set mbeans =
                serverConnection.queryNames(new ObjectName(filter));
        ArrayList mbeanDataList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator();it.hasNext(); ){
            ObjectName objName = (ObjectName)it.next();
            mbeanDataList.add(new MBeanData(objName.getCanonicalName()));
        }
        return mbeanDataList;
    }

    public ObjectInfo getMBean(ServiceContext context,
                               String appName,
                               String mbeanName)
            throws ServiceException {

        ServerConnection serverConnection =
                ServiceUtils.getServerConnection(appName);
        mbeanName = resolveMBeanName(appName, mbeanName);
        ObjectInfo objectInfo =
                serverConnection.getObjectInfo(new ObjectName(mbeanName));
        return objectInfo;
    }

    /**
     * @return list of attribute values for given attributes
     */
    public List getAttributes(ServiceContext context,
                              String appName,
                              String mbeanName,
                              String[] attributes)
            throws ServiceException {
        ServerConnection connection =
                ServiceUtils.getServerConnection(appName);
        mbeanName = resolveMBeanName(appName, mbeanName);
        return connection.getAttributes(new ObjectName(mbeanName), attributes);
    }

    private String resolveMBeanName(String appName, String mbeanName){
        ApplicationConfig appConfig =
                ApplicationConfigManager.getApplicationConfigByName(appName);
        /* check if the mbeanName is the configured mbean name */
        MBeanConfig mbeanConfig = appConfig.findMBean(mbeanName);
        if(mbeanConfig != null){
            mbeanName = mbeanConfig.getObjectName();
        }
        return mbeanName;
    }

    /**
     * Updates MBean attributes at a stand alone application level or at a
     * cluster level.
     *
     * @param context
     * @param request
     * @param objName
     * @param appName
     * @throws ServiceException
     */
    public void updateAttributes(ServiceContext context,
                                 HttpServletRequest request,
                                 String objName,
                                 String appName)
            throws ServiceException{
        ApplicationConfig appConfig =
                ApplicationConfigManager.getApplicationConfigByName(appName);
        ObjectName objectName = new ObjectName(objName);

        List applications = null;
        if(appConfig.isCluster()){
            applications = appConfig.getApplications();
        }else{
            applications = new ArrayList(1);
            applications.add(appConfig);
        }
        StringBuffer erroneousApps = new StringBuffer("");
        for(Iterator it=applications.iterator(); it.hasNext(); ){
            final ApplicationConfig childAppConfig =
                        (ApplicationConfig)it.next();
            try{
                final ServerConnection serverConnection =
                        ServerConnector.getServerConnection(childAppConfig);
                List attributeList = buildAttributeList(request,
                        childAppConfig.getApplicationId());
                serverConnection.setAttributes(objectName, attributeList);
                String logString = getLogString(attributeList);
                UserActivityLogger.getInstance().logActivity(
                        context.getUser().getUsername(),
                        "Updated the attributes of application:" +
                        childAppConfig.getName() + ", object name:" +
                        objectName.getCanonicalName() +
                        logString);
            }catch(ConnectionFailedException e){
                logger.log(Level.FINE, "Error connecting to :" +
                        childAppConfig.getName(), e);
                erroneousApps.append(childAppConfig.getName());
                erroneousApps.append(",");
            }
        }
        if(erroneousApps.toString().endsWith(",")){
            throw new ServiceException(ErrorCodes.ERRONEOUS_APPS,
                    erroneousApps.substring(0, erroneousApps.length() - 1));
        }
    }

    /**
     * request parameter is of the format:
     * attr+<applicationId>+<attrName>+<attrType>
     *
     */
    private List buildAttributeList(HttpServletRequest request,
                                    String applicationId){

        Enumeration enum = request.getParameterNames();
        List attributeList = new LinkedList();
        while(enum.hasMoreElements()){
            String param = (String)enum.nextElement();
            if(param.startsWith("attr+")){
                StringTokenizer tokenizer = new StringTokenizer(param, "+");
                if(tokenizer.countTokens() < 4){
                    throw new RuntimeException("Invalid param name: " + param);
                }
                tokenizer.nextToken(); // equals to "attr"
                if(applicationId.equals(tokenizer.nextToken())){ // applicationId
                    String attrName = tokenizer.nextToken();
                    String attrType = tokenizer.nextToken();
                    String attrValue = request.getParameter(param);
                    ObjectAttribute attribute = new ObjectAttribute(attrName,
                            Utils.getTypedValue(attrValue, attrType));
                    attributeList.add(attribute);
                }
            }
        }
        return attributeList;
    }

    /**
     *
     * @param attributes
     * @return
     */
    private String getLogString(List attributes){
        StringBuffer logString = new StringBuffer("");
        for(Iterator iterator = attributes.iterator(); iterator.hasNext(); ){
            ObjectAttribute attribute = (ObjectAttribute)iterator.next();
            logString.append(" [");
            logString.append(attribute.getName());
            logString.append("=");
            logString.append(attribute.getValue());
            logString.append("]");
        }
        return logString.toString();
    }
}
