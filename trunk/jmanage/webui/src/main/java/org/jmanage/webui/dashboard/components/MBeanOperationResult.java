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
package org.jmanage.webui.dashboard.components;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.data.OperationResultData;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.webui.dashboard.framework.DashboardContext;

/**
 *
 *    <property name="mbean" value="java.lang:type=Threading"/>
 *    <property name="operation" value="getThreadInfo"/>
 *    <property name="param1" value="1"/>
 *    <property name="type1" value="long"/>
 *    <property name="param2" value="10"/>
 *    <property name="type2" value="int"/>
 * @author Rakesh Kalra
 */
public class MBeanOperationResult extends PropertiesDashboardComponent {

    private static final String MBEAN = "mbean";
    private static final String OPERATION = "operation";
    private static final String PARAM = "param";
    private static final String TYPE = "type";
    
    private ObjectName objectName;
    private String operation;
    private String[] signature;
    private String[] params;
    
    @Override
    // TODO: implement
    public void init(Map<String, String> properties) {
        
        try {
            objectName = new ObjectName(properties.get(MBEAN));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        operation = properties.get(OPERATION);
        List<String> sigList = new LinkedList<String>();
        int i = 1;
        while(true){
            if(properties.containsKey(TYPE + i)){
                sigList.add(properties.get(TYPE + i));
                i ++;
            }else{
                break;
            }
        }
        signature = new String[sigList.size()];
        params = new String[sigList.size()];
        int index = 0;
        for(String type: sigList){
            signature[index] = type;
            params[index] = properties.get(PARAM + (index + 1));
            index ++;
        }
    }

    public String draw(DashboardContext context) {
        
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        ServiceContext srvcContext = context.getWebContext().getServiceContext();
        OperationResultData[] operationResult = 
            mbeanService.invoke(srvcContext, objectName, operation, params, signature);
        assert operationResult.length == 1;
        assert !operationResult[0].isError();
        return "<pre>" + operationResult[0].getDisplayOutput() + "</pre>";
    }
}
