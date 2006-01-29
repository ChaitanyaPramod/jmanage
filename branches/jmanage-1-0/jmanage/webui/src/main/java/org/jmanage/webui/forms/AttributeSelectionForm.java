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
 * Date: Aug 24, 2005 2:10:30 PM
 * @author Bhavana
 * @author Shashank Bellary
 */
public class AttributeSelectionForm extends BaseForm{
    private String[] mbeans;
    private String endURL;
    private String[] attributes;
    private String multiple;
    private String attribute;
    private String[] dataTypes;
    private String alertSourceType;
    private String navigation;
    private String objectName;

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getAlertSourceType(){
        return alertSourceType;
    }

    public void setAlertSourceType(String alertSourceType){
        this.alertSourceType = alertSourceType;
    }

    public String[] getDataTypes(){
        return dataTypes;
    }
    public void setDataTypes(String[] dataTypes){
        this.dataTypes = dataTypes;
    }
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String[] getMbeans() {
        return mbeans;
    }

    public void setMbeans(String[] mbeans) {
        this.mbeans = mbeans;
    }

    public String getEndURL(){
        return endURL;
    }

    public void setEndURL(String endURL){
        this.endURL = endURL;
    }
    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
