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

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jmanage.core.util.Loggers;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Curently loading roles from configuration files.
 *
 * Date: Mar 20, 2005 12:58:25 PM
 * @author Shashank Bellary 
 */
public class RoleManager implements AuthConstants{
    private static long lastModified = -1;
    private static List roles = null;
    private static final Logger logger = Loggers.getLogger(RoleManager.class);

    static{
        init(new File(ROLE_CONFIG_FILE_NAME));
    }

    private RoleManager(){}

    /**
     * Initializer
     *
     * @param roleConfigFile
     */
    private static void init(File roleConfigFile) {
        try{
            lastModified = roleConfigFile.lastModified();
            Document roleConfig = new SAXBuilder().build(roleConfigFile);
            roles = loadUserRoles(roleConfig);
        }catch(JDOMException jdEx){
            logger.info("Error reading roles "+ROLE_CONFIG_FILE_NAME);
            jdEx.printStackTrace();
        }
    }

    /**
     * The only accessor method to the configured roles.
     *
     * @return
     */
    public static List getAll(){
        File roleConfigFile = new File(ROLE_CONFIG_FILE_NAME);
        if(lastModified < roleConfigFile.lastModified()){
            /*  Refresh the cache   */
            init(roleConfigFile);
        }
        return roles;
    }

    /**
     * Read the configuration file and load all configured roles
     *
     * @param roleConfig
     * @return
     */
    private static List loadUserRoles(Document roleConfig){
        List userRoles = new ArrayList();
        List configuredRoles = roleConfig.getRootElement().getChildren();
        Iterator roleIterator = configuredRoles.iterator();
        while(roleIterator.hasNext()){
            Element role = (Element)roleIterator.next();
            userRoles.add(new Role(role.getTextTrim()));
        }
        return userRoles;
    }
}