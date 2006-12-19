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
package org.jmanage.core.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.util.ACLConstants;
import org.jmanage.core.util.Loggers;
import org.jmanage.core.auth.ACL;
import org.jmanage.core.auth.ACLStore;
import org.jmanage.core.auth.ACLContext;
import org.jmanage.core.auth.UnAuthorizedAccessException;

/**
 * TODO: it may make sense to move this to services package - rk
 *
 * Date: Mar 14, 2005 12:16:11 AM
 * @author Shashank Bellary
 * @author Rakesh Kalra
 */
public class AccessController {

    private static final Logger logger = Loggers.getLogger(AccessController.class);
    
    /**
     * This method is normaly ued to conditionalize content.
     *
     * @param context   the service context for this call
     * @param aclName
     * @return true if the curren tuser can access give acl
     */
    public static boolean canAccess(ServiceContext context,
                                    String aclName,
                                    String targetName){

        ACL acl = ACLStore.getInstance().getACL(aclName);
        if(acl == null){
            /* if acl is not specified, user has access by default */
            if(logger.isLoggable(Level.FINE))
                logger.fine("acl not configured:" + aclName);
            return true;
        }

        /* construct ACLContext from ServiceContext */
        ACLContext aclContext = getACLContext(context, targetName);
        if(acl.isAuthorized(aclContext, context.getUser())){
            return true;
        }
        return false;
    }

    public static boolean canAccess(ServiceContext context,
                                        String aclName){
        return canAccess(context, aclName, null);
    }

    public static void checkAccess(ServiceContext context,
                                    String aclName,
                                    String targetName)
        throws UnAuthorizedAccessException {

        if(!canAccess(context, aclName, targetName)){
            throw new UnAuthorizedAccessException("Insufficient Privileges");
        }
    }

    public static void checkAccess(ServiceContext context,
                                    String aclName)
        throws UnAuthorizedAccessException {

        checkAccess(context, aclName, null);
    }

    /**
     * Checks if the logged-in user has admin access.
     * 
     * @param context
     * @return true if the user has admin access
     */
    public static boolean hasAdminAccess(ServiceContext context){
        for(String aclName: ACLConstants.ADMIN_ACLS){
            if(canAccess(context, aclName)){
                return true;
            }
        }
        // User doesn't have any of the admin ACLs
        return false;
    }
    
    private static ACLContext getACLContext(ServiceContext context,
                                            String targetName){
        String appName = null;
        String mbeanName = null;
        if(context.getApplicationConfig() != null){
            appName = context.getApplicationConfig().getName();
        }
        if(context.getObjectName() != null){
            mbeanName = context.getObjectName().getCanonicalName();
        }
        return new ACLContext(appName, mbeanName, targetName);
    }
}