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

import javax.servlet.http.HttpServletRequest;

import org.jmanage.webui.util.Utils;
import org.jmanage.webui.util.WebContext;

/**
 *
 * @author Rakesh Kalra
 */
public class DashboardContextImpl implements DashboardContext {

	private final WebContext webContext;
    private final HttpServletRequest request;
    private final DashboardConfig dashboardConfig;
    private String serverPath;
    private boolean isRefreshRequest;
    
    public DashboardContextImpl(WebContext webContext,  
            DashboardConfig dashboardConfig,
            HttpServletRequest request){
        this(webContext, dashboardConfig, request, false);
    }
    
    public DashboardContextImpl(WebContext webContext,  
            DashboardConfig dashboardConfig,
            HttpServletRequest request,
            boolean isRefreshRequest){
        this.webContext = webContext; 
        this.dashboardConfig = dashboardConfig;
        this.request = request;
        this.isRefreshRequest = isRefreshRequest;
        setServerPath();
    }
    
    public WebContext getWebContext() {
        return webContext;
    }

    public DashboardConfig getDashboardConfig() {
        return dashboardConfig;
    }

    public String getVariableValue(String variable) {
        return request.getParameter(variable);
    }
    
    private void setServerPath() {
		serverPath = Utils.getServerBaseURL(request);
    }

    public String getServerPath() {
    	return serverPath;
	}

    public boolean isRefreshRequest() {
        return isRefreshRequest;
    }
}