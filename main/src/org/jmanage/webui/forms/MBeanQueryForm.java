package org.jmanage.webui.forms;

/**
 *
 * date:  Jun 17, 2004
 * @author	Rakesh Kalra
 */
public class MBeanQueryForm extends BaseForm {

    private String objectName = "*:*";

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
