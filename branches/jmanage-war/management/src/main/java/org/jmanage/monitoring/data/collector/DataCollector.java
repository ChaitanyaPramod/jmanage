/**
 * jManage Application Management Platform 
 * Copyright 2004-2008 jManage.org
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 */
package org.jmanage.monitoring.data.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.util.Loggers;
import org.jmanage.monitoring.data.dao.ObservedMBeanAttributeDAO;
import org.jmanage.monitoring.data.model.ObservedMBeanAttribute;

/**
 * @author rkalra
 * 
 */
public class DataCollector {

	private static final Logger logger = Loggers.getLogger(DataCollector.class);
	
	/**
	 * @param appConfig
	 * @param connection
	 */
	public static void collect(ApplicationConfig appConfig, ServerConnection connection) {
		/* get a list of ObservedMBeanAttribute objects for the given ApplicationConfig */
		ObservedMBeanAttributeDAO dao = new ObservedMBeanAttributeDAO();
		List<ObservedMBeanAttribute> observedMBeanAttributes = dao.find(appConfig);
		if(observedMBeanAttributes.size() > 0){
			/* build a map of object name to attribute array mapping */
			Map<String, List<String>> mbeanNameToAttributesMap = 
				buildMBeanNameToAttributesMap(observedMBeanAttributes);
			for(Map.Entry<String, List<String>> entry:mbeanNameToAttributesMap.entrySet()){
				ObjectName objectName = new ObjectName(entry.getKey());
				String[] attributes = 
					entry.getValue().toArray(new String[entry.getValue().size()]);
				logger.log(Level.FINE, "Collection data for application: {0}, mbean: {1}, attributes:{2}", 
						new Object[]{appConfig.getName(), objectName, attributes});
				List objectAttributes = connection.getAttributes(objectName, attributes);
				
				// TODO: writeToDB() -- somehow need a handle to the ObservedMBeanAttribute
			}
		}
	}

	/**
	 * @param observedMBeanAttributes
	 * @return
	 */
	private static Map<String, List<String>> buildMBeanNameToAttributesMap(
			List<ObservedMBeanAttribute> observedMBeanAttributes) {
		Map<String, List<String>> mbeanNameToAttributesMap = new HashMap<String, List<String>>();
		for(ObservedMBeanAttribute attribute:observedMBeanAttributes){
			List<String> attributeNames = 
				mbeanNameToAttributesMap.get(attribute.getMBeanName());
			if(attributeNames == null){
				attributeNames = new ArrayList<String>();
				mbeanNameToAttributesMap.put(attribute.getMBeanName(), attributeNames);
			}
			attributeNames.add(attribute.getAttributeName());
		}
		return mbeanNameToAttributesMap;
	}
}