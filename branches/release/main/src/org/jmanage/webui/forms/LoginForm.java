package org.jmanage.webui.forms;

/**
 *
 * date:  Jun 27, 2004
 * @author	Rakesh Kalra
 */
public class LoginForm extends BaseForm {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
