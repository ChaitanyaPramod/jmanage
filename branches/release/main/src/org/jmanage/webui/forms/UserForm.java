package org.jmanage.webui.forms;

/**
 * Date : Jul 27, 2004 11:40:54 PM
 * @author Shashank
 */
public class UserForm extends BaseForm{

    public static final String FORM_PASSWORD = "$$$$$$$$";

    private String username;
    private String password;
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
