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

package org.jmanage.webui.listener;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jmanage.core.alert.AlertEngine;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.JManageProperties;
import org.jmanage.core.util.Loggers;
import org.jmanage.monitoring.data.collector.ObservedMBeanAttributeCache;
import org.jmanage.monitoring.downtime.ApplicationDowntimeService;
import org.jmanage.util.db.DBUtils;

/**
 * Used for initialization and cleanup processes.
 * 
 * @author shashank
 *
 */
public class JManageApplicationListener implements javax.servlet.ServletContextListener{
	private Logger logger = Loggers.getLogger(JManageApplicationListener.class);

	/**
	 * jManage related resource cleanups done here.
	 */
	public void contextDestroyed(ServletContextEvent applicationEvent) {
		logger.info("jManage shutting down...");
		DBUtils.shutdownDB();		
	}

	/**
	 * jManage configurations are initialized here.
	 */
	public void contextInitialized(ServletContextEvent applicationEvent) {
		logger.info("jManage initializing...");
		String rootDirAbsPath = System.getProperty("JMANAGE_ROOT");

		try{
			ServletContext appContext = applicationEvent.getServletContext();
			String metadataDir =  appContext.getInitParameter("metadata-dir");
			String metadataDirAbsPath = appContext.getRealPath(metadataDir);

			File configDir = new File(rootDirAbsPath + File.separator + "config");
			File configSrcDir = new File(metadataDirAbsPath + File.separator + "config");
			CoreUtils.copyConfig(configDir, configSrcDir);

			String dataFormatConfigSysProp = appContext.getInitParameter("jmanage-data-formatConfig");
			System.setProperty(dataFormatConfigSysProp, 
					rootDirAbsPath+File.separatorChar+"config"+File.separatorChar+"html-data-format.properties");

			String jaasConfigSysProp = appContext.getInitParameter("jaas-config");
			System.setProperty(jaasConfigSysProp, 
					rootDirAbsPath+File.separatorChar+"config"+File.separatorChar+"jmanage-auth.conf");

			CoreUtils.init(rootDirAbsPath, metadataDirAbsPath);

			/* read jmanage.properties */
			JManageProperties jmProp = JManageProperties.getInstance();
			String serverIndicator = System.getProperty("SERVER.IND");
			String sPassword = jmProp.getProperty("jManage.password");
			char[] password = sPassword != null ? sPassword.toCharArray() : null;

			ServiceFactory.init(ServiceFactory.MODE_LOCAL);
			if(!"JETTY".equals(serverIndicator)){
				/* initialize Crypto */
				Crypto.init(password);
				/* clear the password */
				Arrays.fill(password, ' ');
			}

			/* Initialize DBUtils */
			DBUtils.init();
			/* Start AlertEngine */
			AlertEngine.getInstance().start();
			/* Initialize ObservedMBeanAttributeCache */
			ObservedMBeanAttributeCache.init();
			/* Start threads to monitor configured applications */
			ApplicationDowntimeService.getInstance().start();
			logger.info("jManage initialization complete...");
		}catch(Throwable e){
			logger.log(Level.SEVERE, "Error initializing jManage.", e);
		}
	}
}