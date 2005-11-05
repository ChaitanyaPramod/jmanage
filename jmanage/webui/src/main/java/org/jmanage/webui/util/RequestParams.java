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
package org.jmanage.webui.util;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public interface RequestParams {

    public static final String APPLICATION_ID = "applicationId";
    public static final String OBJECT_NAME = "objName";
    public static final String REFRESH_APPS = "refreshApps";
    public static final String USER_NAME = "username";
    public static final String ALERT_ID = "alertId";

    // following params are used for login of user on any URL
    public static final String JMANAGE_USERNAME = "jmanage.username";
    public static final String JMANAGE_PASSWORD = "jmanage.password";

    public static final String END_URL = "endURL";
    public static final String MULTIPLE = "multiple";
    public static final String ALERT_SOURCE_TYPE = "alertSourceType";
    public static final String DATA_TYPE = "dataTypes";
    public static final String NAVIGATION = "navigation";

    public static final String GRAPH_ID = "graphId";
    public static final String MBEANS = "mbeans";
}
