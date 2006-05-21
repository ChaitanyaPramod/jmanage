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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.config.DashboardComponent;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.util.display.html.Select;

/**
 *
 * Example config:
 * 
 *    <property name="mbean" value="java.lang:type=Threading"/>
 *    <property name="attribute" value="AllThreadIds"/>
 *    <property name="idResolverMBean" value="java.lang:type=Threading"/>
 *    <property name="idResolverOperation" value="getThreadInfo"/>
 *    <property name="idResolverName" value="threadName"/>
 *    
 * @author Rakesh Kalra
 */
public class SelectList implements DashboardComponent {

    private static final String MBEAN = "mbean";
    private static final String ATTRIBUTE = "attribute";
    // optional
    private static final String ID_RESOLVER_MBEAN = "idResolverMBean";
    private static final String ID_RESOLVER_OPERATION = "idResolverOperation";
    private static final String ID_RESOLVER_NAME = "idResolverName";
    
    private static final String SIZE = "size";
    
    private String id;
    
    private String mbean;
    private String attribute;
    
    private String idResolverMBean;
    private String idResolverOperation;
    private String idResolverName;
    
    private int size = 10; //default value
    
    public String getId() {
        return id;
    }

    public void init(Element componentConfig) {
        id = componentConfig.getAttribute(ID).getValue();
        Map<String, String> properties = getProperties(componentConfig);
        mbean = properties.get(MBEAN);
        assert mbean != null;
        attribute = properties.get(ATTRIBUTE);
        assert attribute != null;
        idResolverMBean = properties.get(ID_RESOLVER_MBEAN);
        idResolverOperation = properties.get(ID_RESOLVER_OPERATION);
        idResolverName = properties.get(ID_RESOLVER_NAME);
        if(properties.get(SIZE) != null)
            size = Integer.parseInt(properties.get(SIZE));
    }
    
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

    public String draw(String applicationName) {
        Map<String, String> data = getData(applicationName);
        Select select = new Select("dummy", size, true);
        for(String id:data.keySet()){
            select.addOption(id, data.get(id));
        }
        return select.draw();
    }
    
    private Map<String, String> getData(String applicationName){
        Map<String, String> data = new HashMap<String, String>();
        ServerConnection serverConnection = getServerConnection(applicationName); 
        Object value = serverConnection.getAttribute(new ObjectName(mbean), attribute);
        if(value.getClass().isArray()){
            for(int i=0; i<Array.getLength(value); i++){
                String id = Array.get(value, i).toString();
                data.put(id, id); // TODO: use idresolver 
            }
        }else{
            assert false: "List, etc., not yet implemented";
        }
        return data;
    }
    
    private ServerConnection getServerConnection(String appName){
        ApplicationConfig appConfig = ApplicationConfigManager.getApplicationConfigByName(appName);
        return ServerConnector.getServerConnection(appConfig);
    }
    
}
