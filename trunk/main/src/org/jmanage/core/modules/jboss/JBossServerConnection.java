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
package org.jmanage.core.modules.jboss;

import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.modules.JMXServerConnection;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;

import javax.management.MBeanInfo;
import javax.management.AttributeList;
import java.util.Set;
import java.util.List;

/**
 *
 * date:  Oct 30, 2004
 * @author	Prem, Shashank Bellary
 */
public class JBossServerConnection extends JMXServerConnection {

    private final RMIAdaptor rmiAdaptor;

    public JBossServerConnection(RMIAdaptor rmiAdaptor) {
        assert rmiAdaptor != null;
        this.rmiAdaptor = rmiAdaptor;
    }

    public Set queryObjects(ObjectName objectName) {
        Set mbeans = null;

        try {
            mbeans = rmiAdaptor.queryNames(toJMXObjectName(objectName), null);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return toJmanageObjectNameInstance(mbeans);
    }

    public Object invoke(ObjectName objectName,
                         String operationName,
                         Object[] params,
                         String[] signature) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            return rmiAdaptor.invoke(jmxObjName, operationName, params, signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectInfo getObjectInfo(ObjectName objectName) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            MBeanInfo mbeanInfo = rmiAdaptor.getMBeanInfo(jmxObjName);
            return toObjectInfo(mbeanInfo);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List getAttributes(ObjectName objectName, String[] attributeNames) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            AttributeList attrList =
                    rmiAdaptor.getAttributes(jmxObjName, attributeNames);
            return toObjectAttributeList(attrList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List setAttributes(ObjectName objectName, List attributeList) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            AttributeList output =
                    rmiAdaptor.setAttributes(jmxObjName,
                            toJMXAttributeList(attributeList));
            return toObjectAttributeList(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


