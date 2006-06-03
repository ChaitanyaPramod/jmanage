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

import java.util.Map;
import java.util.Random;

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

    private String mbean;
    private String operation;
    private String[] signature;
    private Object[] params;
    
    @Override
    // TODO: implement
    public void init(Map<String, String> properties) {
        
    }

    @Override
    // TODO: implement
    public String draw(String applicationName) {
        return Integer.toString(new Random().nextInt());
    }

}
