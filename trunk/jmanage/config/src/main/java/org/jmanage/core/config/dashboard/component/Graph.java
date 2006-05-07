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

package org.jmanage.core.config.dashboard.component;

import org.jmanage.core.config.DashboardComponent;
import org.jdom.Element;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Date: May 2, 2006 7:36:53 PM
 *
 * @author Shashank Bellary
 */
public class Graph implements DashboardComponent {
    private String id, name, type;
    private int pollingIntervalInSeconds;
    private List<String> mbeans = new ArrayList<String>();
    private Map<String, List<String>> attributes =
            new HashMap<String, List<String>>();
    private Map<String, List<String>> attributeDisplayNames =
            new HashMap<String, List<String>>();

    /*  Constnats for component config XML element - Begin  */
    protected final String TYPE = "type";
    protected final String POLLING_INTERVAL = "pollingInterval";
    protected final String PARAM = "param";
    protected final String MBEAN = "mbean";
    protected final String ATTRIBUTE = "attribute";
    protected final String DISPLAY_NAME = "displayName";
    /*  Constnats for component config XML element - End    */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPollingIntervalInSeconds() {
        return pollingIntervalInSeconds;
    }

    public void init(Element componentConfig){
        id = componentConfig.getAttribute(ID).getValue();
        name = componentConfig.getAttribute(NAME).getValue();
        type = componentConfig.getChild(TYPE).getTextTrim();
        pollingIntervalInSeconds =
                Integer.parseInt(componentConfig.getChild(POLLING_INTERVAL).getTextTrim());
        List<Element> paramElements = componentConfig.getChildren(PARAM);
        for(Element param: paramElements){
            String mbean = param.getAttribute(MBEAN).getValue();
            String attribute = param.getAttribute(ATTRIBUTE).getValue();
            String attribDisplayName = param.getAttribute(DISPLAY_NAME).getValue();

            if(!mbeans.contains(mbean))
                mbeans.add(mbean);

            if(attributes.containsKey(mbean))
                attributes.get(mbean).add(attribute);
            else{
                List attribs = new ArrayList();
                attribs.add(attribute);
                attributes.put(mbean, attribs);
            }

            if(attributeDisplayNames.containsKey(mbean))
                attributeDisplayNames.get(mbean).add(attribDisplayName);
            else{
                List attribDisplayNames = new ArrayList();
                attribDisplayNames.add(attribDisplayName);
                attributeDisplayNames.put(mbean, attribDisplayNames);
            }
        }
    }

    public String draw(String applicationName) {
        return null;
    }
}