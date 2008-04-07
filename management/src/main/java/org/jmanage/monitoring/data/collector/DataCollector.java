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
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.management.ObjectAttribute;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.util.Loggers;
import org.jmanage.monitoring.data.dao.DAOFactory;
import org.jmanage.monitoring.data.dao.ObservedMBeanAttributeValueDAO;
import org.jmanage.monitoring.data.model.ObservedMBeanAttribute;
import org.jmanage.monitoring.data.model.ObservedMBeanAttributeValue;

/**
 * Collects values for configured MBean attributes and saves them to the database.
 * 
 * @author rkalra
 */
public class DataCollector {

	private static final Logger logger = Loggers.getLogger(DataCollector.class);

	/**
	 * @param appConfig
	 * @param connection
	 */
	public static void collect(ApplicationConfig appConfig, ServerConnection connection) {
		try {
			/*
			 * get a list of ObservedMBeanAttribute objects for the given ApplicationConfig
			 */
			Set<ObservedMBean> observedMBeans = ObservedMBeanAttributeCache
					.getObservedMBeans(appConfig);
			if (observedMBeans != null) {
				List<ObservedMBeanAttributeValue> observedMBeanAttributeValues = 
					new ArrayList<ObservedMBeanAttributeValue>();
				for (ObservedMBean observedMBean : observedMBeans) {
					if (logger.isLoggable(Level.FINE)) {
						logger.log(Level.FINE,
								"Collection data for application: {0}, mbean: {1}, attributes:{2}",
								new Object[] { appConfig.getName(), observedMBean.getObjectName(),
										observedMBean.getAttributeNames() });
					}
					List<ObjectAttribute> objectAttributes = connection.getAttributes(observedMBean
							.getObjectName(), observedMBean.getAttributeNames());
					int index = 0;
					for (ObjectAttribute objectAttribute : objectAttributes) {
						// TODO: what if it fails? -rk
						assert objectAttribute.getStatus() == ObjectAttribute.STATUS_OK;
						ObservedMBeanAttribute observedMBeanAttribute = observedMBean
								.getObservedMBeanAttributes()[index++];
						ObservedMBeanAttributeValue observedMBeanAttributeValue = 
							new ObservedMBeanAttributeValue(
								observedMBeanAttribute, objectAttribute.getDisplayValue()); // TODO:
						// Is
						// display
						// value
						// the
						// right
						// value
						// to
						// use?
						observedMBeanAttributeValues.add(observedMBeanAttributeValue);
					}

				}
				// save the values to the database
				DAOFactory.getDAO(ObservedMBeanAttributeValueDAO.class).save(observedMBeanAttributeValues);
			}
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error collecting data for application: "
					+ appConfig.getName(), t);
		}
	}
}