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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.util.Loggers;
import org.jmanage.monitoring.data.dao.DAOFactory;
import org.jmanage.monitoring.data.dao.ObservedMBeanAttributeDAO;
import org.jmanage.monitoring.data.model.ObservedMBeanAttribute;

/**
 * A cache contains a mapping of ApplicationConfig to a list of ObservedMBean objects.
 * @author rkalra
 */
public class ObservedMBeanAttributeCache {

	private static final Logger logger = Loggers.getLogger(ObservedMBeanAttributeCache.class);
	private static Map<ApplicationConfig, Set<ObservedMBean>> appConfigToObservedMBeansMap;
	
	public static void init(){
		if(appConfigToObservedMBeansMap != null){
			throw new RuntimeException("Cache already initialized");
		}
		appConfigToObservedMBeansMap = new HashMap<ApplicationConfig, Set<ObservedMBean>>();
		ObservedMBeanAttributeDAO dao = DAOFactory.getDAO(ObservedMBeanAttributeDAO.class);
		List<ObservedMBeanAttribute> observedMBeanAttributes = dao.findAll();
		for(ObservedMBeanAttribute observedMBeanAttribute:observedMBeanAttributes){
			Set<ObservedMBean> observedMBeans = 
				appConfigToObservedMBeansMap.get(observedMBeanAttribute.getApplicationConfig());
			if(observedMBeans == null){
				observedMBeans = new HashSet<ObservedMBean>();
				appConfigToObservedMBeansMap.put(observedMBeanAttribute.getApplicationConfig(), 
						observedMBeans);
			}
			ObjectName objectName = new ObjectName(observedMBeanAttribute.getMBeanName());
			ObservedMBean observedMBean = find(observedMBeans, objectName);
			if(observedMBean == null){
				observedMBean = new ObservedMBean(objectName);
				observedMBeans.add(observedMBean);
			}
			observedMBean.addObservedMBeanAttribute(observedMBeanAttribute);
		}
		logger.log(Level.INFO, "ObservedMBeanAttributeCache initialized with {0} applications.", 
				appConfigToObservedMBeansMap.size());
	}

	public static Set<ObservedMBean> getObservedMBeans(ApplicationConfig appConfig){
		assert appConfigToObservedMBeansMap != null;
		return appConfigToObservedMBeansMap.get(appConfig);
	}
	
	/**
	 * @param observedMBeans
	 * @return
	 */
	private static ObservedMBean find(Set<ObservedMBean> observedMBeans, ObjectName objectName) {
		for(ObservedMBean observedMBean:observedMBeans){
			if(observedMBean.getObjectName().equals(objectName)){
				return observedMBean;
			}
		}
		return null;
	}
}
