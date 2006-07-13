/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.webui.dashboard.framework;

import org.jdom.Element;

/**
 * A decorator for the base DashboardComponent that adds refereshing behavior using AJAX.
 *
 * @author Rakesh Kalra
 */
public class RefreshingDashboardComponent implements DashboardComponent {

    private final DashboardComponent component;
    private final long refreshInterval;
    
    public RefreshingDashboardComponent(DashboardComponent component, int refreshInterval){
        this.component = component;
        this.refreshInterval = refreshInterval;
    }
    
    public String getId() {
        return component.getId();
    }

    public void init(Element componentConfig) {
        component.init(componentConfig);
    }

    /**
     * Wraps the output of wrapped componet with div tag and appends the following to the end:
     * <p>
     * &lt;script&gt;self.setTimeout("refreshDBComponent(''com3'', 5000, 1)", 5000);&lt;/script&gt;
     * <p>
     * It is not clear to me why single quotes around com3 need to be escaped.
     */
    public String draw(DashboardContext context) {
        StringBuffer output = new StringBuffer();
        output.append(component.draw(context));
        // append script
        final String dashboardId = context.getDashboardConfig().getDashboardId();
        String appId = context.getWebContext().getApplicationConfig().getApplicationId();
        output.append("\n<script>");
        output.append("self.setTimeout(\"refreshDBComponent(");
        output.append("''");
        output.append(dashboardId);
        output.append("'', ''");
        output.append(getId());
        output.append("'', ");
        output.append(refreshInterval + ", " +  appId + ",''dummy'',''dummy'')\", " + refreshInterval + ");");
        output.append("</script>");
        return output.toString();
    }

}
