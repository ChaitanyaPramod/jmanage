package org.jmanage.core.auth;

import java.util.List;
import java.util.Iterator;
import java.security.Principal;

/**
 * Date : Jun 27, 2004 11:52:43 PM
 * @author Shashank
 */
public class User implements Principal{
    private String username;
    private String password;
    private List roles;

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
    public User(String username, String password, List roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
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

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public String getName() {
        return getUsername();
    }

    public boolean isAdmin(){
        for(Iterator it=getRoles().iterator(); it.hasNext();){
            String role = (String)it.next();
            if(role.equals(AuthConstants.ROLE_OPS)){
                return true;
            }
        }
        return false;
    }
}
