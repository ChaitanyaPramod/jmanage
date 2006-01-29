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
package org.jmanage.webui.util;

import org.jmanage.core.management.ObjectAttribute;
import org.jmanage.core.management.ObjectOperationInfo;
import org.jmanage.core.management.ObjectAttributeInfo;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Arrays;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.reflect.Array;

/**
 * Contains some mbean utility methods, which are used in the web layer
 *
 * date:  Oct 15, 2004
 * @author	Rakesh Kalra
 */
public class MBeanUtils {

    public static String jsEscape(String str){
        StringBuffer buff = new StringBuffer(str.length());
        for(int i=0; i<str.length(); i++){
            final char ch = str.charAt(i);
            if(ch == '"'){
                buff.append("&quot;");
            }else if(ch == '\''){
                buff.append("\\");
                buff.append(ch);
            }else{
                buff.append(ch);
            }
        }
        return buff.toString();
    }

    // TODO: value should be converted to actual data type in the proper
    //      classloader: e.g. in the case of javax.managed.ObjectName
    public static ObjectAttribute getObjectAttribute(
            List attributeList,
            ObjectAttributeInfo attrInfo){

        String attrName = attrInfo.getName();
        for(Iterator it=attributeList.iterator(); it.hasNext(); ){
            ObjectAttribute attribute = (ObjectAttribute)it.next();
            if(attribute != null && attribute.getName().equals(attrName)){
                return attribute;
            }
        }
        return new ObjectAttribute(attrName, ObjectAttribute.STATUS_NOT_FOUND, null);
    }

    public static boolean isDataTypeEditable(String type){
        if(type.equals("boolean")
                || type.equals("char")
                || type.equals("byte")
                || type.equals("short")
                || type.equals("int")
                || type.equals("long")
                || type.equals("float")
                || type.equals("double")
                || type.equals("void")
                || type.equals("java.lang.Boolean")
                || type.equals("java.lang.Character")
                || type.equals("java.lang.Byte")
                || type.equals("java.lang.Short")
                || type.equals("java.lang.Integer")
                || type.equals("java.lang.Long")
                || type.equals("java.lang.Float")
                || type.equals("java.lang.Double")
                || type.equals("java.lang.Void")
                || type.equals("java.lang.String")
                || type.equals("java.math.BigInteger")
                || type.equals("java.math.BigDecimal")
                || type.equals("java.util.Date")
                || type.equals("javax.management.ObjectName")){
            return true;
        }
        return false;
    }

    public static boolean isKnownDataType(Object obj){
        if(obj == null)
            return false;

        Class clazz = obj.getClass();
        if(clazz.isPrimitive()
            || clazz.isAssignableFrom(Boolean.class)
            || clazz.isAssignableFrom(Character.class)
            || clazz.isAssignableFrom(Byte.class)
            || clazz.isAssignableFrom(Short.class)
            || clazz.isAssignableFrom(Integer.class)
            || clazz.isAssignableFrom(Long.class)
            || clazz.isAssignableFrom(Float.class)
            || clazz.isAssignableFrom(Double.class)
            || clazz.isAssignableFrom(Void.class)
            || clazz.isAssignableFrom(String.class)
            || clazz.isAssignableFrom(BigInteger.class)
            || clazz.isAssignableFrom(BigDecimal.class)
            || clazz.isAssignableFrom(Date.class)
            || clazz.getName().equals("javax.management.ObjectName")){
            return true;
        }
        return false;
    }

    public static String getImpact(int impact){
        switch(impact){
            case ObjectOperationInfo.INFO:
                return "Information";
            case ObjectOperationInfo.ACTION:
                return "Action";
            case ObjectOperationInfo.ACTION_INFO:
                return "Action and Information";
            case ObjectOperationInfo.UNKNOWN:
                return "Unknown";
            default:
                return "Invalid Impact Value";

        }
    }
}
