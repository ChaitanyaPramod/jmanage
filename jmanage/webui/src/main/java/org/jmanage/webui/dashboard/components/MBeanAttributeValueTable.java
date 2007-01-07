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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.jmanage.core.management.ObjectAttribute;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.webui.dashboard.framework.BaseDashboardComponent;
import org.jmanage.webui.dashboard.framework.DashboardContext;
/**
 * @author Shashank Bellary
 * Date: Jan 06, 2007
 */

public class MBeanAttributeValueTable extends BaseDashboardComponent {

    private ObjectName objectName;
    private String attributes;
    private String displayNames;
    private String objectNameFilter;

    protected void drawInternal(DashboardContext context, StringBuffer output) {
    	output.append("<table class=\"plaintext\" cellspacing=\"5\" style=\"border:1;border-style:solid;border-width:1px;border-color:#C0C0C0\">");
        ServerConnection connection = context.getWebContext().getServerConnection();
        Set<ObjectName> objects = connection.queryNames(objectName);
        StringTokenizer stAttributes = new StringTokenizer(attributes, "|");
        StringTokenizer stDispNames = new StringTokenizer(displayNames, "|");
        assert stAttributes.countTokens() == stDispNames.countTokens() : "Invalid component configuration";
        String[] attribs = new String[stAttributes.countTokens()];
    	output.append("<tr>");
        for(int ctr=0; stAttributes.hasMoreTokens(); ctr++){
        	attribs[ctr] = stAttributes.nextToken();
        	output.append("<td><b>").append(stDispNames.nextToken()).append("</b></td>");
        }
    	output.append("</tr>");
    	String objectNamePattern = objectName.getDisplayName();
    	objectNamePattern = objectNamePattern.endsWith("*") ? 
    			objectNamePattern.substring(0, objectNamePattern.length()-1) : objectNamePattern;
    	objectNamePattern += objectNameFilter.endsWith("*") ? 
    			objectNameFilter.substring(0, objectNameFilter.length() -1) : objectNameFilter;
        for(ObjectName anObjectName : objects){
        	if(!anObjectName.getDisplayName().startsWith(objectNamePattern))
        		continue;
        	output.append("<tr>");
            List attributeValues = connection.getAttributes(anObjectName, attribs);
            for(Iterator it = attributeValues.iterator(); it.hasNext();){
            	ObjectAttribute objAttribute = (ObjectAttribute)it.next();
            	output.append("<td>").append(objAttribute.getDisplayValue()).append("</td>");            	
            }
            output.append("</tr>");
        }
        output.append("</table>");
	}

	protected void init(Map<String, String> properties) {
        try {
            objectName = new ObjectName(properties.get(MBEAN_PATTERN));
            attributes = properties.get(ATTRIBUTES);
            displayNames = properties.get(DISPLAY_NAMES);
            objectNameFilter = properties.get(OBJECT_NAME_FILTER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

}
