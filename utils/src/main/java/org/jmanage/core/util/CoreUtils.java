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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 *
 * date:  Jun 22, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class CoreUtils {

    private static final Logger logger = Loggers.getLogger(CoreUtils.class);

    private static String rootDir;
    private static String dataDir;
    private static String metadataDir;

    public static void init(String appRootDir, String metadataDirPath){
    	rootDir = appRootDir;
    	assert rootDir != null;

    	metadataDir = metadataDirPath;
    	assert rootDir != null;
    	
    	/* create logs dir, before doing any logging */
    	new File(CoreUtils.getLogDir()).mkdirs();
    	logger.info("app root dir= " + rootDir);
    	
    	/* create data dir */
    	dataDir = getRootDir() + "/data";
    	File dataDirFile = new File(dataDir);
    	if(!dataDirFile.exists()){
    		dataDirFile.mkdirs();
    	}
    }
    
    public static String getRootDir(){
        return rootDir;
    }

    
    public static String getMetadataDir() {
		return metadataDir;
	}

	public static String getConnectorDir() {
        return getMetadataDir() + File.separatorChar + "connector";    
    }

    public static String getConfigDir(){
        return getRootDir() + "/config";
    }

    public static String getDashboardsDir(){
        return getMetadataDir() + File.separatorChar + "dashboards";
    }

    public static String getModuleDir(String moduleId){
        return getMetadataDir() + "/modules/" + moduleId;
    }

    public static String getApplicationDir(String appId){
        return getMetadataDir() + "/applications/" + appId;
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
    
    public static void copyConfig(File configDir, File configSrcDir) throws Exception{
    	if(!configDir.exists()){
    		configDir.mkdir();
    		copyDirectory(configSrcDir, configDir);
    		deleteFile(configSrcDir);
    	}
    }
    
    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    public static void deleteFile(File fileLocation) throws IOException {
	    if(fileLocation.isDirectory()) {
			File[] files = fileLocation.listFiles();
			for(int fileCtr = 0; fileCtr < files.length; fileCtr++){
				deleteFile(files[fileCtr]);
			}
			fileLocation.delete();
	    }else{
	    	fileLocation.delete();
	    }
    }
}