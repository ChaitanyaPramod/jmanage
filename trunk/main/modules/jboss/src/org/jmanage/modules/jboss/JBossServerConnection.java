package org.jmanage.modules.jboss;

import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.JMXServerConnection;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;

import javax.management.MBeanInfo;
import javax.management.AttributeList;
import java.util.Set;
import java.util.List;

/**
 *
 * date:  Oct 30, 2004
 * @author	Prem
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


