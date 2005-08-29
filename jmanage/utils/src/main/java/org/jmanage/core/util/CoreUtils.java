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

import java.io.File;
import java.util.logging.Logger;

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

    private static String dataDir = getRootDir() + "/data";

    static{
        File dataDirFile = new File(dataDir);
        if(!dataDirFile.exists()){
             dataDirFile.mkdirs();
        }
    }
    public static String getDataDir() {
        return dataDir;
    }

    public static void copyProperties(Object dest, Object source) {
        try {
            BeanUtils.copyProperties(dest, source);
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void exitSystem(){
        logger.severe("Shutting down application");
        System.exit(1);
    }
}