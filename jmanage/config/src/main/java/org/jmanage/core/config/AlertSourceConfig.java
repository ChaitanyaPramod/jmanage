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
package org.jmanage.core.config;

/**
 * Specifies a source of an alert. Alert source could be one of:
 * <li> MBean Notification
 * <li> Attribute Value changes
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class AlertSourceConfig {

    public static final String SOURCE_TYPE_NOTIFICATION = "notification";
    public static final String SOURCE_TYPE_GAUGE_MONITOR = "gauge";
    public static final String SOURCE_TYPE_COUNTER_MONITOR = "counter";
    public static final String SOURCE_TYPE_STRING_MONITOR = "string";

    private String sourceType;
    private ApplicationConfig appConfig;
    private String objectName;

    /* for notification */
    private String notificationType;

    /* for attribute change */
    // attr name
    private String attributeName;
    private Number lowThreshold;
    private Number highThreshold;
    private String stringAttributeValue;
    private String attributeDataTYpe;

    public AlertSourceConfig(String objectName, String notificationType){
        this.sourceType = SOURCE_TYPE_NOTIFICATION;
        this.objectName = objectName;
        this.notificationType = notificationType;
    }

    public AlertSourceConfig(String objectName, String attributeName,
                             Number minValue, Number maxValue,
                             String attributeDataType){
        this.sourceType = SOURCE_TYPE_GAUGE_MONITOR;
        this.objectName = objectName;
        this.attributeName = attributeName;
        this.lowThreshold = minValue;
        this.highThreshold = maxValue;
        this.attributeDataTYpe = attributeDataType;
    }

    public AlertSourceConfig(String objectName, String attributeName,
                             String stringAttributeValue){
        this.sourceType = SOURCE_TYPE_STRING_MONITOR;
        this.objectName = objectName;
        this.attributeName = attributeName;
        this.stringAttributeValue = stringAttributeValue;
    }

    /* todo: enable if this could be useful - rk
    // alert on any change to the attribute value
    //private Boolean anyChange;

    public AlertSourceConfig(String objectName, String attributeName,
                             boolean anyChange){
        this.sourceType = SOURCE_TYPE_ATTRIBUTE_CHANGE;
        this.objectName = new ObjectName(objectName);
        this.attributeName = attributeName;
        assert anyChange == true;
        this.anyChange = Boolean.TRUE;
    }
    */

    public String getSourceType() {
        return sourceType;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Number getLowThreshold() {
        return lowThreshold;
    }

    public Number getHighThreshold() {
        return highThreshold;
    }

    public String getStringAttributeValue(){
        return stringAttributeValue;
    }

    public void setApplicationConfig(ApplicationConfig appConfig) {
        this.appConfig = appConfig;
    }

    public ApplicationConfig getApplicationConfig(){
        return appConfig;
    }
    public String getAttributeDataTYpe() {
            return attributeDataTYpe;
        }

}
