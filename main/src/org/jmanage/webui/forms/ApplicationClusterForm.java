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
