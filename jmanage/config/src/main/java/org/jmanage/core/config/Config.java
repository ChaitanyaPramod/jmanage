/**
 * Copyright 2004-2005 jManage.org. All rights reserved.
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

import java.util.List;

/**
 *
 * <p>
 * Date:  Feb 8, 2006
 * @author	Rakesh Kalra
 */
public class Config {

    private final List applications;
    private final List dashboards;

    public Config(List applications, List dashboards){
        this.applications = applications;
        this.dashboards = dashboards;
    }

    public List getApplications() {
        return applications;
    }

    public List getDashboards() {
        return dashboards;
    }
}
