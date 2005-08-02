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
package org.jmanage.core.modules.jsr160;

import org.jmanage.core.management.*;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.modules.JMXServerConnection;

import javax.management.*;
import javax.management.remote.JMXConnector;
import java.util.*;
import java.io.IOException;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class JSR160ServerConnection extends JMXServerConnection{

    private final JMXConnector jmxc;
    private final MBeanServerConnection mbeanServer;

    public JSR160ServerConnection(JMXConnector jmxc )
        throws IOException {
        assert jmxc != null;
        this.jmxc = jmxc;
        this.mbeanServer = jmxc.getMBeanServerConnection();
    }

    /**
     * Queries the management objects based on the given object name, containing
     * the search criteria.
     *
     * @param objectName
     * @return
     */
    public Set queryNames(ObjectName objectName) {
        Set mbeans = null;
        try {
            mbeans = mbeanServer.queryNames(
                                    toJMXObjectName(objectName),
                                    null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            return toObjectInfo(objectName, mbeanInfo);
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

    public void addNotificationListener(ObjectName objectName,
                                        ObjectNotificationListener listener,
                                        ObjectNotificationFilter filter,
                                        Object handback){

        try {
            mbeanServer.addNotificationListener(toJMXObjectName(objectName),
                    toJMXNotificationListener(listener),
                    toJMXNotificationFilter(filter),
                    handback);
        } catch (InstanceNotFoundException e) {
            // TODO: do we need specific exceptions ?
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO: do we need specific exceptions ?
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the connection to the server
     */
    public void close() throws IOException {
        jmxc.close();
    }
}


