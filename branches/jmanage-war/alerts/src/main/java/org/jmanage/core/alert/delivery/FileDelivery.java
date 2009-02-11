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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jmanage.core.alert.AlertDelivery;
import org.jmanage.core.alert.AlertInfo;

public class FileDelivery implements AlertDelivery {

	public void deliver(AlertInfo alertInfo) {
		String path = alertInfo.getEmailAddress() + File.pathSeparator
				+ alertInfo.getAlertId();
		write(path, getContent(alertInfo));

	}

	private String getContent(AlertInfo alertInfo) {
		StringBuffer buff = new StringBuffer();
		buff.append("Timestamp: ");
		buff.append(alertInfo.getFormattedTimeStamp());
		buff.append("\n");
		buff.append("Application Name: ");
		buff.append(alertInfo.getApplicationName());
		buff.append("\n");
		buff.append("Alert Name: ");
		buff.append(alertInfo.getAlertName());
		buff.append("\n");
		buff.append("Message: ");
		buff.append(alertInfo.getMessage());
		buff.append("\n");
		buff.append("Source: ");
		buff.append(alertInfo.getObjectName() + "\n");
		return buff.toString();
	}

	public static void write(String filename, String data) {

		try {
			OutputStream out = new FileOutputStream(filename);
			out.write(data.getBytes());
			out.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
