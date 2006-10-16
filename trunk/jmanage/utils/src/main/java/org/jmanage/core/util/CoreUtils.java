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
import org.jmanage.util.db.DBUtils;

import java.io.File;
import java.util.logging.Logger;
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 *
 * date:  Jun 22, 2004
 * @author	Rakesh Kalra
 */
public class CoreUtils {

    private static final Logger logger = Loggers.getLogger(CoreUtils.class);

    public static final String RELATIVE_DASHBOARDS_PATH = "/WEB-INF/dashboards/";

    private static final String rootDir;
    private static String dataDir;

    static{
        rootDir = System.getProperty(SystemProperties.JMANAGE_ROOT);
        assert rootDir != null;
        logger.info("jManage.root=" + rootDir);
        
        /* create data dir */
        dataDir = getRootDir() + "/data";
        File dataDirFile = new File(dataDir);
        if(!dataDirFile.exists()){
             dataDirFile.mkdirs();
        }
        
        /* create db tables if they don't exist */
        File dbFile = new File(dataDir+"/db.properties");
        if(!dbFile.exists()){
            logger.info("Creating DB tables");
            DBUtils.createTables();
        }else{
            /* if lock file was left around -- try to delete it */
            File dbLockFile = new File(dataDir + "/db.lck");
            if(dbLockFile.exists()){
                logger.warning("DB lock file exists. Trying to delete.");
                dbLockFile.delete();
            }
        }
    }
    
    public static String getRootDir(){
        return rootDir;
    }

    public static String getConnectorDir() {
        return getRootDir() + File.separatorChar + "connector";    
    }

    public static String getConfigDir(){
        return getRootDir() + "/config";
    }

    public static String getWebDir(){
        return getRootDir() + "/web";
    }

    public static String getDashboardsDir(){
        return getRootDir() + File.separatorChar + "dashboards";
    }

    public static String getModuleDir(String moduleId){
        return getRootDir() + "/modules/" + moduleId;
    }

    public static String getApplicationDir(String appId){
        return getRootDir() + "/applications/" + appId;
    }

    public static String getLogDir(){
        return getRootDir() + "/logs";
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

    public static Number valueOf(String value, String dataType){
        if(dataType.equals("java.lang.Integer")|| dataType.equals("int")){
            return new Integer(value);
        }
        if(dataType.equals("java.lang.Double") || dataType.equals("double")){
            return new Double(value);
        }
        if(dataType.equals("java.lang.Long") || dataType.equals("long")){
            return new Long(value);
        }
        if(dataType.equals("java.lang.Float") || dataType.equals("float")){
            return new Double(value);
        }
        if(dataType.equals("java.lang.Short") || dataType.equals("short")){
            return new Short(value);
        }
        if(dataType.equals("java.lang.Byte") || dataType.equals("byte")){
            return new Byte(value);
        }
        if(dataType.equals("java.math.BigInteger")){
            return new BigInteger(value);
        }
        if(dataType.equals("java.math.BigDecimal")){
            return new BigDecimal(value);
        }
        return null;
    }
}