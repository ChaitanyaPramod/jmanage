/**
 * jManage - Open Source Application Management
 * Copyright (C) 2004-2006 jManage.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 */

package org.jmanage.webui.dashboard.components;

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
		String objectNamePattern = null;
		if(objectNameFilter != null){
			objectNamePattern = objectName.getDisplayName();
			objectNamePattern = objectNamePattern.endsWith("*") ? 
					objectNamePattern.substring(0, objectNamePattern.length()-1) : objectNamePattern;
					objectNamePattern += objectNameFilter.endsWith("*") ? 
							objectNameFilter.substring(0, objectNameFilter.length() -1) : objectNameFilter;
		}
		for(ObjectName anObjectName : objects){
			if(objectNamePattern != null && !anObjectName.getDisplayName().startsWith(objectNamePattern))
				continue;
			output.append("<tr>");
			List<ObjectAttribute> attributeValues = connection.getAttributes(anObjectName, attribs);
			for(ObjectAttribute objAttribute : attributeValues){
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
