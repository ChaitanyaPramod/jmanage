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
    public final ActionErrors validate(ActionMapping mapping,
                                       HttpServletRequest request) {

        return super.validate(mapping, request);
    }

}
