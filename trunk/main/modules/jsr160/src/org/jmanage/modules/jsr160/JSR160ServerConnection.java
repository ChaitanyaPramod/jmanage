package org.jmanage.modules.jsr160;

import org.jmanage.core.management.*;
import org.jmanage.core.management.MalformedObjectNameException;
import org.jmanage.core.management.ObjectInstance;
import org.jmanage.core.management.ObjectName;

import javax.management.*;
import java.util.*;
import java.io.IOException;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class JSR160ServerConnection implements
        ServerConnection{

    private final MBeanServerConnection mbeanServer;

    public JSR160ServerConnection(MBeanServerConnection mbeanServer){
        assert mbeanServer != null;
        this.mbeanServer = mbeanServer;
    }

    /**
     * Queries the management objects based on the given object name, containing
     * the search criteria.
     *
     * @param objectName
     * @return
     */
    public Set queryObjects(ObjectName objectName) {
        Set mbeans = null;
        try {
            mbeans = mbeanServer.queryMBeans(
                                    toJMXObjectName(objectName),
                                    null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toJmanageObjectInstance(mbeans);
    }

    /**
     * Invokes the given "operationName" on the object identified by
     * "objectName".
     *
     * @param objectName
     * @param operationName
     * @param params
     * @param signature
     * @return
     */
    public Object invoke(ObjectName objectName,
                         String operationName,
                         Object[] params,
                         String[] signature) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            return mbeanServer.invoke(jmxObjName, operationName, params, signature);
        } catch (Exception e) {
           // TODO: do we need specific exceptions ?
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the information about the given objectName.
     *
     * @param objectName
     * @return
     */
    public ObjectInfo getObjectInfo(ObjectName objectName) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            MBeanInfo mbeanInfo = mbeanServer.getMBeanInfo(jmxObjName);
            return toObjectInfo(mbeanInfo);
        } catch (Exception e){
            // TODO: do we need specific exceptions ?
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a list of ObjectAttribute objects containing attribute names
     * and values for the given attributeNames
     *
     * @param objectName
     * @param attributeNames
     * @return
     */
    public List getAttributes(ObjectName objectName, String[] attributeNames) {

        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            AttributeList attrList =
                    mbeanServer.getAttributes(jmxObjName, attributeNames);
            return toObjectAttributeList(attrList);
        } catch (Exception e) {
            // TODO: do we need specific exceptions ?
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the attribute values.
     *
     * @param objectName
     * @param attributeList list of ObjectAttribute objects
     */
    public List setAttributes(ObjectName objectName, List attributeList) {

        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            AttributeList output =
                    mbeanServer.setAttributes(jmxObjName,
                            toJMXAttributeList(attributeList));
            return toObjectAttributeList(output);
        } catch (Exception e) {
            // TODO: do we need specific exceptions ?
            throw new RuntimeException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utility methods

    private static javax.management.ObjectName
            toJMXObjectName(ObjectName objectName){
        try {
            return new javax.management.ObjectName(objectName.toString());
        } catch (javax.management.MalformedObjectNameException e) {
            throw new MalformedObjectNameException(e);
        }
    }

    private static ObjectName toJmanageObjectName(
            javax.management.ObjectName objectName){
        return new ObjectName(objectName.getCanonicalName());
    }

    private static Set toJmanageObjectInstance(Set mbeans){
        final Set output = new HashSet(mbeans.size());
        for(Iterator it=mbeans.iterator(); it.hasNext();){
            javax.management.ObjectInstance objInstance =
                    (javax.management.ObjectInstance)it.next();
            final ObjectName objName =
                    toJmanageObjectName(objInstance.getObjectName());
            output.add(new ObjectInstance(objName,
                    objInstance.getClassName()));
        }
        return output;
    }

    private static ObjectInfo toObjectInfo(MBeanInfo mbeanInfo){

        ObjectAttributeInfo[] attributes =
                toObjectAttributes(mbeanInfo.getAttributes());
        ObjectConstructorInfo[] constructors =
                toObjectConstructors(mbeanInfo.getConstructors());
        ObjectOperationInfo[] operations =
                toObjectOperations(mbeanInfo.getOperations());
        ObjectNotificationInfo[] notifications =
                toObjectNotifications(mbeanInfo.getNotifications());
        return new ObjectInfo(mbeanInfo.getClassName(),
                mbeanInfo.getDescription(), attributes,
                constructors, operations, notifications);
    }

    private static ObjectAttributeInfo[]
            toObjectAttributes(MBeanAttributeInfo[] attributes){
        ObjectAttributeInfo[] objAttributes =
                new ObjectAttributeInfo[attributes.length];
        for(int i=0; i < attributes.length; i++) {
            objAttributes[i] = toObjectAttributeInfo(attributes[i]);
        }
        return objAttributes;
    }

    private static ObjectAttributeInfo
            toObjectAttributeInfo(MBeanAttributeInfo attribute){

        return new ObjectAttributeInfo(attribute.getName(),
                attribute.getDescription(),
                attribute.getType(),
                attribute.isWritable(),
                attribute.isReadable(),
                attribute.isIs());
    }

    private static ObjectConstructorInfo[]
            toObjectConstructors(MBeanConstructorInfo[] constructors){
        ObjectConstructorInfo[] objCtors =
                new ObjectConstructorInfo[constructors.length];
        for(int i=0; i < constructors.length; i++) {
            objCtors[i] = toObjectConstructorInfo(constructors[i]);
        }
        return objCtors;
    }

    private static ObjectConstructorInfo
            toObjectConstructorInfo(MBeanConstructorInfo constructor){
        return new ObjectConstructorInfo(constructor.getName(),
                constructor.getDescription(),
                toObjectParameters(constructor.getSignature()));
    }

    private static ObjectOperationInfo[]
            toObjectOperations(MBeanOperationInfo[] operations){
        ObjectOperationInfo[] objOperations =
                new ObjectOperationInfo[operations.length];
        for(int i=0; i < operations.length; i++) {
            objOperations[i] = toObjectOperationInfo(operations[i]);
        }
        return objOperations;
    }

    private static ObjectOperationInfo
            toObjectOperationInfo(MBeanOperationInfo operation){
        return new ObjectOperationInfo(operation.getName(),
                operation.getDescription(),
                toObjectParameters(operation.getSignature()),
                operation.getReturnType(),
                operation.getImpact());
    }

    private static ObjectNotificationInfo[]
                toObjectNotifications(MBeanNotificationInfo[] notifications){
        ObjectNotificationInfo[] objNotifications =
                new ObjectNotificationInfo[notifications.length];
        for(int i=0; i < notifications.length; i++) {
            objNotifications[i] = toObjectNotificationInfo(notifications[i]);
        }
        return objNotifications;
    }

    private static ObjectNotificationInfo
            toObjectNotificationInfo(MBeanNotificationInfo notification){
        return new ObjectNotificationInfo(notification.getNotifTypes(),
                notification.getName(),
                notification.getDescription());
    }

    private static ObjectParameterInfo[]
            toObjectParameters(MBeanParameterInfo[] parameters){
        ObjectParameterInfo[] objParameters =
                new ObjectParameterInfo[parameters.length];
        for(int i=0; i < parameters.length; i++) {
            objParameters[i] = toObjectParameterInfo(parameters[i]);
        }
        return objParameters;
    }

    private static ObjectParameterInfo
            toObjectParameterInfo(MBeanParameterInfo parameter){

        return new ObjectParameterInfo(parameter.getName(),
                parameter.getDescription(), parameter.getType());
    }

    private static List toObjectAttributeList(AttributeList attrList){
        final List objAttrList = new ArrayList(attrList.size());
        for(Iterator it=attrList.iterator(); it.hasNext(); ){
            Attribute attr = (Attribute)it.next();
            objAttrList.add(toObjectAttribute(attr));
        }
        return objAttrList;
    }

    private static ObjectAttribute toObjectAttribute(Attribute attr){
        return new ObjectAttribute(attr.getName(), attr.getValue());
    }

    private static AttributeList toJMXAttributeList(List objAttrs){
        AttributeList attrList = new AttributeList(objAttrs.size());
        for(Iterator it=objAttrs.iterator(); it.hasNext(); ){
            attrList.add(toJMXAttribute((ObjectAttribute)it.next()));
        }
        return attrList;
    }

    private static Attribute toJMXAttribute(ObjectAttribute objAttr){
        return new Attribute(objAttr.getName(), objAttr.getValue());
    }
}


