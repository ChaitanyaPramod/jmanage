/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.core.management.metadata;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jmanage.core.management.ObjectAttributeInfo;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectOperationInfo;
import org.jmanage.core.management.ObjectParameterInfo;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

/**
 * 
 * @author Rakesh Kalra
 */
public class Repository {

    private static Logger logger = Loggers.getLogger(Repository.class);
    private static Map<ObjectName, ObjectInfo> mbeanToObjectInfoMap = new HashMap<ObjectName, ObjectInfo>();
    
    public static ObjectInfo applyMetaData(ObjectInfo objInfo, ServerConnection connection){
        ObjectInfo metaObjectInfo = mbeanToObjectInfoMap.get(objInfo.getObjectName());
        if(metaObjectInfo != null){
            objInfo.applyMetaData(metaObjectInfo, 
                    new ExpressionProcessor(connection, objInfo.getObjectName()));
        }
        return objInfo;
    }
    
    static{
        try {
            load();
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void load() throws JDOMException{
        
        String fileName = CoreUtils.getConfigDir() + "/mbeans/PlatformMBeans.xml";
        Document config = new SAXBuilder().build(new File(fileName));
        List mbeans =
            config.getRootElement().getChildren();
        
        for(Iterator it= mbeans.iterator(); it.hasNext();){
            Element mbean = (Element)it.next();
            ObjectInfo objInfo = getObjectInfo(mbean);
            ObjectInfo oldObjInfo = 
                mbeanToObjectInfoMap.put(objInfo.getObjectName(), objInfo);
            if(oldObjInfo != null){
                logger.warning("Duplicate mbean found: " + oldObjInfo.getObjectName().toString());
            }
        }
    }
    
    private static ObjectInfo getObjectInfo(Element mbean){
        ObjectName objectName = new ObjectName(mbean.getAttributeValue("name"));
        String description = mbean.getAttributeValue("description");
        ObjectAttributeInfo[] attributes = getObjectAttributeInfo(mbean);
        ObjectOperationInfo[] operations = getObjectOperationInfo(mbean);
        return new ObjectInfo(objectName, null, description, 
                attributes, null, operations, null);
    }
    
    private static ObjectAttributeInfo[] getObjectAttributeInfo(Element mbean){
        
        List attributes = mbean.getChildren("attribute");
        ObjectAttributeInfo[] attributeInfo = new ObjectAttributeInfo[attributes.size()];
        int index = 0;
        for(Iterator it = attributes.iterator(); it.hasNext(); index++){
            Element attribute = (Element)it.next();
            attributeInfo[index] = new ObjectAttributeInfo(attribute.getAttributeValue("name"),
                                attribute.getAttributeValue("description"),
                                null, // not overridden
                                false,// not overridden
                                false,// not overridden
                                false);// not overridden
        }
        return attributeInfo;
    }
    
    private static ObjectOperationInfo[] getObjectOperationInfo(Element mbean){
        
        List operations = mbean.getChildren("operation");
        ObjectOperationInfo[] operationInfo = new ObjectOperationInfo[operations.size()];
        int index = 0;
        for(Iterator it = operations.iterator(); it.hasNext(); index++){
            Element operation = (Element)it.next();
            operationInfo[index] = new ObjectOperationInfo(operation.getAttributeValue("name"),
                    operation.getAttributeValue("description"),
                    getSignature(operation),
                    null, // not overridden
                    0);   // not overridden
        }
        return operationInfo;
    }
    
    private static ObjectParameterInfo[] getSignature(Element operation){
        List parameters = operation.getChildren("parameter");
        ObjectParameterInfo[] parameterInfo = new ObjectParameterInfo[parameters.size()];
        int index = 0;
        for(Iterator it = parameters.iterator(); it.hasNext(); index++){
            Element parameter = (Element)it.next();
            parameterInfo[index] = new ObjectParameterInfo(parameter.getAttributeValue("name"),
                    parameter.getAttributeValue("description"),
                    parameter.getAttributeValue("type"),
                    parameter.getAttributeValue("legalValues"));
        }
        return parameterInfo;
    }
}
