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

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.math.BigInteger;
import java.math.BigDecimal;

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

    public static String getAttributeValue(List attributeList,
                            String attrName){
        String value = null;
        for(Iterator it=attributeList.iterator(); it.hasNext(); ){
            ObjectAttribute attribute = (ObjectAttribute)it.next();
            if(attribute.getName().equals(attrName)){
                //TODO: handle different return types
                Object objValue = attribute.getValue();
                if(isKnownDataType(objValue.getClass())){
                    value = objValue.toString();
                }else{
                    value = "Object";
                }
                break;
            }
        }
        return value;
    }

    public static boolean isKnownDataType(Class clazz){
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
            || clazz.isAssignableFrom(Date.class)){
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
