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
package org.jmanage.core.dashboard.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jmanage.core.config.DashboardComponent;

/**
 * This dashboard component has simple property name/value based configuration. The
 * XML looks like the following:<br/>
 *    &lt;property name="mbean" value="java.lang:type=Threading"/&gt;<br/>
 *    &lt;property name="attribute" value="AllThreadIds"/&gt;<br/>
 * <p>
 * 
 * It parses the component configration and makes the information available as a Map.
 * 
 * @author Rakesh Kalra
 */
public abstract class PropertiesDashboardComponent implements DashboardComponent {

    private String id;
    
    public String getId() {
        return id;
    }
    
    public final void init(Element componentConfig) {
        id = componentConfig.getAttribute(ID).getValue();
        init(getProperties(componentConfig));
    }
    
    public abstract void init(Map<String, String> properties);
    public abstract String draw(String applicationName);
    
    @SuppressWarnings("unchecked")
    private Map<String, String> getProperties(Element componentConfig){
        List<Element> propertyElements = componentConfig.getChildren("property");
        Map<String, String> properties = new HashMap<String, String>();
        for(Element property: propertyElements){
            String name = property.getAttribute("name").getValue();
            String value = property.getAttribute("value").getValue();
            properties.put(name, value);
        }
        return properties;
    }
}
