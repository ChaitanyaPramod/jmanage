package org.jmanage.webui.forms;

/**
 *
 * date:  Jun 27, 2004
 * @author	Rakesh Kalra
 */
public class WeblogicApplicationForm extends ApplicationForm {

    private String serverName;

    public String getServerName(){
        return serverName;
    }

    public void setServerName(String serverName){
        this.serverName = serverName;
    }
}
