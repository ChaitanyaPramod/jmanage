package org.jmanage.modules.weblogic;

import org.jmanage.core.management.*;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.modules.JMXServerConnection;

import javax.management.*;
import java.util.*;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class WLServerConnection extends JMXServerConnection{

    private final MBeanServer mbeanServer;

    public WLServerConnection(MBeanServer mbeanServer){
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
        Set mbeans =
                mbeanServer.queryNames(
                        toJMXObjectName(objectName),
                        null);
        return toJmanageObjectNameInstance(mbeans);
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
}