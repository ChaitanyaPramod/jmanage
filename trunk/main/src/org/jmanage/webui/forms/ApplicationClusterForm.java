/*
 * Copyright 2000-2004 by Upromise Inc.
 * 117 Kendrick Street, Suite 200, Needham, MA, 02494, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Upromise, Inc. ("Confidential Information").  You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms of an agreement between you and Upromise.
 */
package org.jmanage.webui.forms;

/**
 *
 * date:  Oct 17, 2004
 * @author	Rakesh Kalra
 */
public class ApplicationClusterForm extends BaseForm {

    private String appId;
    private String name;
    private String[] childApplicationIds;
    private String[] standAloneApplicationIds;
    private String selectedChildApplications;

    public String getApplicationId() {
        return appId;
    }

    public void setApplicationId(String appId) {
        if(appId != null && appId.length() > 0)
            this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getChildApplicationIds() {
        return childApplicationIds;
    }

    public void setChildApplicationIds(String[] childApplicationIds) {
        this.childApplicationIds = childApplicationIds;
    }

    public String[] getStandAloneApplicationIds() {
        return standAloneApplicationIds;
    }

    public void setStandAloneApplicationIds(String[] standAloneApplicationIds) {
        this.standAloneApplicationIds = standAloneApplicationIds;
    }

    public String getSelectedChildApplications() {
        return selectedChildApplications;
    }

    public void setSelectedChildApplications(String selectedChildApplications) {
        this.selectedChildApplications = selectedChildApplications;
    }
}
