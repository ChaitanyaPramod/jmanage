/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.core.modules.snmp;

import org.jmanage.core.modules.JMXServerConnection;
import org.jmanage.core.management.*;

import java.util.*;
import java.io.IOException;

import snmp.*;

/**
 * @author shashank
 * Date: Jul 31, 2005
 */
public class SNMPAgentConnection extends JMXServerConnection{
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

    public List getAttributes(ObjectName objectName, String[] attributeNames) {
            SNMPVarBindList mibList = getMIBDetails(objectName);
            return getAttributeList(mibList);
    }

    public List setAttributes(ObjectName objectName, List attributeList) {
        return null;
    }

    public void close() throws IOException {
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