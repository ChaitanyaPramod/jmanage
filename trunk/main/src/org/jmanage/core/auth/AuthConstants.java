package org.jmanage.core.auth;

/**
 * Date : Jun 27, 2004 1:50:07 PM
 * @author Shashank
 */
public interface AuthConstants {
    public String USER_CONFIG_FILE_NAME = "build/config/jmanage-users.xml";
    public String AUTH_CONFIG_INDEX = "JManageAuth";
    public String AUTH_CONFIG_FILE_NAME = "build/config/jmanage-auth.conf";
    public String AUTH_CONFIG_SYS_PROPERTY = "java.security.auth.login.config";

    /*  'jmanage-users.xml' file related constants  */
    public String JM_USERS = "jmanage-users";
    public String USER = "user";
    public String NAME = "name";
    public String PASSWORD = "password";
    public String ROLE = "role";

    public String ROLE_OPS = "ops";
    public String ROLE_DEV = "dev";

}
