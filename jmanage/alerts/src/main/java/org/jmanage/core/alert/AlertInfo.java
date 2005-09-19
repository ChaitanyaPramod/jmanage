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
package org.jmanage.core.alert;

import org.jmanage.core.management.ObjectNotification;
import org.jmanage.core.config.AlertConfig;
import org.jmanage.core.config.ApplicationConfig;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertInfo {

    // unique id for the alert
    private String alertId = null;
    private String type = null;
    private long sequenceNumber = 0;
    private String message = null;
    private long timeStamp;
    private Object userData = null;
    private String source = null;

    private String alertConfigId;
    private String alertName;
    private String subject;
    private String emailAddress;

    private String appId;
    private String appName;

    public AlertInfo(){}

    public AlertInfo(ObjectNotification notification){
        // todo: figure out a better way to generate unique alert ids
        setAlertId(notification.getType() + System.currentTimeMillis());
        setType(notification.getType());
        setSequenceNumber(notification.getSequenceNumber());
        setMessage(notification.getMessage());
        setTimeStamp(notification.getTimeStamp());
        setUserData(notification.getUserData());
        if(notification.getMySource() != null)
            setSource(notification.getMySource().toString());
    }

    public void setAlertConfig(AlertConfig alertConfig) {
        this.alertConfigId = alertConfig.getAlertId();
        this.alertName = alertConfig.getAlertName();
        this.subject = alertConfig.getSubject();
        this.emailAddress = alertConfig.getEmailAddress();
        ApplicationConfig appConfig =
                alertConfig.getAlertSourceConfig().getApplicationConfig();
        this.setApplicationId(appConfig.getApplicationId());
        this.setApplicationName(appConfig.getName());
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAlertConfigId() {
        return alertConfigId;
    }

    public void setAlertConfigId(String alertConfigId) {
        this.alertConfigId = alertConfigId;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getApplicationId() {
        return appId;
    }

    public void setApplicationId(String appId) {
        this.appId = appId;
    }

    public String getApplicationName() {
        return appName;
    }

    public void setApplicationName(String appName) {
        this.appName = appName;
    }
}
