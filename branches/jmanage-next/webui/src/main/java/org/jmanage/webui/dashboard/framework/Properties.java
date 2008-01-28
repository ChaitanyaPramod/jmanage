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

import org.jdom.Element;
import org.jmanage.core.util.Loggers;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Date: Jun 21, 2006 7:15:52 PM
 *
 * @author Shashank Bellary
 */
public class Properties extends HashMap<String, String> {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Loggers.getLogger(Properties.class);
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
