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

import java.util.*;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationConfigManager {

    private static final Map applicationConfigs =
            Collections.synchronizedMap(new HashMap());

    public static final String TEST_APP_ID = "1234";

    static{
        /* added temporarily for testing */
        Map paramValues = new HashMap(1);
        paramValues.put("serverName", "loyaltyServer");
        ApplicationConfig config =
                ApplicationConfigFactory.create(TEST_APP_ID,
                        "test",
                        ApplicationConfig.TYPE_WEBLOGIC,
                        "localhost", 7001,
                        "system", "12345678",
                        paramValues);
        applicationConfigs.put(config.getApplicationId(), config);
    }

    public static ApplicationConfig getApplicationConfig(String applicationId){
        return (ApplicationConfig)applicationConfigs.get(applicationId);
    }
}
