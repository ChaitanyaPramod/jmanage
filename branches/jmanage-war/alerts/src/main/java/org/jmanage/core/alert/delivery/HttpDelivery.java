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

package org.jmanage.core.alert.delivery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.jmanage.core.alert.AlertDelivery;
import org.jmanage.core.alert.AlertInfo;
import org.jmanage.core.util.Loggers;


public class HttpDelivery implements AlertDelivery {

	private static final Logger logger = Loggers.getLogger(HttpDelivery.class);

	public void deliver(AlertInfo alertInfo) {
		
		try {
			// Construct data
			String data = URLEncoder.encode(getContent(alertInfo), "UTF-8");
			
			// Send data
			URL url = new URL(alertInfo.getEmailAddress());
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = rd.readLine()) != null) {
          // Process line...
      }
      wr.close();
      rd.close();

		} catch (Exception e) {
			logger.severe("Error sending http. Error: " + e.getMessage());
		}
	}

	private String getContent(AlertInfo alertInfo) {

		String postMessage = "";

		postMessage += "Timestamp=" + alertInfo.getFormattedTimeStamp();
		postMessage += "&AppName=" + alertInfo.getApplicationName();
		postMessage += "&AlertName=" + alertInfo.getAlertName();
		postMessage += "&Message=" + alertInfo.getMessage();
		postMessage += "&Source=" + alertInfo.getObjectName();

		return postMessage;
	}
}
