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
package org.jmanage.core.config;

import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.CoreUtils;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.*;

/**
 * JManageProperties provides an interface to read configuration parameters from
 * jmanage.properties file.
 *
 * Date: Dec 4, 2004 2:38:01 AM
 * @author Shashank Bellary
 * @author Rakesh Kalra
 */
public class JManageProperties extends Properties{

    private static final Logger logger =
            Loggers.getLogger(JManageProperties.class);

    /* see jmanage.properties for documentation of these properties */
    public static String LOGIN_MAX_ATTEMPTS = "login.maxAttempts";
    private static String JMANAGE_URL = "jmanage.url";
    private static String JMANAGE_PORT = "jmanage.port";
    private static String JMANAGE_SSL_PORT = "jmanage.ssl.port";
    private static String JMANAGE_KEYSTORE_FILENAME = "jmanage.ssl.keyfilename";
    private static String JMANAGE_SSL_PASSWORD = "jmanage.ssl.password";
    private static String JMANAGE_SSL_KEY_PASSWORD = "jmanage.ssl.keypassword";
    /*  The only instance   */
    private static JManageProperties jManageProperties = new JManageProperties();

    /**
     * The only constructor, which is private.
     *
     * TODO: I think we should remove this ctor and initialize in static block.
     * All get methods on this class can be static - rk
     */
    private JManageProperties(){
      super();
      try{
        InputStream property =
                new FileInputStream(ConfigConstants.JMANAGE_PROPERTY_FILE);
        load(property);
      }catch(Exception e){
          logger.log(Level.SEVERE, "Error reading " +
                  ConfigConstants.JMANAGE_PROPERTY_FILE, e);
          CoreUtils.exitSystem();
      }
    }

    /**
     * Gets an instance of the JManageProperties class. This is the only way
     * that any class can get and access a JManageProperties object, since the
     * constructor is private
     **/
    public static JManageProperties getInstance() {
      return jManageProperties;
    }

    public static String getJManageURL(){
        return jManageProperties.getProperty(JMANAGE_URL);
    }

    public static Integer getPort(){
        return new Integer(jManageProperties.getProperty(JMANAGE_PORT, "9090"));
    }

    public static Integer getSslPort(){
        if(jManageProperties.getProperty(JMANAGE_SSL_PORT)!=null){
            return new Integer(jManageProperties.getProperty(JMANAGE_SSL_PORT));
        }else{
            return null;
        }
    }

    public static String getKeystrokeFile(){
        return jManageProperties.getProperty(JMANAGE_KEYSTORE_FILENAME);
    }

    public static String getSSLPassword(){
        return jManageProperties.getProperty(JMANAGE_SSL_PASSWORD);
    }

    public static String getSSLKeyPassword(){
        return jManageProperties.getProperty(JMANAGE_SSL_KEY_PASSWORD);
    }

    public void storeMaxLoginAttempts(int maxLoginAttempt){
                this.setProperty(LOGIN_MAX_ATTEMPTS,
                Integer.toString(maxLoginAttempt));
        try{
           FileOutputStream fileOutputStream =
                 new FileOutputStream(ConfigConstants.JMANAGE_PROPERTY_FILE);
            this.store(fileOutputStream, null);
        } catch( Exception e){
            throw new RuntimeException(e);
        }
    }
}
