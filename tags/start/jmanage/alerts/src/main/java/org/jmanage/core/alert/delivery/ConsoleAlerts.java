/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.core.alert.delivery;

import org.jmanage.core.alert.AlertInfo;
import org.jmanage.core.util.CoreUtils;

import java.util.*;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

/**
 *
 * Date:  Aug 2, 2005
 * @author	Rakesh Kalra
 */
public class ConsoleAlerts {

    private static final String CONSOLE_ALERTS_FILE =
            CoreUtils.getDataDir() + File.separator + "console-alerts.xml";

    private static Map alerts = Collections.synchronizedMap(new HashMap());

    public static void add(AlertInfo alertInfo){
        Object prevValue = alerts.put(alertInfo.getAlertId(), alertInfo);
        assert prevValue == null;
        save();
    }

    public static void remove(String alertId){
        alerts.remove(alertId);
        save();
    }

    public static AlertInfo get(String alertId){
        return (AlertInfo)alerts.get(alertId);
    }

    public static Collection getAll(){
        return alerts.values();
    }

    public static void removeAll(){
        alerts.clear();
    }

    private static void save(){
        try {
            XMLEncoder encoder = new XMLEncoder(
                    new BufferedOutputStream(
                            new FileOutputStream(CONSOLE_ALERTS_FILE)));
            Map persistedAlerts = new HashMap();
            persistedAlerts.putAll(alerts);
            encoder.writeObject(persistedAlerts);
            encoder.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        // read from file when class is initialized
        read();
    }

    private static void read(){
        try {
            File file = new File(CONSOLE_ALERTS_FILE);
            if(file.exists()){
                XMLDecoder decoder = new XMLDecoder(
                                    new BufferedInputStream(
                                        new FileInputStream(CONSOLE_ALERTS_FILE)));
                Map persistedAlerts = (Map)decoder.readObject();
                alerts.putAll(persistedAlerts);
                decoder.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
