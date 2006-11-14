/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.webui.forms;

import org.jmanage.core.auth.User;

/**
 * Date : Jul 27, 2004 11:40:54 PM
 * @author Shashank
 */
@SuppressWarnings("serial")
public class UserForm extends BaseForm{

    public static final String FORM_PASSWORD = "$$$$$$$$";

    private String username;
    private String password;
    private String confirmPassword;
    private String role;

    public String getStatus() {
        if(status == null){
            status = User.STATUS_ACTIVE;
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
