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

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.jmanage.core.services.ServiceContext;

import java.util.StringTokenizer;

/**
 *
 * date:  Jun 20, 2004
 * @author	Rakesh Kalra
 */
public class Utils {

    /**
     * Appends param and value to the given url.
     *
     * @param url
     * @param param
     * @param value
     * @return
     */
    public static String appendURLParam(String url, String param, String value){

        StringBuffer urlString = new StringBuffer(url);
        if (url.indexOf("?") == -1) {
            urlString.append("?");
        }else if(url.endsWith("&") == false) {
            urlString.append("&");
        }
        urlString.append(param);
        urlString.append("=");
        urlString.append(value);
        return urlString.toString();
    }

    public static Object getTypedValue(String value, String type){


        if(type.equals("int")){
            type = "java.lang.Integer";
        }else if(type.equals("long")){
            type = "java.lang.Long";
        }else if(type.equals("short")){
            type = "java.lang.Short";
        }else if(type.equals("float")){
            type = "java.lang.Float";
        }else if(type.equals("double")){
            type = "java.lang.Double";
        }else if(type.equals("char")){
            type = "java.lang.Character";
        }else if(type.equals("boolean")){
            type = "java.lang.Boolean";
        }else if(type.equals("byte")){
            type = "java.lang.Byte";
        }

        try {
            return ConvertUtils.convert(value, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static ServiceContext getServiceContext(WebContext webContext){
        return new ServiceContextImpl(webContext);
    }
}
