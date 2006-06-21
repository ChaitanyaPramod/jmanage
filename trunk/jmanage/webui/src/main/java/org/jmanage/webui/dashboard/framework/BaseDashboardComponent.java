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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jdom.Element;
import org.jmanage.core.util.Loggers;

/**
 * This dashboard component has simple property name/value based configuration. The
 * XML looks like the following:<br/>
 *    &lt;property name="mbean" value="java.lang:type=Threading"/&gt;<br/>
 *    &lt;property name="attribute" value="AllThreadIds"/&gt;<br/>
 * <p>
 * It parses the component configration and makes the information available as a Map.
 * <p>
 * It also adds support for event support.
 * 
 * @author Rakesh Kalra
 */
public abstract class BaseDashboardComponent implements DashboardComponent {

    private static final Logger logger = Loggers.getLogger(BaseDashboardComponent.class);
    
    private String id;
    private Event event;
    private Properties properties;
    
    public String getId() {
        return id;
    }
    
    // <event source="com2" name="onSelect" dataVariable="threadId"/>
    public void init(Element componentConfig) {
        id = componentConfig.getAttribute(ID).getValue();
   
        /* initialize properties */
        properties = new Properties(componentConfig);
        init(properties);
        
        /* parse any event element */
        Element eventElement = componentConfig.getChild("event");
        if(eventElement != null){
            event = new Event(eventElement);
        }
    }
    
    protected abstract void init(Map<String, String> properties);
    
    public final String draw(DashboardContext context) {
        
        String applicationId = context.getWebContext().getApplicationConfig().getApplicationId();
        StringBuffer output = new StringBuffer();
        if(event != null){
            output.append("<script>");
            output.append("addEventHandler(''" + event.source + "'', ''" + 
                    event.name + "'', ''" + getId() + "'', ''" + event.dataVariable + "'', ''" + 
                    applicationId + "'')");
            output.append("</script>");
        }

        // wrap wih div tag
        output.append("<div id=\"" + getId() + "\">");
        try{
            properties.context = context;
            if(!properties.hasUnresolvedVariable())
                drawInternal(context, output);
        }finally{
            properties.context = null;
        }
        output.append("</div>");

        return output.toString();
    }
    
    protected abstract void drawInternal(DashboardContext context, StringBuffer output);
        
    private class Event{
        String source;
        String name;
        String dataVariable;
        Event(Element eventElement){
            this.source = eventElement.getAttributeValue("source");
            this.name = eventElement.getAttributeValue("name");
            this.dataVariable = eventElement.getAttributeValue("dataVariable");
        }
    }

    @SuppressWarnings("serial")
    private class Properties extends HashMap<String, String>{
         
        DashboardContext context;
        
        Properties(Element componentConfig){
            parseProperties(componentConfig);
        }
        
        public String get(Object propertyName){
            String value = super.get(propertyName);
            return resolveVariable(value);
        }
        
        public boolean hasUnresolvedVariable(){
            for(String value:this.values()){
                if(value != null && resolveVariable(value) == null)
                    return true;
            }
            return false;
        }
        
        /**
         * 
         * @return null if the variable can't be resolved
         */
        private String resolveVariable(String value){
            if(value != null && value.startsWith("${")){
                assert value.endsWith("}");
                String variable = value.substring(2, value.length() - 1);
                value = context.getVariableValue(variable);
                logger.fine("Resolving variable:" + variable + " to value:" + value);
            }
            return value;
        }
        
        @SuppressWarnings("unchecked")
        private void parseProperties(Element componentConfig){
            List<Element> propertyElements = componentConfig.getChildren("property");
            for(Element property: propertyElements){
                String name = property.getAttribute("name").getValue();
                String value = property.getAttribute("value").getValue();
                this.put(name, value);
            }
        }
    }
}
