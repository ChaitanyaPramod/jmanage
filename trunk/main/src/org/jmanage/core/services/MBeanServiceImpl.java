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

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.MBeanConfig;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.data.OperationResultData;
import org.jmanage.core.data.AttributeListData;
import org.jmanage.core.management.*;
import org.jmanage.core.util.*;
import org.jmanage.util.StringUtils;

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

    private static final Logger logger = Loggers.getLogger(MBeanService.class);

    private static final String DEFAULT_FILTER = "*:*";

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
     * @return list of all attribute values
     */
    public AttributeListData[] getAttributes(ServiceContext context,
                                             String appName,
                                             String mbeanName)
            throws ServiceException {

        ServerConnection serverConnection =
                ServiceUtils.getServerConnectionEvenIfCluster(appName);
        mbeanName = resolveMBeanName(appName, mbeanName);
        ObjectInfo objInfo =
                serverConnection.getObjectInfo(new ObjectName(mbeanName));
        assert objInfo != null;
        ObjectAttributeInfo[] attributes = objInfo.getAttributes();
        List attributeNames = new LinkedList();
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].isReadable()){
                attributeNames.add(attributes[i].getName());
            }
        }

        ObjectName objectName = new ObjectName(mbeanName);
        String[] attributeArray = StringUtils.listToStringArray(attributeNames);
        ApplicationConfig appConfig =
                ServiceUtils.getApplicationConfigByName(appName);
        AttributeListData[] resultData = null;;
        if(appConfig.isCluster()){
            /* we need to perform this operation for all servers
                in this cluster */
            resultData = new AttributeListData[appConfig.getApplications().size()];
            int index = 0;
            for(Iterator it=appConfig.getApplications().iterator(); it.hasNext(); index++){
                ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
                resultData[index] =
                        getAttributes(childAppConfig, objectName, attributeArray);
            }
        }else{
            resultData = new AttributeListData[1];
            resultData[0] =
                    getAttributes(appConfig, objectName, attributeArray);
        }
        return resultData;
    }

    /**
     *
     * @return list of attribute values for given attributes
     */
    public AttributeListData[] getAttributes(ServiceContext context,
                                             String appName,
                                             String mbeanName,
                                             String[] attributes,
                                             boolean handleCluster)
            throws ServiceException {

        ObjectName objectName = new ObjectName(mbeanName);
        ApplicationConfig appConfig =
                ServiceUtils.getApplicationConfigByName(appName);
        AttributeListData[] resultData = null;;
        if(appConfig.isCluster()){
            if(!handleCluster){
                throw new ServiceException(ErrorCodes.OPERATION_NOT_SUPPORTED_FOR_CLUSTER);
            }
            /* we need to perform this operation for all servers
                in this cluster */
            resultData = new AttributeListData[appConfig.getApplications().size()];
            int index = 0;
            for(Iterator it=appConfig.getApplications().iterator(); it.hasNext(); index++){
                ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
                resultData[index] =
                        getAttributes(childAppConfig, objectName, attributes);
            }
        }else{
            resultData = new AttributeListData[1];
            resultData[0] =
                    getAttributes(appConfig, objectName, attributes);
        }
        return resultData;
    }

    /**
     * @return list of attribute values for given attributes
     */
    private AttributeListData getAttributes(ApplicationConfig appConfig,
                                            ObjectName objectName,
                                            String[] attributes)
            throws ServiceException {

        ServerConnection connection =
                ServerConnector.getServerConnection(appConfig);
        List attrList =
                connection.getAttributes(objectName, attributes);
        return new AttributeListData(appConfig.getName(), attrList);
    }

    public OperationResultData[] invoke(ServiceContext context,
                                        String appName,
                                        String mbeanName,
                                        String operationName,
                                        String[] params)
            throws ServiceException {

        mbeanName = resolveMBeanName(appName, mbeanName);
        ObjectName objectName = new ObjectName(mbeanName);
        /* try to determine the method, based on params */
        ObjectOperationInfo operationInfo =
                findOperation(appName, objectName, operationName,
                        params!=null?params.length:0);
        return invoke(context, appName, objectName, operationName, params,
                operationInfo.getParameters());
    }

    /**
     * Invokes MBean operation
     * @return
     * @throws ServiceException
     */
    public OperationResultData[] invoke(ServiceContext context,
                                        String appName,
                                        ObjectName objectName,
                                        String operationName,
                                        String[] params,
                                        String[] signature)
            throws ServiceException {

        ApplicationConfig appConfig =
                ServiceUtils.getApplicationConfigByName(appName);

        OperationResultData[] resultData = null;;
        if(appConfig.isCluster()){
            /* we need to perform this operation for all servers
                in this cluster */
            resultData = new OperationResultData[appConfig.getApplications().size()];
            int index = 0;
            for(Iterator it=appConfig.getApplications().iterator(); it.hasNext(); index++){
                ApplicationConfig childAppConfig = (ApplicationConfig)it.next();
                resultData[index] =
                        executeMBeanOperation(context, childAppConfig, objectName,
                                operationName, params, signature);
            }
        }else{
            resultData = new OperationResultData[1];
            resultData[0] =
                    executeMBeanOperation(context, appConfig, objectName,
                            operationName, params, signature);
        }
        return resultData;
    }

    private static OperationResultData executeMBeanOperation(
            ServiceContext context,
            ApplicationConfig appConfig,
            ObjectName objectName,
            String operationName,
            String[] params,
            String[] signature){

        OperationResultData resultData =
                new OperationResultData(appConfig.getName());
        try {
            final ServerConnection serverConnection =
                    ServerConnector.getServerConnection(appConfig);
            Object[] typedParams = CoreUtils.getTypedArray(params, signature);
            final Object result = serverConnection.invoke(objectName, operationName,
                            typedParams, signature);
            resultData.setOutput(result.toString());
            UserActivityLogger.getInstance().logActivity(
                    context.getUser().getUsername(),
                    "Performed "+operationName+" on "+objectName.getCanonicalName()
                    + " in application " + appConfig.getName());
        } catch (ConnectionFailedException e) {
            logger.log(Level.INFO, "Error executing operation " +
                    operationName + " on " + objectName, e);
            resultData.setResult(OperationResultData.RESULT_ERROR);
            resultData.setErrorString(ErrorCatalog.getMessage(ErrorCodes.CONNECTION_FAILED));
        } catch (Exception e){
            logger.log(Level.SEVERE, "Error executing operation " +
                    operationName + " on " + objectName, e);
            resultData.setResult(OperationResultData.RESULT_ERROR);
            resultData.setErrorString(e.getMessage());
        }
        return resultData;
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

    private ObjectOperationInfo findOperation(String appName,
                                              ObjectName objectName,
                                              String operationName,
                                              int paramCount){

        ServerConnection connection =
                ServiceUtils.getServerConnectionEvenIfCluster(appName);
        ObjectInfo objectInfo = connection.getObjectInfo(objectName);
        ObjectOperationInfo[] operationInfo = objectInfo.getOperations();
        for(int i=0; i< operationInfo.length; i++){
            if(operationInfo[i].getName().equals(operationName) &&
                    operationInfo[i].getSignature().length == paramCount){
                return operationInfo[i];
            }
        }
        throw new ServiceException(ErrorCodes.INVALID_MBEAN_OPERATION,
                operationName, objectName);
    }

    //TODO: should we first check that all apps in a cluster are up,
    // before updating?  - rk
    public AttributeListData[] setAttributes(ServiceContext context,
                                             String objName,
                                             String appName,
                                             String[][] attributes)
            throws ServiceException{
        List applications = getApplications(appName);
        ObjectName objectName = new ObjectName(objName);
        List attributeList = buildAttributeList(appName, objectName, attributes);
        AttributeListData[] attrListData =
                new AttributeListData[applications.size()];
        int index = 0;
        for(Iterator it=applications.iterator(); it.hasNext(); index++){
            final ApplicationConfig childAppConfig =
                        (ApplicationConfig)it.next();
            attrListData[index] = updateAttributes(context, childAppConfig,
                    objectName, attributeList);
        }
        return attrListData;
    }

    /**
     * Updates MBean attributes at a stand alone application level or at a
     * cluster level.
     *
     * TODO: remove the usage of HttpServletRequest - rk
     *
     * @param context
     * @param request
     * @param objName
     * @param appName
     * @throws ServiceException
     */
    public AttributeListData[] setAttributes(ServiceContext context,
                                             HttpServletRequest request,
                                             String objName,
                                             String appName)
            throws ServiceException{
        List applications = getApplications(appName);
        ObjectName objectName = new ObjectName(objName);
        AttributeListData[] attrListData =
                new AttributeListData[applications.size()];
        int index = 0;
        for(Iterator it=applications.iterator(); it.hasNext(); index++){
            final ApplicationConfig childAppConfig =
                        (ApplicationConfig)it.next();
            List attributeList = buildAttributeList(request,
                        childAppConfig.getApplicationId());
            attrListData[index] = updateAttributes(context, childAppConfig,
                    objectName, attributeList);
        }
        return attrListData;
    }

    private AttributeListData updateAttributes(ServiceContext context,
                                               ApplicationConfig appConfig,
                                               ObjectName objectName,
                                               List attributeList){
        AttributeListData attrListData = null;
        try{
            final ServerConnection serverConnection =
                    ServerConnector.getServerConnection(appConfig);
            attributeList =
                    serverConnection.setAttributes(objectName, attributeList);
            attrListData = new AttributeListData(appConfig.getName(),
                    attributeList);
            String logString = getLogString(attributeList);
            UserActivityLogger.getInstance().logActivity(
                    context.getUser().getUsername(),
                    "Updated the attributes of application:" +
                    appConfig.getName() + ", object name:" +
                    objectName.getCanonicalName() +
                    logString);
        }catch(ConnectionFailedException e){
            logger.log(Level.FINE, "Error connecting to :" +
                    appConfig.getName(), e);
            attrListData = new AttributeListData(appConfig.getName());
        }
        return attrListData;
    }

    /**
     * Converts a two di
     * @param attributes
     * @return
     */
    private List buildAttributeList(String appName,
                                    ObjectName objectName,
                                    String[][] attributes){
        ServerConnection connection =
                ServiceUtils.getServerConnectionEvenIfCluster(appName);
        ObjectInfo objInfo = connection.getObjectInfo(objectName);
        ObjectAttributeInfo[] objAttributes = objInfo.getAttributes();
        List attributeList = new LinkedList();
        for(int i=0; i<attributes.length; i++){
            String attribute = attributes[i][0];
            String type = getAttributeType(objAttributes, attribute, objectName);
            Object value = CoreUtils.getTypedValue(attributes[i][1], type);
            ObjectAttribute objAttribute =
                    new ObjectAttribute(attribute, value);
            attributeList.add(objAttribute);
        }
        return attributeList;
    }

    private String getAttributeType(ObjectAttributeInfo[] objAttributes,
                                    String attribute,
                                    ObjectName objectName){
        for(int i=0; i<objAttributes.length; i++){
            if(objAttributes[i].getName().equals(attribute)){
                return objAttributes[i].getType();
            }
        }
        throw new ServiceException(ErrorCodes.INVALID_MBEAN_ATTRIBUTE,
                attribute, objectName);
    }


    private List getApplications(String appName){
        ApplicationConfig appConfig =
                ApplicationConfigManager.getApplicationConfigByName(appName);
        List applications = null;
        if(appConfig.isCluster()){
            applications = appConfig.getApplications();
        }else{
            applications = new ArrayList(1);
            applications.add(appConfig);
        }
        return applications;
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
                            CoreUtils.getTypedValue(attrValue, attrType));
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
