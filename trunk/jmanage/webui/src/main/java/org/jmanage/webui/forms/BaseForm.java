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
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * date:  Jun 17, 2004
 * @author	Rakesh Kalra
 */
public abstract class BaseForm extends ValidatorForm {

    /**
     * The validate method is made final, to use validation.xml based
     * validation.
     *
     * @param mapping   The ActionMapping for this form
     * @param request   The HttpServletRequest that we are processing
     * @return  in case of errors, it returns ActionErrors object;
     * otherwise it returns null
     */
    public ActionErrors validate(ActionMapping mapping,
                                       HttpServletRequest request) {

        return super.validate(mapping, request);
    }

}
