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
package org.jmanage.monitoring.data.model;

import org.jmanage.core.config.ApplicationConfig;
import java.util.Date;

/**
 * @author rkalra
 * 
 */
public class ObservedMBeanAttribute {

	private long id;

	private ApplicationConfig appConfig;

	private String mbeanName;

	private String attributeName;
	
	private Date whenStarted;
	
	private String displayName;

	public ObservedMBeanAttribute(long id, ApplicationConfig appConfig,
			String mbeanName, String attributeName, Date whenStarted, String  displayName) {
		this(appConfig, mbeanName, attributeName, whenStarted, displayName);
		this.id = id;
	}

	public ObservedMBeanAttribute(ApplicationConfig appConfig,
			String mbeanName, String attributeName, Date whenStarted, String  displayName) {
		this.appConfig = appConfig;
		this.mbeanName = mbeanName;
		this.attributeName = attributeName;
		this.whenStarted = whenStarted;
		this.displayName = displayName;
	}

	public long getId() {
		return id;
	}

	public ApplicationConfig getApplicationConfig() {
		return appConfig;
	}

	public String getMBeanName() {
		return mbeanName;
	}

	public String getAttributeName() {
		return attributeName;
	}
	
	public Date getWhenStarted() {
		return whenStarted;
	}

	public String getDisplayName() {
		return displayName;
	}
}
