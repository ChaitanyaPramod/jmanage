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

/**
 * Date: Nov 4, 2004 12:33:58 PM
 * @author Shashank Bellary 
 */
public class JManageFieldChecks {

    private static final String FIELD_TEST_NULL = "NULL";
    private static final String FIELD_TEST_NOTNULL = "NOTNULL";
    private static final String FIELD_TEST_EQUAL = "EQUAL";
    private static final String FIELD_TEST_NOT_EQUAL = "NOTEQUAL";

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


    /**
     * Checks if the field isn't null based on the values of other fields.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>ActionErrors</code> object to add errors to if
     * any validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param request Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateRequiredIf(Object bean, ValidatorAction va,
                                             Field field, ActionErrors errors,
                                             org.apache.commons.validator.Validator validator,
                                             HttpServletRequest request) {
        Object form = validator.getResource(
                org.apache.commons.validator.Validator.BEAN_KEY);
        String value = null;
        boolean required = false;
        if(isString(bean)){
            value = (String)bean;
        }else{
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }
        int i = 0;
        String fieldJoin = "AND";
        if(!GenericValidator.isBlankOrNull(field.getVarValue("fieldJoin"))){
            fieldJoin = field.getVarValue("fieldJoin");
        }
        if(fieldJoin.equalsIgnoreCase("AND")){
            required = true;
        }
        while(!GenericValidator.isBlankOrNull(field.getVarValue("field"+i))){
            String dependProp = field.getVarValue("field"+i);
            String dependTest = field.getVarValue("fieldTest"+i);
            String dependTestValue = field.getVarValue("fieldValue"+i);
            String dependVal = null;
            boolean thisRequired = false;

            dependVal = ValidatorUtil.getValueAsString(form, dependProp);
            if(dependTest.equals(FIELD_TEST_NULL)){
                if((dependVal != null) && (dependVal.length() > 0)){
                    thisRequired = false;
                }else{
                    thisRequired = true;
                }
            }else if(dependTest.equals(FIELD_TEST_NOTNULL)){
                if((dependVal != null) && (dependVal.length() > 0)){
                    thisRequired = true;
                }else{
                    thisRequired = false;
                }
            }else if(dependTest.equals(FIELD_TEST_EQUAL)){
                thisRequired = dependTestValue.equalsIgnoreCase(dependVal);
            }else if(dependTest.equals(FIELD_TEST_NOT_EQUAL)){
                thisRequired = !dependTestValue.equalsIgnoreCase(dependVal);
            }

            if(fieldJoin.equalsIgnoreCase("AND")){
                required = required && thisRequired;
            }else{
                required = required || thisRequired;
            }
            i++;
        }
		if(required){
			if(GenericValidator.isBlankOrNull(value)){
                errors.add(field.getKey(),
                        Resources.getActionError(request, va, field));
                return false;
			}else{
				return true;
			}
		}
        return true;
    }

    /**
     *  Return <code>true</code> if the specified object is a String or a <code>null</code>
     *  value.
     *
     * @param o Object to be tested
     * @return The string value
     */
    protected static boolean isString(Object o) {
        return (o == null) ? true : String.class.isInstance(o);
    }
}