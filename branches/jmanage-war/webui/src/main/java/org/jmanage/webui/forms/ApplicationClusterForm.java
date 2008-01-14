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
package org.jmanage.webui.forms;

/**
 *
 * date:  Oct 17, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationClusterForm extends BaseForm {

    private static final long serialVersionUID = 1L;
	
	private String appId;
    private String name;
    private String[] childApplicationIds;
    private String[] standAloneApplicationIds;
    private String selectedChildApplications;

    public String getApplicationId() {
        return appId;
    }

    public void setApplicationId(String appId) {
        if(appId != null && appId.length() > 0)
            this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getChildApplicationIds() {
        return childApplicationIds;
    }

    public void setChildApplicationIds(String[] childApplicationIds) {
        this.childApplicationIds = childApplicationIds;
    }

    public String[] getStandAloneApplicationIds() {
        return standAloneApplicationIds;
    }

    public void setStandAloneApplicationIds(String[] standAloneApplicationIds) {
        this.standAloneApplicationIds = standAloneApplicationIds;
    }

    public String getSelectedChildApplications() {
        return selectedChildApplications;
    }

    public void setSelectedChildApplications(String selectedChildApplications) {
        this.selectedChildApplications = selectedChildApplications;
    }
}
