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
package org.jmanage.core.modules;

import org.jmanage.core.management.*;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.MalformedObjectNameException;

import javax.management.*;
import java.util.*;

/**
 * Date: Sep 3, 2004 11:19:06 PM
 * @author Shashank Bellary 
 */
public abstract class JMXServerConnection implements ServerConnection{

    // todo: remove after all sub classes start supporting notifications
    public void addNotificationListener(ObjectName objectName,
                                        ObjectNotificationListener listener,
                                        ObjectNotificationFilter filter,
                                        Object handback){
        throw new RuntimeException("Notifications not supported");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utility methods

    protected static javax.management.ObjectName
            toJMXObjectName(ObjectName objectName){
        try {
            return new javax.management.ObjectName(objectName.toString());
        } catch (javax.management.MalformedObjectNameException e) {
            throw new MalformedObjectNameException(e);
        }
    }

    protected static ObjectName toJmanageObjectName(
            javax.management.ObjectName objectName){
        return new ObjectName(objectName.getCanonicalName());
    }

    /**
     * Converts a Set of javax.management.ObjectName to
     * org.jmanage.core.management.ObjectName
     */
    protected static Set toJmanageObjectNameInstance(Set mbeans){
        final Set output = new HashSet(mbeans.size());
        for(Iterator it=mbeans.iterator(); it.hasNext();){
            javax.management.ObjectName objName =
                    (javax.management.ObjectName)it.next();
            output.add(toJmanageObjectName(objName));
        }
        return output;
    }

    protected static ObjectInfo toObjectInfo(ObjectName objectName,
                                             MBeanInfo mbeanInfo){

        ObjectAttributeInfo[] attributes =
                toObjectAttributes(mbeanInfo.getAttributes());
        ObjectConstructorInfo[] constructors =
                toObjectConstructors(mbeanInfo.getConstructors());
        ObjectOperationInfo[] operations =
                toObjectOperations(mbeanInfo.getOperations());
        ObjectNotificationInfo[] notifications =
                toObjectNotifications(mbeanInfo.getNotifications());
        return new ObjectInfo(objectName, mbeanInfo.getClassName(),
                mbeanInfo.getDescription(), attributes,
                constructors, operations, notifications);
    }

    protected static ObjectAttributeInfo[]
            toObjectAttributes(MBeanAttributeInfo[] attributes){
        ObjectAttributeInfo[] objAttributes =
                new ObjectAttributeInfo[attributes.length];
        for(int i=0; i < attributes.length; i++) {
            objAttributes[i] = toObjectAttributeInfo(attributes[i]);
        }
        return objAttributes;
    }

    protected static ObjectAttributeInfo
            toObjectAttributeInfo(MBeanAttributeInfo attribute){

        return new ObjectAttributeInfo(attribute.getName(),
                attribute.getDescription(),
                attribute.getType(),
                attribute.isWritable(),
                attribute.isReadable(),
                attribute.isIs());
    }

    protected static ObjectConstructorInfo[]
            toObjectConstructors(MBeanConstructorInfo[] constructors){
        ObjectConstructorInfo[] objCtors =
                new ObjectConstructorInfo[constructors.length];
        for(int i=0; i < constructors.length; i++) {
            objCtors[i] = toObjectConstructorInfo(constructors[i]);
        }
        return objCtors;
    }

    protected static ObjectConstructorInfo
            toObjectConstructorInfo(MBeanConstructorInfo constructor){
        return new ObjectConstructorInfo(constructor.getName(),
                constructor.getDescription(),
                toObjectParameters(constructor.getSignature()));
    }

    protected static ObjectOperationInfo[]
            toObjectOperations(MBeanOperationInfo[] operations){
        ObjectOperationInfo[] objOperations =
                new ObjectOperationInfo[operations.length];
        for(int i=0; i < operations.length; i++) {
            objOperations[i] = toObjectOperationInfo(operations[i]);
        }
        return objOperations;
    }

    protected static ObjectOperationInfo
            toObjectOperationInfo(MBeanOperationInfo operation){
        return new ObjectOperationInfo(operation.getName(),
                operation.getDescription(),
                toObjectParameters(operation.getSignature()),
                operation.getReturnType(),
                operation.getImpact());
    }

    protected static ObjectNotificationInfo[]
                toObjectNotifications(MBeanNotificationInfo[] notifications){
        ObjectNotificationInfo[] objNotifications =
                new ObjectNotificationInfo[notifications.length];
        for(int i=0; i < notifications.length; i++) {
            objNotifications[i] = toObjectNotificationInfo(notifications[i]);
        }
        return objNotifications;
    }

    protected static ObjectNotificationInfo
            toObjectNotificationInfo(MBeanNotificationInfo notification){
        return new ObjectNotificationInfo(notification.getNotifTypes(),
                notification.getName(),
                notification.getDescription());
    }

    protected static ObjectParameterInfo[]
            toObjectParameters(MBeanParameterInfo[] parameters){
        ObjectParameterInfo[] objParameters =
                new ObjectParameterInfo[parameters.length];
        for(int i=0; i < parameters.length; i++) {
            objParameters[i] = toObjectParameterInfo(parameters[i]);
        }
        return objParameters;
    }

    protected static ObjectParameterInfo
            toObjectParameterInfo(MBeanParameterInfo parameter){

        return new ObjectParameterInfo(parameter.getName(),
                parameter.getDescription(), parameter.getType());
    }

    protected static List toObjectAttributeList(AttributeList attrList){
        final List objAttrList = new ArrayList(attrList.size());
        for(Iterator it=attrList.iterator(); it.hasNext(); ){
            Attribute attr = (Attribute)it.next();
            objAttrList.add(toObjectAttribute(attr));
        }
        return objAttrList;
    }

    protected static ObjectAttribute toObjectAttribute(Attribute attr){
        return new ObjectAttribute(attr.getName(), attr.getValue());
    }

    protected static AttributeList toJMXAttributeList(List objAttrs){
        AttributeList attrList = new AttributeList(objAttrs.size());
        for(Iterator it=objAttrs.iterator(); it.hasNext(); ){
            attrList.add(toJMXAttribute((ObjectAttribute)it.next()));
        }
        return attrList;
    }

    protected static Attribute toJMXAttribute(ObjectAttribute objAttr){
        return new Attribute(objAttr.getName(), objAttr.getValue());
    }

    protected static ObjectNotification toObjectNotification(
            Notification n){

        return new ObjectNotification(n.getType(), n.getSource(),
                n.getSequenceNumber(), n.getTimeStamp(),
                n.getMessage(), n.getUserData());
    }

    protected static NotificationListener toJMXNotificationListener(
            final ObjectNotificationListener listener){
        return new NotificationListener(){
            public void handleNotification(Notification notification,
                                           Object handback) {
                listener.handleNotification(toObjectNotification(notification),
                        handback);
            }
        };
    }

    protected static NotificationFilter toJMXNotificationFilter(
            final ObjectNotificationFilter filter){
        NotificationFilterSupport notificationFilter =
                new NotificationFilterSupport();
        for(Iterator it=filter.getEnabledTypes().iterator(); it.hasNext();){
            notificationFilter.enableType((String)it.next());
        }
        return notificationFilter;
    }
}
