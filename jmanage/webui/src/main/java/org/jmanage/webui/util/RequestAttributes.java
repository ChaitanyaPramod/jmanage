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
public interface RequestAttributes {

    public static final String APPLICATION_CONFIG = "applicationConfig";
    public static final String APPLICATIONS = "applications";
    public static final String USERS = "users";
    public static final String ROLES = "roles";

    public static final String WEB_CONTEXT = "webContext";
    public static final String META_APP_CONFIG = "metaAppConfig";
    public static final String AVAILABLE_APPLICATIONS = "availableApplications";
    public static final String USER_ACTIVITIES = "userActivities";

    public static final String NAV_CURRENT_PAGE = "navCurrentPage";
    public static final String AUTHENTICATED_USER = "authenticatedUser";
    public static final String QUALIFYING_DASHBOARDS = "qualifyingDashboards";

}
