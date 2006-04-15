/*
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
 * date:  Dec 28, 2005
 * 
 * @author	Tak-Sang Chan
 */
public class ConnectorForm extends BaseForm {

    public final static String CONNECTOR_ID = "connectorId";

    private int MAX_NUM_CONFIG = 10;

    private String applicationId;
    private String type;
    private String name;

    private String[] configNames = new String[MAX_NUM_CONFIG];
    private String[] configValues = new String[MAX_NUM_CONFIG];

    private String[] connectorNames;
    private String[] connectorIds;
    private String connectorId;
    private String connectorName;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String appId) {
        this.applicationId = appId;
    }

    /**
     *
     * @return  Application Type
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Unique name for a connector configuration
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConnectorCount() {
        return connectorNames.length;
    }

    public String[] getConfigNames() {
        return configNames;
    }

    public String getConfigName(int index) {
        return this.configNames[index];
    }

    public void setConfigNames(String[] configNames) {
        this.configNames = configNames;
    }

    public void setConfigName(int index, String value) {
        this.configNames[index] = value;
    }

    public String[] getConfigValues() {
        return configValues;
    }

    public void setConfigValues(String[] configValues) {
        this.configValues = configValues;
    }

    public String getConfigValue(int index) {
        return this.configValues[index];
    }

    /**
     * Note Struts does not set the value in the order of field being
     * displayed.  It is more like random, such as 3, 1, 0, 2 if
     * there are four fields.
     *
     * @param index
     * @param value
     */
    public void setConfigValue(int index, String value) {
        this.configValues[index] = value;
    }

    public String[] getConnectorNames() {
        return connectorNames;
    }

    public void setConnectorNames(String[] connectorNames) {
        this.connectorNames = new String[connectorNames.length + 1];
        this.connectorNames[0] = "Select a connector...";
        for (int i = 0; i < connectorNames.length; i++) {
            this.connectorNames[i+1] = connectorNames[i];
        }
    }

    public String[] getConnectorIds() {
        return connectorIds;
    }

    public void setConnectorIds(String[] connectorIds) {
        this.connectorIds = new String[connectorIds.length + 1];
        this.connectorIds[0] = "none";
        for (int i = 0; i < connectorIds.length; i++) {
            this.connectorIds[i+1] = connectorIds[i];
        }
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public void setConnectorName(String value) {
        this.connectorName = value;
    }

    public String getConnectorName() {
        return connectorName;
    }
}
