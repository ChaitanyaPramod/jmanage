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

package org.jmanage.core.auth;

import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.Loggers;

import java.util.Properties;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.io.*;

/**
 * Date: Mar 8, 2005 8:00:47 AM
 * @author Shashank Bellary 
 */
public class ACLStore extends Properties {
    private static final String ACL_CONFIG_FILE = "acl-config.properties";
    private static final Logger logger = Loggers.getLogger(ACLStore.class);
    private static final ACLStore instance = new ACLStore();

    /**
     *
     */
    private ACLStore() {
        super();
        final String configFile = CoreUtils.getConfigDir() + File.separator +
                ACL_CONFIG_FILE;
        try{
            InputStream is = new FileInputStream(configFile);
            load(is);
        }catch(FileNotFoundException fne){
            fne.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Initializer.
     */
    public static void init(){
        logger.info("Loaded ACLs");
        Enumeration acls = getInstance().keys();
        while(acls.hasMoreElements()){
            logger.info(acls.nextElement().toString());
        }

    }

    /**
     * The only access to this instance.
     *
     * @return
     */
    public static ACLStore getInstance() {
        return instance;
    }
}