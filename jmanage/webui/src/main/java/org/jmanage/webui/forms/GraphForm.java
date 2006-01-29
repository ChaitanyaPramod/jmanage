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

import org.jmanage.core.util.Expression;

/**
 * Date: Jun 23, 2005 7:36:48 PM
 * @author Bhavana
 */
public class GraphForm extends BaseForm{

    private String graphId;
    private String graphName;
    private String pollInterval;
    private String[] mbeans;
    private String[] attributes;
    private String[] displayNames;

    public String[] getDisplayNames(){
        return displayNames;
    }
    public void setDiaplayNames(String[] displayNames){
        this.displayNames = displayNames;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public String[] getMbeans() {
        return mbeans;
    }

    public void setMbeans(String[] mbeans) {
        this.mbeans = mbeans;
    }



    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public String getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(String pollInterval) {
        this.pollInterval = pollInterval;
    }
}
