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
 * date:  Jul 21, 2004
 * @author	Rakesh Kalra
 */
public class MBeanConfigForm extends BaseForm {

    private String name;
    private String objectName;
    private boolean applicationCluster;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public boolean isApplicationCluster(){
        return applicationCluster;
    }

    public void setApplicationCluster(boolean applicationCluster){
        this.applicationCluster = applicationCluster;
    }
}
