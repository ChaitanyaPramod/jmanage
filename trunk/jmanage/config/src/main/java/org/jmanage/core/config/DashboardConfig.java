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

import java.util.List;
import java.util.Map;

/**
 * This can get initialized from two places,
 * ConfigReader: Partially initialized
 * DashboardLoader: Fully initialized
 *
 * <p>
 * Date:  Jan 16, 2006
 * @author	Rakesh Kalra
 */
public class DashboardConfig {

    private String dashboardId;
    private String name;
    private String template;
    private Map<String, DashboardComponent> components;
    private List<DashboardQualifier> qualifiers;

    public DashboardConfig(String dashboardId){
        this.dashboardId = dashboardId;
    }

    public DashboardConfig(String dashboardId, String name, String template,
                           Map<String, DashboardComponent> components,
                           List<DashboardQualifier> qualifiers) {
        this.dashboardId = dashboardId;
        this.name = name;
        this.template = template;
        this.components = components;
        this.qualifiers = qualifiers;
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

        return dashboardId.equals(dashboardConfig.dashboardId);
    }

    public int hashCode() {
        return dashboardId.hashCode();
    }

    public Map<String, DashboardComponent> getComponents() {
        return components;
    }

    public void setComponents(Map<String, DashboardComponent> components) {
        this.components = components;
    }

    public List<DashboardQualifier> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<DashboardQualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }
}
