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
package org.jmanage.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.jmanage.core.management.ServerConnection;

import java.util.logging.Logger;
import java.io.IOException;

/**
 *
 * date:  Jun 22, 2004
 * @author	Rakesh Kalra
 */
public class CoreUtils {

    private static final Logger logger = Loggers.getLogger(CoreUtils.class);

    public static String getRootDir(){
        return System.getProperty(SystemProperties.JMANAGE_ROOT);
    }

    public static String getConfigDir(){
        return getRootDir() + "/config";
    }

    public static String getWebDir(){
        return getRootDir() + "/web";
    }

    public static String getModuleDir(String moduleId){
        return getRootDir() + "/modules/" + moduleId;
    }

    public static String getLogDir(){
        return getRootDir() + "/logs";
    }


    public static void copyProperties(Object dest, Object source) {
        try {
            BeanUtils.copyProperties(dest, source);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
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

    public static Object[] getTypedArray(String[] values, String[] type){
        Object[] obj = new Object[values.length];
        for(int i=0; i<values.length; i++){
            obj[i] = getTypedValue(values[i], type[i]);
        }
        return obj;
    }

    public static void exitSystem(){
        logger.severe("Shutting down application");
        System.exit(1);
    }

    public static void close(ServerConnection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (IOException e) {
                logger.info("Error closing connection: " + e.getMessage());
            }
        }
    }
}