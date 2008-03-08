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

import java.util.Date;

/**
 * @author rkalra
 *
 */
public class ObservedMBeanAttributeValue {

	private Date whenCollected;
	private ObservedMBeanAttribute observedMBeanAttribute;
	private String value;
	
	public ObservedMBeanAttributeValue(ObservedMBeanAttribute observedMBeanAttribute, 
			String value){
		this.whenCollected = new Date();
		this.observedMBeanAttribute = observedMBeanAttribute;
		this.value = value;
	}

	public ObservedMBeanAttribute getObservedMBeanAttribute() {
		return observedMBeanAttribute;
	}

	public String getValue() {
		return value;
	}

	public Date getWhenCollected() {
		return whenCollected;
	}
}
