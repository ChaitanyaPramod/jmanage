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

import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.security.Principal;

/**
 * Date : Jun 27, 2004 11:52:43 PM
 * @author Shashank
 */
public class User implements Principal, java.io.Serializable{

    private String username;
    private String password;
    private List roles;
    private String status;
    private int lockCount;

    /**
     * Default,
     */
    public User(){}

    /**
     * Create users with specified permissions (represented by roles).
     *
     * @param username
     * @param password
     * @param roles
     */
    public User(String username, String password, List roles, String status,
                int lockCount) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.lockCount = lockCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List getRoles() {
        return roles;
    }

    public String getRolesAsString(){
        StringBuffer strRoles = new StringBuffer();
        for(Iterator it=roles.iterator(); it.hasNext();){
            Role role = (Role)it.next();
            strRoles.append(role.getName());
            if(it.hasNext())
                strRoles.append(", ");
        }
        return strRoles.toString();
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    public String getName() {
        return getUsername();
    }

    // TODO: check all usage and see if they are valid after ACL impl.
    public boolean isAdmin(){
        for(Iterator it=getRoles().iterator(); it.hasNext();){
            Role role = (Role)it.next();
            if(AuthConstants.ROLE_OPS.equals(role.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param acl
     * @return
     */
    public boolean canAccess(String acl){
        String authorizedList = ACLStore.getInstance().getProperty(acl);
        /*if acl is not configured, by default there is access */
        if(authorizedList == null)
            return true;
        StringTokenizer tokenizer = new StringTokenizer(authorizedList, ",");
        while(tokenizer.hasMoreTokens()){
            if(getUsername().equalsIgnoreCase(tokenizer.nextToken())){
                return true;
            }
        }
        for(Iterator it=getRoles().iterator(); it.hasNext();){
            Role role = (Role)it.next();
            if(role.canAccess(acl))
                return true;
        }
        return false;
    }
}