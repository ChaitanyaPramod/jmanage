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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.jmanage.core.config.*;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.webui.validator.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * date:  Jun 25, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ApplicationForm extends BaseForm {

    public static final String FORM_PASSWORD = "$$$$$$$$";

    private String appId;
    private String name;
    private String host;
    private String port;
    private String url;
    private String username;
    private String password;
    private String type;

    // jsr160 only
    private String jndiFactory;
    private String jndiURL;

    public String getApplicationId() {
        return appId;
    }

    public void setApplicationId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setJndiFactory(String jndiFactory){
        if(jndiFactory != null && jndiFactory.length() > 0)
            this.jndiFactory = jndiFactory;
    }

    public String getJndiFactory(){
        return jndiFactory;
    }

    public void setJndiURL(String jndiURL){
        if(jndiURL != null && jndiURL.length() > 0)
            this.jndiURL = jndiURL;
    }

    public String getJndiURL(){
        return jndiURL;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
        ActionErrors errors = super.validate(mapping, request);
        if(errors==null || errors.isEmpty()){
            if(name.indexOf("/") != -1){
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError(ErrorCodes.INVALID_CHAR_APP_NAME));
            }
            ApplicationType appType = ApplicationTypes.getApplicationType(type);
            MetaApplicationConfig metaAppConfig =
                    appType.getModule().getMetaApplicationConfig();
            if(metaAppConfig.isDisplayHost()){
                Validator.validateRequired(host, "Host Name", errors);
            }
            if(metaAppConfig.isDisplayPort()){
                if(Validator.validateRequired(port, "Port Number", errors)){
                    Validator.validateInteger(port, "Port Number", errors);
                }
            }
            if(metaAppConfig.isDisplayURL()){
                Validator.validateRequired(url, "URL", errors);
            }
        }
        return errors;
    }
}
