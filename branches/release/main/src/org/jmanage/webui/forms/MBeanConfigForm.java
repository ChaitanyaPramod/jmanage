package org.jmanage.webui.forms;

/**
 *
 * date:  Jul 21, 2004
 * @author	Rakesh Kalra
 */
public class MBeanConfigForm extends BaseForm {

    private String name;
    private String objectName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
