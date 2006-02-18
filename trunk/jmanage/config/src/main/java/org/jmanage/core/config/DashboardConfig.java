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

/**
 *
 * <p>
 * Date:  Jan 16, 2006
 * @author	Rakesh Kalra
 */
public class DashboardConfig {

    private String dashboardId;
    private String name;
    private String template;

    public DashboardConfig(String dashboardId, String name){
        this.dashboardId = dashboardId;
        this.name = name;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DashboardConfig)) return false;

        final DashboardConfig dashboardConfig = (DashboardConfig) o;

        if (!dashboardId.equals(dashboardConfig.dashboardId)) return false;

        return true;
    }

    public int hashCode() {
        return dashboardId.hashCode();
    }

}
