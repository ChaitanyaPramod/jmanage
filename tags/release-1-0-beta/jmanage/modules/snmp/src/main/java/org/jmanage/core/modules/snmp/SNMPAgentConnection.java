/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.core.modules.snmp;

import org.jmanage.core.management.*;

import java.util.*;
import java.io.IOException;

import snmp.*;

/**
 * @author shashank
 * @author Rakesh Kalra
 * Date: Jul 31, 2005
 */
public class SNMPAgentConnection implements ServerConnection{

    private final SNMPv1CommunicationInterface comInterface;
    private final String mBeanObjectName = "snmp:name=SNMPAgent";

    public SNMPAgentConnection(SNMPv1CommunicationInterface comInterface) {
        assert comInterface != null;
        this.comInterface = comInterface;
    }

    public Set queryNames(ObjectName objectName) {
        // This call is to make sure that SNMP agent is accessible
        getMIBDetails(objectName);
        Set mBeanSet = new HashSet();
        mBeanSet.add(new ObjectName(mBeanObjectName));
        return mBeanSet;
    }

    public Object invoke(ObjectName objectName,
                         String operationName,
                         Object[] params,
                         String[] signature) {
        return null;
    }

    public ObjectInfo getObjectInfo(ObjectName objectName) {
        SNMPVarBindList mibList = getMIBDetails(objectName);
        ObjectInfo objectInfo = mibListToJMXMBean(mibList);
        return objectInfo;
    }

    /**
     * Gets the value for a single attribute.
     *
     * todo: optimize
     *
     * @param objectName
     * @param attributeName
     * @return attribute value
     */
    public Object getAttribute(ObjectName objectName, String attributeName) {
        List attrList = getAttributes(objectName, new String[]{attributeName});
        if(attrList.size() > 0){
            ObjectAttribute objAttr = (ObjectAttribute)attrList.get(0);
            return objAttr.getValue();
        }
        return null; // todo: null is probably not the right value here
    }

    public List getAttributes(ObjectName objectName, String[] attributeNames) {
        SNMPVarBindList mibList = getMIBDetails(objectName);
        return getAttributeList(mibList);
    }

    public List setAttributes(ObjectName objectName, List attributeList) {
        return null;
    }

    public void addNotificationListener(ObjectName objectName,
                                        ObjectNotificationListener listener,
                                        ObjectNotificationFilter filter,
                                        Object handback){
        throw new RuntimeException("Notifications not supported");
    }

    public void removeNotificationListener(ObjectName objectName,
                                           ObjectNotificationListener listener,
                                           ObjectNotificationFilter filter,
                                           Object handback){
        throw new RuntimeException("Notifications not supported");
    }

    public void createMBean(String className,
                            ObjectName name,
                            Object[] params,
                            String[] signature){
        throw new RuntimeException("createMBean not supported");
    }

    public void unregisterMBean(ObjectName objectName){
        throw new RuntimeException("unregisterMBean not supported");
    }

    public void close() throws IOException {
        comInterface.closeConnection();
    }

    public Object buildObjectName(String objectName){
        try {
            return new javax.management.ObjectName(objectName);
        } catch (javax.management.MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * checks if this connection is open
     * @return true if this connection is open
     */
    public boolean isOpen() {
        // todo: requires proper implementation before notifications
        //   are enabled
        return true;
    }

    /**
     * Builds list of SNMP attributes available as MIB info into a JMX object
     * attributes.
     *
     * @param mibList
     * @return
     */
    private ObjectInfo mibListToJMXMBean(SNMPVarBindList mibList){
        final int mibVarCount = mibList.size();
        ObjectAttributeInfo[] objectAttributes = new ObjectAttributeInfo[mibVarCount];
        ObjectConstructorInfo[] objectConstructorInfo = new ObjectConstructorInfo[0];
        ObjectOperationInfo[] objectOperationInfo = new ObjectOperationInfo[0];
        ObjectNotificationInfo[] objectNotificationInfo = new ObjectNotificationInfo[0];

        for(int index=0; index<mibVarCount; index++){
            SNMPSequence pair = (SNMPSequence)mibList.getSNMPObjectAt(index);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)pair.getSNMPObjectAt(0);
            SNMPObject snmpValue = pair.getSNMPObjectAt(1);
            ObjectAttributeInfo objectAttributeInfo =
                    new ObjectAttributeInfo(snmpOID.toString(),
                            "SNMP Object ID",
                            getAttributeType(snmpValue), false, true, false);
            objectAttributes[index] = objectAttributeInfo;
        }

        ObjectInfo objectInfo = new ObjectInfo(new ObjectName(mBeanObjectName),
                "SNMPAgent", "SNMPAgent", objectAttributes, objectConstructorInfo,
                objectOperationInfo, objectNotificationInfo);
        return objectInfo;
    }

    /**
     * TODO Add support for all TYPES
     * @param snmpValue
     * @return
     */
    private String getAttributeType(SNMPObject snmpValue){
        final String attributeType = snmpValue.getClass().getName();
        if(snmp.SNMPOctetString.class.getName().equals(attributeType)){
            return "String";
        }else if(snmp.SNMPInteger.class.getName().equals(attributeType)){
            return "int";
        }else if(snmp.SNMPNull.class.getName().equals(attributeType)){
            return "null";
        }else{
            throw new RuntimeException("Unsupported data type");
        }
    }

    /**
     *
     * @param mibList
     * @return
     */
    private List getAttributeList(SNMPVarBindList mibList){
        final int mibVarCount = mibList.size();
        List attributeList = new ArrayList();
        for(int index=0; index<mibVarCount; index++){
            SNMPSequence pair = (SNMPSequence)mibList.getSNMPObjectAt(index);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)pair.getSNMPObjectAt(0);
            SNMPObject snmpValue = pair.getSNMPObjectAt(1);
            attributeList.add(new ObjectAttribute(snmpOID.toString(),
                    snmpValue.toString()));
        }
        return attributeList;
    }

    /**
     *
     * @param objectName
     * @return
     */
    private SNMPVarBindList getMIBDetails(ObjectName objectName){
        try{
            return comInterface.retrieveAllMIBInfo("");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}