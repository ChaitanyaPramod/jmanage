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

import java.util.Properties;
import java.io.*;

/**
 * Date: Dec 4, 2004 2:38:01 AM
 * @author Shashank Bellary 
 */
public class JManageProperties extends Properties{

    public static String maxLoginAttempts = "login.maxAttempts";
    public static String JMANAGE_HOST = "jmanage.host";
    public static String JMANAGE_PORT = "jmanage.port";

    /*  The only instance   */
    private static JManageProperties jManageProperties = new JManageProperties();

    /**
     * The only constructor, which is priivate.
     */
    private JManageProperties(){
      super();
      try{
        InputStream property =
                new FileInputStream(ConfigConstants.JMANAGE_PROPERTY_FILE);
        load(property);
      }catch(FileNotFoundException fnfEx){
        fnfEx.printStackTrace();
      }catch(IOException ioe){
        ioe.printStackTrace();
      }catch(Exception ex){
        ex.printStackTrace();
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

    public static String getHostName(){
        return jManageProperties.getProperty(JMANAGE_HOST, "localhost");
    }

    public static Integer getPort(){
        return new Integer(jManageProperties.getProperty(JMANAGE_PORT, "9090"));
    }

    public void storeMaxLoginAttempts(int maxLoginAttempt){
                this.setProperty("login.maxAttempts",
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
