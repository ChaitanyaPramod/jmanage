package org.jmanage.webui.forms;

/**
 *
 * date:  Dec 27, 2004
 * @author	Vandana Taneja
 */
public class ConfigureForm extends BaseForm {

    int maxLoginAttempts;

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }
}
