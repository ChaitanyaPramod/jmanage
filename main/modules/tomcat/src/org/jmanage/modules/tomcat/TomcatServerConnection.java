package org.jmanage.modules.tomcat;

import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.JMXServerConnection;

import javax.management.MBeanServer;
import javax.management.MBeanInfo;
import javax.management.AttributeList;
import java.util.Set;
import java.util.List;

/**
 * Date: Aug 31, 2004 10:33:02 PM
 * @author Shashank Bellary 
 */
public class TomcatServerConnection extends JMXServerConnection{

    private final MBeanServer mbeanServer;

    public TomcatServerConnection(MBeanServer mbeanServer) {
        assert mbeanServer != null;
        this.mbeanServer = mbeanServer;
    }

    public Set queryObjects(ObjectName objectName) {
        Set mbeans =
                mbeanServer.queryNames(
                        toJMXObjectName(objectName),
                        null);
        return toJmanageObjectNameInstance(mbeans);
    }

    public Object invoke(ObjectName objectName,
                         String operationName,
                         Object[] params,
                         String[] signature) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            return mbeanServer.invoke(jmxObjName, operationName, params, signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectInfo getObjectInfo(ObjectName objectName) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            MBeanInfo mbeanInfo = mbeanServer.getMBeanInfo(jmxObjName);
            return toObjectInfo(mbeanInfo);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List getAttributes(ObjectName objectName, String[] attributeNames) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            AttributeList attrList =
                    mbeanServer.getAttributes(jmxObjName, attributeNames);
            return toObjectAttributeList(attrList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List setAttributes(ObjectName objectName, List attributeList) {
        try {
            javax.management.ObjectName jmxObjName = toJMXObjectName(objectName);
            AttributeList output =
                    mbeanServer.setAttributes(jmxObjName,
                            toJMXAttributeList(attributeList));
            return toObjectAttributeList(output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
