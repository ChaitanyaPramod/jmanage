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
import org.apache.commons.validator.GenericValidator;
import org.jmanage.core.util.ErrorCodes;
import org.jmanage.webui.util.RequestParams;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * date:  Jul 21, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class MBeanConfigForm extends BaseForm {
    //TODO: Usage of DynaForm should clean up this.
    private final String nameMask = "^[a-zA-Z0-9\\s-\\$\\.]*$";
    private String[] name;
    private String objectName;
    private boolean applicationCluster;

    public final ActionErrors validate(ActionMapping mapping,
                                       HttpServletRequest request) {
        if(request.getParameter(RequestParams.MULTI_MBEAN_CONFIG) != null){
            boolean validValue = false, invalidValue = false, nullValue = false;
            final String[] objectNames = request.getParameterValues("name");
            for(int mbeanCtr=0; mbeanCtr < objectNames.length; mbeanCtr++ ){
                final String configName = request.getParameter(objectNames[mbeanCtr]);
                if(GenericValidator.isBlankOrNull(configName)){
                    nullValue = true;
                }else{
                    if(!GenericValidator.matchRegexp(configName, nameMask)){
                        invalidValue = true;
                    }else{
                        validValue = true;
                    }
                }
            }
            ActionErrors errors = new ActionErrors();
            if(invalidValue){
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError(ErrorCodes.INVALID_CHAR_APP_NAME));
                return errors;
            }else if(nullValue && !validValue){
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("errors.required", "application name"));
                return errors;
            }
            return null;
        }else{
            final String configName = request.getParameter("name");
            ActionErrors errors = new ActionErrors();
            if(GenericValidator.isBlankOrNull(configName)){
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("errors.required", "application name"));
                return errors;
            }else if(!GenericValidator.matchRegexp(configName, nameMask)){
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError(ErrorCodes.INVALID_CHAR_APP_NAME));
                return errors;
            }else{
                return null;
            }
        }
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public boolean isApplicationCluster(){
        return applicationCluster;
    }

    public void setApplicationCluster(boolean applicationCluster){
        this.applicationCluster = applicationCluster;
    }
}
