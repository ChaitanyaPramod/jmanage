package org.jmanage.core.auth;

import org.jmanage.core.util.CoreUtils;

/**
 * Date : Jun 27, 2004 1:50:07 PM
 * @author Shashank
 */
public interface AuthConstants {
    public String USER_CONFIG_FILE_NAME = CoreUtils.getConfigDir()
            +  "/jmanage-users.xml";
    public String AUTH_CONFIG_INDEX = "JManageAuth";
    public String AUTH_CONFIG_FILE_NAME = CoreUtils.getConfigDir()
            + "/jmanage-auth.conf";
    public String AUTH_CONFIG_SYS_PROPERTY = "java.security.auth.login.config";

    /*  'jmanage-users.xml' file related constants  */
    public String JM_USERS = "jmanage-users";
    public String USER = "user";
    public String NAME = "name";
    public String PASSWORD = "password";
    public String STATUS = "status";
    public String LOCK_COUNT = "lockCount";
    public String ROLE = "role";

    public String USER_ADMIN = "admin";

    public String ROLE_OPS = "ops";
    public String ROLE_DEV = "dev";

}
