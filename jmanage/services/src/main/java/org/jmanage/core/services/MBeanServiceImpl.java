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

import org.apache.commons.beanutils.ConvertUtils;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.MBeanConfig;
import org.jmanage.core.data.AttributeListData;
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.data.OperationResultData;
import org.jmanage.core.management.*;
import org.jmanage.core.util.*;
import org.jmanage.util.StringUtils;

import java.lang.reflect.Constructor;
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

    private static final ObjectName DEFAULT_FILTER_OBJECT_NAME =
            new ObjectName(DEFAULT_FILTER);

    public List queryMBeans(ServiceContext context,
                          String filter)
            throws ServiceException {

        ServerConnection serverConnection =
                context.getServerConnection();

        ObjectName filterObjectName = null;
        if(filter == null){
            filterObjectName = DEFAULT_FILTER_OBJECT_NAME;
        }else{
            filterObjectName = new ObjectName(filter);
        }

        Set mbeans =
                serverConnection.queryNames(filterObjectName);
        ArrayList mbeanDataList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator();it.hasNext(); ){
            ObjectName objName = (ObjectName)it.next();
            mbeanDataList.add(new MBeanData(objName.getCanonicalName()));
        }
        return mbeanDataList;
    }

    public Map queryMBeansOutputMap(ServiceContext context, String filter,
                                    String[] dataTypes){
        List mbeanDataList = queryMBeansWithAttributes(context,filter,dataTypes);

        Map domainToObjectNameListMap = new TreeMap();
        ObjectNameTuple tuple = new ObjectNameTuple();
        for(Iterator it=mbeanDataList.iterator(); it.hasNext();){
            MBeanData mbeanData = (MBeanData)it.next();
            tuple.setObjectName(mbeanData.getName());
            String domain = tuple.getDomain();
            String name = tuple.getName();
            Set objectNameList = (Set)domainToObjectNameListMap.get(domain);
            if(objectNameList == null){
                objectNameList = new TreeSet();
                domainToObjectNameListMap.put(domain, objectNameList);
            }
            objectNameList.add(name);
        }
        return domainToObjectNameListMap;
    }

     private List queryMBeansWithAttributes(ServiceContext context, String filter,
                                         String[] dataTypes)
            throws ServiceException{
        ServerConnection serverConnection = context.getServerConnection();
        List mbeans = queryMBeans(context, filter);
        List mbeanToAttributesList = new ArrayList();
        for(Iterator itr=mbeans.iterator(); itr.hasNext();){
            MBeanData mbeanData = (MBeanData)itr.next();
            ObjectName objName = new ObjectName(mbeanData.getName());
            try {
                ObjectInfo objInfo = serverConnection.getObjectInfo(objName);
                ObjectAttributeInfo[] objAttrInfo = objInfo.getAttributes();
                if(objAttrInfo!=null && objAttrInfo.length > 0){
                    if(dataTypes!=null && dataTypes.length > 0){
                        if(checkAttributeDataType(objAttrInfo, dataTypes,
                                context.getApplicationConfig(), null)){
                            mbeanToAttributesList.add(mbeanData);
                        }
                    }else{
                        mbeanToAttributesList.add(mbeanData);
                    }
                }
            } catch (Exception e) {
                /* if there is an error while getting MBean Info, continue
                    looking further */
                String errorMessage = "Error getting ObjectInfo for: " +
                        objName + ", error=" + e.getMessage();
                logger.log(Level.WARNING, errorMessage);
                logger.log(Level.FINE, errorMessage, e);
            }
        }
         return mbeanToAttributesList;
    }

    /**
     *
     * @param objAttrInfo
     * @param dataTypes
     * @param appConfig
     * @param attributesList (optional) if specified, it will be populated
     *              with ObjectAttributeInfo objects that match the dataTypes
     *              specified
     * @return
     */
    private boolean checkAttributeDataType(ObjectAttributeInfo[] objAttrInfo,
                                           String[] dataTypes,
                                           ApplicationConfig appConfig,
                                           List attributesList){

        boolean result = false;
        outerloop:
        for(int i=0; i<objAttrInfo.length;i++){
            ObjectAttributeInfo attrInfo = objAttrInfo[i];
            for(int j=0; j<dataTypes.length; j++){
                Class attrInfoType = getClass(attrInfo.getType(),
                        appConfig.getApplicationClassLoader());
                Class dataType = getClass(dataTypes[j],
                        this.getClass().getClassLoader());
                if(attrInfoType != null &&
                        dataType.isAssignableFrom(attrInfoType)){
                    result = true;
                    if(attributesList != null){
                        attributesList.add(attrInfo);
                    }else{
                        break outerloop;
                    }
                }
            }
        }
        return result;
    }

    private Class getClass(String type, ClassLoader classLoader){
        if(type.equals("boolean"))
             return Boolean.class;
        if(type.equals("byte"))
             return Byte.TYPE;
        if(type.equals("char"))
             return Character.class;
        if(type.equals("double"))
             return Double.class;
        if(type.equals("float"))
             return Float.class;
        if(type.equals("int"))
             return Integer.class;
        if(type.equals("long"))
             return Long.class;
        if(type.equals("short"))
             return Short.class;
        if(type.equals("void"))
             return Void.class;
        Class clazz = null;
        try{
            clazz = Class.forName(type, true, classLoader);

        }catch(ClassNotFoundException e){
            logger.fine("Error finding class of type=" + type +
                    ", error=" + e.getMessage());
        }
        return clazz;
    }

    private static class ObjectNameTuple{
        String domain;
        String name;

        void setObjectName(String canonicalName){
            int index = canonicalName.indexOf(":");
            domain = canonicalName.substring(0, index);
            name = canonicalName.substring(index + 1);
        }

        String getName(){
            return name;
        }
        String getDomain(){
            return domain;
        }
    }

    public ObjectInfo getMBeanInfo(ServiceContext context)
            throws ServiceException {
        canAccessThisMBean(context);
        ServerConnection serverConnection = context.getServerConnection();
        ObjectInfo objectInfo =
                serverConnection.getObjectInfo(context.getObjectName());
        return objectInfo;
    }

    /**
     * @return list of all attribute values
     */
    public AttributeListData[] getAttributes(ServiceContext context)
            throws ServiceException {
        canAccessThisMBean(context);
        ServerConnection serverConnection = null;
        try {
            serverConnection =
                    ServiceUtils.getServerConnectionEvenIfCluster(
                            context.getApplicationConfig());
            ObjectInfo objInfo =
                    serverConnection.getObjectInfo(context.getObjectName());
            assert objInfo != null;
            ObjectAttributeInfo[] attributes = objInfo.getAttributes();
            List attributeNames = new LinkedList();
            for (int i = 0; i < attributes.length; i++) {
                if(attributes[i].isReadable()){
                    attributeNames.add(attributes[i].getName());
                }
            }
            String[] attributeArray = StringUtils.listToStringArray(attributeNames);
            return getAttributes(context, attributeArray, true);
        } finally {
            ServiceUtils.close(serverConnection);
        }
    }

    /**
     *
     * @return list of attribute values for given attributes
     */
    public AttributeListData[] getAttributes(ServiceContext context,
                                             String[] attributes,
                                             boolean handleCluster)
            throws ServiceException {
        canAccessThisMBean(context);
        ApplicationConfig appConfig = context.getApplicationConfig();
        ObjectName objectName = context.getObjectName();

        AttributeListData[] resultData = null;
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
                try {
                    resultData[index] =
                            getAttributes(context,
                                    childAppConfig, objectName, attributes);
                } catch (ConnectionFailedException e) {
                    resultData[index] =
                            new AttributeListData(childAppConfig.getName());
                }
            }
        }else{
            resultData = new AttributeListData[1];
            resultData[0] =
                    getAttributes(context, appConfig, objectName, attributes);
        }
        return resultData;
    }

    public ObjectAttribute getObjectAttribute(ServiceContext context,
                                              String attribute)
            throws ServiceException{
        assert context.getObjectName() != null;
        assert context.getApplicationConfig() != null;
        canAccessThisMBean(context);
        AccessController.checkAccess(context,
                ACLConstants.ACL_VIEW_MBEAN_ATTRIBUTES,
                attribute);
        // we don't support clustering here
        assert !context.getApplicationConfig().isCluster();

        ServerConnection connection =
                        context.getServerConnection();
        List attrList =
                connection.getAttributes(context.getObjectName(),
                        new String[]{attribute});
        return (ObjectAttribute)attrList.get(0);
    }

    /**
     * @return list of attribute values for given attributes
     */
    private AttributeListData getAttributes(ServiceContext context,
                                            ApplicationConfig appConfig,
                                            ObjectName objectName,
                                            String[] attributes)
            throws ConnectionFailedException {

        // TODO: we should hide the attributes in the jsp
        for(int attrCount = 0; attrCount < attributes.length; attrCount++){
            AccessController.checkAccess(context,
                    ACLConstants.ACL_VIEW_MBEAN_ATTRIBUTES,
                    attributes[attrCount]);
        }
        ServerConnection connection = null;

        try {
            connection = ServerConnector.getServerConnection(appConfig);
            List attrList =
                    connection.getAttributes(objectName, attributes);
            return new AttributeListData(appConfig.getName(), attrList);
        } finally {
            ServiceUtils.close(connection);
        }
    }
    public List filterAttributes(ServiceContext context, ObjectAttributeInfo[] objAttrInfo, String[] dataTypes){
        List objAttrInfoList = new LinkedList();
        checkAttributeDataType(objAttrInfo, dataTypes,
                context.getApplicationConfig() ,objAttrInfoList);
         return objAttrInfoList;
    }

    public OperationResultData[] invoke(ServiceContext context,
                                        String operationName,
                                        String[] params)
            throws ServiceException {
        canAccessThisMBean(context);
        AccessController.checkAccess(context,
                ACLConstants.ACL_EXECUTE_MBEAN_OPERATIONS, operationName);

        /* try to determine the method, based on params */
        ObjectOperationInfo operationInfo =
                findOperation(context, operationName,
                        params != null ? params.length : 0);
        return invoke(context, operationName, params,
                operationInfo.getParameters());
    }

    /**
     * Invokes MBean operation
     * @return
     * @throws ServiceException
     */
    public OperationResultData[] invoke(ServiceContext context,
                                        String operationName,
                                        String[] params,
                                        String[] signature)
            throws ServiceException {
        canAccessThisMBean(context);
        AccessController.checkAccess(context,
                ACLConstants.ACL_EXECUTE_MBEAN_OPERATIONS, operationName);

        ApplicationConfig appConfig = context.getApplicationConfig();
        ObjectName objectName = context.getObjectName();

        OperationResultData[] resultData = null;
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
        ServerConnection serverConnection = null;
        try {
            serverConnection = ServerConnector.getServerConnection(appConfig);
            Object[] typedParams = getTypedArray(appConfig,
                    params, signature);
            final Object result = serverConnection.invoke(objectName, operationName,
                            typedParams, signature);

            resultData.setOutput(result);
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
        } finally {
            ServiceUtils.close(serverConnection);
        }
        return resultData;
    }

    private ObjectOperationInfo findOperation(ServiceContext context,
                                              String operationName,
                                              int paramCount){

        ObjectName objectName = context.getObjectName();
        ServerConnection connection = null;
        try {
            connection =
                ServiceUtils.getServerConnectionEvenIfCluster(
                        context.getApplicationConfig());
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
        } finally {
            ServiceUtils.close(connection);
        }
    }

    //TODO: should we first check that all apps in a cluster are up,
    // before updating?  - rk
    public AttributeListData[] setAttributes(ServiceContext context,
                                             String[][] attributes)
            throws ServiceException{
        canAccessThisMBean(context);
        List applications = getApplications(context.getApplicationConfig());
        ObjectName objectName = context.getObjectName();
        List attributeList = buildAttributeList(context, attributes);
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
     * <p>
     * The attributes element contains the keys in the format:
     * attr+<applicationId>+<attrName>+<attrType>
     * <p>
     * todo: improve this interface (currently written for webui)
     *
     *
     * @param context
     * @param attributes Map containing
     * @throws ServiceException
     */
    public AttributeListData[] setAttributes(ServiceContext context,
                                             Map attributes)
            throws ServiceException{
        canAccessThisMBean(context);
        List applications = getApplications(context.getApplicationConfig());
        ObjectName objectName = context.getObjectName();
        AttributeListData[] attrListData =
                new AttributeListData[applications.size()];
        int index = 0;
        for(Iterator it=applications.iterator(); it.hasNext(); index++){
            final ApplicationConfig childAppConfig =
                        (ApplicationConfig)it.next();
            List attributeList = buildAttributeList(attributes,
                        childAppConfig);
            attrListData[index] = updateAttributes(context, childAppConfig,
                    objectName, attributeList);
        }
        return attrListData;
    }

    public Map queryMBeansWithNotifications(ServiceContext context)
            throws ServiceException {

        ServerConnection serverConnection =
                context.getServerConnection();
        Set mbeans = serverConnection.queryNames(DEFAULT_FILTER_OBJECT_NAME);
        Map mbeanToNoficationsMap = new TreeMap();
        for(Iterator it=mbeans.iterator(); it.hasNext(); ){
            ObjectName objName = (ObjectName)it.next();

            try {
                ObjectInfo objInfo = serverConnection.getObjectInfo(objName);
                ObjectNotificationInfo[] notifications = objInfo.getNotifications();
                if(notifications != null && notifications.length > 0){
                    mbeanToNoficationsMap.put(objName.getCanonicalName(), notifications);
                }
            } catch (Exception e) {
                /* if there is an error while getting MBean Info, continue
                    looking further */
                String errorMessage = "Error getting ObjectInfo for: " +
                        objName + ", error=" + e.getMessage();
                logger.log(Level.WARNING, errorMessage);
                logger.log(Level.FINE, errorMessage, e);
            }
        }
        return mbeanToNoficationsMap;
    }
    private AttributeListData updateAttributes(ServiceContext context,
                                               ApplicationConfig appConfig,
                                               ObjectName objectName,
                                               List attributeList){
        for(Iterator attrIterator = attributeList.iterator(); attrIterator.hasNext();){
            ObjectAttribute objAttr = (ObjectAttribute)attrIterator.next();
            AccessController.checkAccess(context,
                    ACLConstants.ACL_UPDATE_MBEAN_ATTRIBUTES,
                    objAttr.getName());
        }
        AttributeListData attrListData = null;
        ServerConnection serverConnection = null;
        try{
            serverConnection = ServerConnector.getServerConnection(appConfig);
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
        }finally{
            ServiceUtils.close(serverConnection);
        }
        return attrListData;
    }

    /**
     * Converts a two dimentional String array containing attribute name and
     * value to a list of ObjectAttribute objects.
     *
     * @return list containing ObjectAttribute objects
     */
    private List buildAttributeList(ServiceContext context,
                                    String[][] attributes){
        ObjectName objectName;
        ObjectInfo objInfo;
        ServerConnection connection = null;
        try {
            connection =
                    ServiceUtils.getServerConnectionEvenIfCluster(
                            context.getApplicationConfig());
            objectName = context.getObjectName();
            objInfo = connection.getObjectInfo(objectName);
        } finally {
            ServiceUtils.close(connection);
        }

        ObjectAttributeInfo[] objAttributes = objInfo.getAttributes();
        List attributeList = new LinkedList();
        for(int i=0; i<attributes.length; i++){
            String attribute = attributes[i][0];
            String type = getAttributeType(objAttributes, attribute, objectName);
            /* ensure that this attribute is writable */
            ensureAttributeIsWritable(objAttributes, attribute, objectName);

            Object value = getTypedValue(
                    context.getApplicationConfig(), attributes[i][1], type);
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

    private void ensureAttributeIsWritable(ObjectAttributeInfo[] objAttributes,
                                           String attribute,
                                           ObjectName objectName){
        ObjectAttributeInfo attributeInfo = null;
        for(int i=0; i<objAttributes.length; i++){
            if(objAttributes[i].getName().equals(attribute)){
                attributeInfo = objAttributes[i];
                break;
            }
        }
        assert attributeInfo != null :"attribute not found:" + attribute;
        if(!attributeInfo.isWritable()){
            throw new ServiceException(ErrorCodes.READ_ONLY_MBEAN_ATTRIBUTE,
                    attribute, objectName);
        }
    }


    private List getApplications(ApplicationConfig appConfig){
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
     * Map keys are of the format:
     * attr+<applicationId>+<attrName>+<attrType>
     *
     */
    private List buildAttributeList(Map attributes,
                                    ApplicationConfig appConfig){

        String applicationId = appConfig.getApplicationId();
        Iterator it = attributes.keySet().iterator();
        List attributeList = new LinkedList();
        while(it.hasNext()){
            String param = (String)it.next();
            // look for keys which only start with "attr+"
            if(param.startsWith("attr+")){
                StringTokenizer tokenizer = new StringTokenizer(param, "+");
                if(tokenizer.countTokens() < 4){
                    throw new RuntimeException("Invalid param name: " + param);
                }
                tokenizer.nextToken(); // equals to "attr"
                if(applicationId.equals(tokenizer.nextToken())){ // applicationId
                    String attrName = tokenizer.nextToken();
                    String attrType = tokenizer.nextToken();
                    String[] attrValues = (String[])attributes.get(param);
                    // todo: we currently don't support writtable arrays
                    assert attrValues.length == 1;
                    String attrValue = attrValues[0];
                    ObjectAttribute attribute = new ObjectAttribute(attrName,
                            getTypedValue(appConfig, attrValue,
                                    attrType));
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


    private void canAccessThisMBean(ServiceContext context){
        final ApplicationConfig config = context.getApplicationConfig();
        final MBeanConfig configuredMBean =
                config.findMBeanByObjectName(context.getObjectName().getCanonicalName());
        AccessController.checkAccess(context,
                ACLConstants.ACL_VIEW_APPLICATIONS);
        if(configuredMBean != null)
            AccessController.checkAccess(context,
                    ACLConstants.ACL_VIEW_MBEANS);
    }

    public static Object getTypedValue(ApplicationConfig appConfig,
                                       String value,
                                       String type){

        if(type.equals("int")){
            type = "java.lang.Integer";
        }else if(type.equals("long")){
            type = "java.lang.Long";
        }else if(type.equals("short")){
            type = "java.lang.Short";
        }else if(type.equals("float")){
            type = "java.lang.Float";
        }else if(type.equals("double")){
            type = "java.lang.Double";
        }else if(type.equals("char")){
            type = "java.lang.Character";
        }else if(type.equals("boolean")){
            type = "java.lang.Boolean";
        }else if(type.equals("byte")){
            type = "java.lang.Byte";
        }

        try {
            /* handle ObjectName as a special type */
            if(type.equals("javax.management.ObjectName")){
                Class clazz = Class.forName(type, true,
                        appConfig.getApplicationClassLoader());
                try {
                    Constructor ctor = clazz.getConstructor(new Class[]{String.class});
                    return ctor.newInstance(new Object[]{value});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            /* other types */
            return ConvertUtils.convert(value, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[] getTypedArray(ApplicationConfig appConfig,
                                         String[] values,
                                         String[] type){
        Object[] obj = new Object[values.length];
        for(int i=0; i<values.length; i++){
            obj[i] = getTypedValue(appConfig, values[i], type[i]);
        }
        return obj;
    }

    public String getAttributeDataType(ServiceContext context,
                                              String attributeName,
                                              String objectName){
        ServerConnection connection = context.getServerConnection();
        ObjectName objName = new ObjectName(objectName);
        ObjectInfo objectInfo = connection.getObjectInfo(objName);
        ObjectAttributeInfo[] objAttrInfo = objectInfo.getAttributes();
        return getAttributeType(objAttrInfo, attributeName, objName);
    }
}
