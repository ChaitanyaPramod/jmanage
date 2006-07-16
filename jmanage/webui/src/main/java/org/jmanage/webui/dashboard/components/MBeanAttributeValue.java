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

import org.jmanage.webui.dashboard.framework.BaseDashboardComponent;
import org.jmanage.webui.dashboard.framework.DashboardContext;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectAttribute;

import java.util.Map;

/**
 * Date: Jun 18, 2006 4:50:59 PM
 *
 * @ author: Shashank Bellary
 */
public class MBeanAttributeValue extends BaseDashboardComponent{
    
    private ObjectName objectName;
    private String attribute;
    private String displayName;

    /**
     *
     * @param properties
     */
    public void init(Map<String, String> properties) {
        try {
            objectName = new ObjectName(properties.get(MBEAN));
            attribute = properties.get(ATTRIBUTE);
            displayName = properties.get(DISPLAY_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param context
     */
    protected void drawInternal(DashboardContext context, StringBuffer output) {
        ServerConnection connection = context.getWebContext().getServerConnection();
        ObjectAttribute attributeValue =
                (ObjectAttribute)connection.getAttributes(objectName, new String[]{attribute}).get(0);

        String data= displayName != null && !displayName.equals("") ?
                "<b>" +displayName+" : </b>"+ attributeValue.getDisplayValue() :
                attributeValue.getDisplayValue();
        output.append(data);
    }
}