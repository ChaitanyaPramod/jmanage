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

package org.jmanage.webui.taglib.jm;

import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Utils;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.DashboardConfig;
import org.jmanage.core.config.DashboardComponent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Date: May 13, 2006 10:13:43 AM
 *
 * @author Shashank Bellary
 */
public class DashboardComponentTag extends BaseTag{
    private String id;
    private int width = 400;
    private int height = 300;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int doStartTag() throws JspException{
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        WebContext context = WebContext.get(request);
        ApplicationConfig appConfig = context.getApplicationConfig();
        // Graphs at cluster level are not supported yet
        assert !appConfig.isCluster();
        String currentDashboardId = request.getParameter("dashBID");
        DashboardConfig currentDashboardConfig = null;
        for(DashboardConfig dbConfig : appConfig.getDashboards()){
            if(currentDashboardId.equals(dbConfig.getDashboardId())){
                currentDashboardConfig = dbConfig;
                break;
            }
        }
        assert currentDashboardConfig != null : "Error retrieving dashboard details";
        DashboardComponent component =
                currentDashboardConfig.getComponents().get(getId());
        String componentDisplay = component.draw(appConfig.getName());
        componentDisplay = MessageFormat.format(componentDisplay, getWidth(),
                getHeight(), Utils.getCookieValue(request, "JSESSIONID"));
        final JspWriter writer = pageContext.getOut();

        try {
            writer.println(componentDisplay);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}
