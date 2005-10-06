/*
 * Copyright 2000-2003 by Upromise Inc.
 * 117 Kendrick Street, Suite 200, Needham, MA, 02494, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Upromise, Inc. ("Confidential Information").  You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms of an agreement between you and Upromise.
 */
package org.jmanage.core.auth;

import org.jmanage.core.util.Loggers;
import org.jmanage.core.util.CoreUtils;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.InputStream;
import java.io.FileInputStream;

/**
 * @author shashank
 * Date: Sep 27, 2005
 */
public class ExternalUserRolesConfig extends Properties{
    private static final Logger logger =
            Loggers.getLogger(ExternalUserRolesConfig.class);

    private String EXTERNAL_USER_ROLES_CONFIG_FILE = CoreUtils.getConfigDir() +
            "/external-user-roles.properties";

    private final String ASTERIX = "*";

    /*  The only instance   */
    private static ExternalUserRolesConfig instance =
            new ExternalUserRolesConfig();

    /**
     *
     */
    private ExternalUserRolesConfig(){
      super();
      try{
        InputStream property =
                new FileInputStream(EXTERNAL_USER_ROLES_CONFIG_FILE);
        load(property);
      }catch(Exception e){
          logger.log(Level.SEVERE, "Error reading " +
                  EXTERNAL_USER_ROLES_CONFIG_FILE, e);
          CoreUtils.exitSystem();
      }
    }

    public static ExternalUserRolesConfig getInstance(){
        return instance;
    }

    public String getUserRole(String username){
        String role = instance.getProperty(username);
        return role != null ? role : instance.getProperty(ASTERIX);
    }
}
