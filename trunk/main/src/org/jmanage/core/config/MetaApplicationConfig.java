/*
 * Copyright 2000-2004 by Upromise Inc.
 * 117 Kendrick Street, Suite 200, Needham, MA, 02494, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Upromise, Inc. ("Confidential Information").  You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms of an agreement between you and Upromise.
 */
package org.jmanage.core.config;

/**
 *
 * date:  Sep 15, 2004
 * @author	Rakesh Kalra
 */
public class MetaApplicationConfig {

    private boolean displayHost;
    private boolean displayPort;
    private boolean displayURL;
    private boolean displayUsername;
    private boolean displayPassword;
    /* the ApplicationConfig class for this module */
    private String configClass;

    public MetaApplicationConfig(boolean host, boolean port, boolean url,
                                 boolean username, boolean password,
                                 String configClass){
        this.displayHost = host;
        this.displayPort = port;
        this.displayURL = url;
        this.displayUsername = username;
        this.displayPassword = password;
        this.configClass = configClass;
    }

    public boolean isDisplayHost() {
        return displayHost;
    }

    public boolean isDisplayPort() {
        return displayPort;
    }

    public boolean isDisplayURL() {
        return displayURL;
    }

    public boolean isDisplayUsername() {
        return displayUsername;
    }

    public boolean isDisplayPassword() {
        return displayPassword;
    }

    public String getApplicationConfigClassName(){
        return configClass;
    }
}
