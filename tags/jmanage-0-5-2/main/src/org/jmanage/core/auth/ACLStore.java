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
import org.jmanage.util.StringUtils;

import java.util.*;
import java.util.logging.Logger;
import java.io.*;

/**
 * Date: Mar 8, 2005 8:00:47 AM
 * @author Shashank Bellary 
 */
public class ACLStore {

    private static final String ACL_CONFIG_FILE = "acl-config.properties";
    private static final Logger logger = Loggers.getLogger(ACLStore.class);
    private static final ACLStore instance = new ACLStore();

    private Map aclNameToACLMap = new HashMap();

    /**
     *
     */
    private ACLStore() {

        final String configFile = CoreUtils.getConfigDir() + File.separator +
                ACL_CONFIG_FILE;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line = reader.readLine();
            while(line != null){
                parse(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading: " + configFile, e);
        }
        logger.info("Loaded ACLs");
    }


    /**
     * The only access to this instance.
     *
     * @return
     */
    public static ACLStore getInstance() {
        return instance;
    }

    public ACL getACL(String aclName){
        return (ACL)aclNameToACLMap.get(aclName);
    }

    private void parse(String line){
        line = line.trim();
        if(line.length() == 0 || line.startsWith("#")){
            return;
        }

        int index = line.lastIndexOf('=');
        if(index == -1){
            throw new RuntimeException("Invalid line format: " + line);
        }
        String acl = line.substring(0, index);
        String authorizedList = line.substring(index + 1);
        /* now seperate acl name from the context */
        index = acl.indexOf('@');
        String aclName = null;
        String aclContext = null;
        if(index != -1){
            aclName = acl.substring(0, index);
            aclContext = acl.substring(index+1);
        }else{
            aclName = acl;
        }

        storeACL(aclName, aclContext, authorizedList);
    }

    private void storeACL(String aclName,
                          String aclContext,
                          String authorizedList){
        ACL acl = (ACL)aclNameToACLMap.get(aclName);
        if(acl == null){
            acl = new ACL(aclName);
            aclNameToACLMap.put(aclName, acl);
        }
        List authorizedListObj = StringUtils.csvToList(authorizedList);
        if(aclContext == null){
            acl.setAuthorizedList(authorizedListObj);
        }else{
            acl.add(new ACLContext(aclContext), authorizedListObj);
        }
        logger.fine("Added ACL: " + aclName + " - " +
                aclContext + " - " + authorizedList);
    }
}