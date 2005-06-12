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
import java.util.Iterator;

/**
 *
 * Date:  Jun 12, 2005
 * @author	Rakesh Kalra
 */
public class GraphConfig {

    private String id;
    private String name;
    private long pollingInterval;
    // list of GraphAttrbuteConfig objects
    private List attributes;
    private ApplicationConfig appConfig;

    public GraphConfig(String id,
                       String name,
                       long pollingInterval,
                       ApplicationConfig appConfig,
                       List attributes){
        this.id = id;
        this.name = name;
        this.pollingInterval = pollingInterval;
        this.appConfig = appConfig;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPollingInterval() {
        return pollingInterval;
    }

    public List getAttributes() {
        return attributes;
    }

    public ApplicationConfig getAppConfig() {
        return appConfig;
    }

    /**
     * Output is of the format:
     * <p>
     * [applicationId/jmanage:name=Configuration/AppUptime],
     * [testApp2/jmanage:name=Configuration/AppUptime]
     * @return
     */
    public String getAttributesAsString() {
        StringBuffer graphAttributes = new StringBuffer();
        for(Iterator it=attributes.iterator(); it.hasNext();){
            if(graphAttributes.length() > 0){
                graphAttributes.append(",");
            }
            GraphAttributeConfig attrConfig = (GraphAttributeConfig)it.next();
            // todo: it will be better to use Expression object for this purpose
            graphAttributes.append("[");
            graphAttributes.append(appConfig.getName());
            graphAttributes.append("/");
            graphAttributes.append("\"");
            graphAttributes.append(attrConfig.getMBean());
            graphAttributes.append("\"");
            graphAttributes.append("/");
            graphAttributes.append(attrConfig.getAttribute());
            graphAttributes.append("]");
        }
        return graphAttributes.toString();
    }

    /**
     * output if of the form
     * @return
     */
    public String getAttributeDisplayNames(){
        StringBuffer displayNames = new StringBuffer();
        for(Iterator it=attributes.iterator(); it.hasNext();){
            if(displayNames.length() > 0){
                displayNames.append("|");
            }
            GraphAttributeConfig attrConfig = (GraphAttributeConfig)it.next();
            displayNames.append(attrConfig.getDisplayName());
        }
        return displayNames.toString();
    }
}
