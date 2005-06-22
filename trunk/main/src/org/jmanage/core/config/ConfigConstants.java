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
package org.jmanage.core.config;

import org.jmanage.core.util.CoreUtils;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank, Rakesh Kalra
 */
public interface ConfigConstants {

    /*  Default config file to use  */
    public String DEFAULT_CONFIG_FILE_NAME = CoreUtils.getConfigDir()
            + "/config.xml";
    /* booted config file */
    public String BOOTED_CONFIG_FILE_NAME = DEFAULT_CONFIG_FILE_NAME + ".booted";

    public String JMANAGE_PROPERTY_FILE = CoreUtils.getConfigDir() +
            "/jmanage.properties";

    public String APPLICATION_CONFIG = "application-config";
    public String APPLICATIONS = "applications";
    public String APPLICATION_CLUSTER = "application-cluster";
    public String APPLICATION = "application";
    public String PARAMETER = "param";
    public String PARAMETER_NAME = "param-name";
    public String PARAMETER_VALUE = "param-value";
    public String APPLICATION_ID = "id";
    public String APPLICATION_NAME = "name";
    public String APPLICATION_TYPE = "type";
    public String HOST = "host";
    public String PORT = "port";
    public String URL = "url";
    public String USERNAME = "username";
    public String PASSWORD = "password";

    public String MBEANS = "mbeans";
    public String MBEAN = "mbean";
    public String MBEAN_NAME = "name";
    public String MBEAN_OBJECT_NAME = "object-name";

    public String ALERTS="alerts";
    public String ALERT="alert";
    public String ALERT_ID="id";
    public String ALERT_NAME="name";
    public String ALERT_DELIVERY = "delivery";
    public String ALERT_DELIVERY_TYPE="type";
    public String ALERT_EMAIL_ADDRESS="emailAddress";
    public String ALERT_SUBJECT="subject";

    public String GRAPHS = "graphs";
    public String GRAPH = "graph";
    public String GRAPH_ID = "id";
    public String GRAPH_NAME = "name";
    public String GRAPH_POLLING_INTERVAL = "pollingInterval";
    public String GRAPH_ATTRIBUTE = "attribute";
    public String GRAPH_ATTRIBUTE_MBEAN = "mbean";
    public String GRAPH_ATTRIBUTE_NAME = "attribute";
    public String GRAPH_ATTRIBUTE_DISPLAY_NAME = "displayName";

}
