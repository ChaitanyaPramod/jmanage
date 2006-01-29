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
package org.jmanage.webui.validator;

import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Date: Nov 4, 2004 12:33:58 PM
 * @author Shashank Bellary 
 */
public class JManageFieldChecks implements Serializable{

    /**
     * This method compares two specified fields for equality.
     * @param bean
     * @param validatorAction
     * @param field
     * @param errors
     * @param request
     * @return
     */
    public static boolean validateMatch(Object bean,
                                        ValidatorAction validatorAction,
                                        Field field, ActionErrors errors,
                                        HttpServletRequest request) {

        String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);

        if (!GenericValidator.isBlankOrNull(value) &&
                !GenericValidator.isBlankOrNull(value2) &&
                !value.equals(value2)) {
            errors.add(field.getKey(),
                    Resources.getActionError(request, validatorAction,
                            field));
            return false;
        }
        return true;
    }
}

