package org.jmanage.core.management;

import java.util.Set;
import java.util.List;

/**
 * This interface is the abstraction between different MBeanServer
 * implementations and jmanage application. jManage application talks to
 * different MBeanServers via this connection.
 * <p>
 * We may be able to leverage this abstraction to talk to applications
 * which support some other management protocol (like SNMP) than JMX.
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public interface ServerConnection {

    /**
     * Queries the management objects based on the given object name, containing
     * the search criteria.
     *
     * @param objectName
     * @return
     */
    public Set queryObjects(ObjectName objectName);

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
                         String[] signature);

    /**
     * Returns the information about the given objectName.
     *
     * @param objectName
     * @return
     */
    public ObjectInfo getObjectInfo(ObjectName objectName);

    /**
     * Returns a list of ObjectAttribute objects containing attribute names
     * and values for the given attributeNames
     *
     * @param objectName
     * @param attributeNames
     * @return
     */
    public List getAttributes(ObjectName objectName, String[] attributeNames);

    /**
     * Saves the attribute values.
     *
     * @param objectName
     * @param attributeList list of ObjectAttribute objects
     */
    public List setAttributes(ObjectName objectName, List attributeList);
}
