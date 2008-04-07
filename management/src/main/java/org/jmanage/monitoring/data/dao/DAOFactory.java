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
package org.jmanage.monitoring.data.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory for DAO objects.
 * 
 * @author rkalra
 */
public class DAOFactory {

	/* DAO Class to instance map */
	private static final Map<Class<? extends DAO>, DAO> daoClassToInstanceMap = 
		Collections.synchronizedMap(new HashMap<Class<? extends DAO>, DAO>());

	@SuppressWarnings("unchecked")
	public static <T extends DAO> T getDAO(Class<T> daoClass) {
		T daoInstance = (T) daoClassToInstanceMap.get(daoClass);
		if (daoInstance == null) {
			try {
				daoInstance = daoClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Error creating DAO instance for class: "
						+ daoClass.getName(), e);
			}
			daoClassToInstanceMap.put(daoClass, daoInstance);
		}
		return daoInstance;
	}
}
