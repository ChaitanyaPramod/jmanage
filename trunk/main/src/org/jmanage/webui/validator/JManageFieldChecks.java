/*
 * $Header: /home/cmsdomain/avneet/jmanage-cvsbackup/main/src/org/jmanage/webui/validator/JManageFieldChecks.java,v 1.1 2004-11-14 09:11:17 sbellary Exp $
 * $Revision: 1.1 $
 * $Date: 2004-11-14 09:11:17 $
 *
 * ====================================================================
 *
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution, if
 *  any, must include the following acknowlegement:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowlegement may appear in the software itself,
 *  if and wherever such third-party acknowlegements normally appear.
 *
 *  4. The names "The Jakarta Project", "Struts", and "Apache Software
 *  Foundation" must not be used to endorse or promote products derived
 *  from this software without prior written permission. For written
 *  permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache"
 *  nor may "Apache" appear in their names without prior written
 *  permission of the Apache Group.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
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
     * Checks if the field exists in the request and isn't null and length of
     * the field is greater than zero not including whitespace.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>ActionErrors</code> object to add errors to if
     * any validation errors occur.
     * @param request Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateRequired(Object bean,
                                           ValidatorAction va, Field field,
                                           ActionErrors errors,
                                           HttpServletRequest request) {

        String value = null;
        /*  If the filed being validated doesn't exists in the request, then
            don't validate  */
        if(!fieldExists(request, field.getProperty()))
            return true;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            errors.add(field.getKey(), Resources.getActionError(request, va, field));
            return false;
        } else {
            return true;
        }

    }

    /**
     *  Return <code>true</code> if the specified object is a String or a
     * <code>null</code> value.
     *
     * @param o Object to be tested
     * @return The string value
     */
    protected static boolean isString(Object o) {
        return (o == null) ? true : String.class.isInstance(o);
    }

    /**
     * Return <code>true</code> if the specified parameter exists in the request.
     *
     * @param request
     * @param fieldName
     * @return
     */
    protected static boolean fieldExists(HttpServletRequest request,
                                         String fieldName){
        return request.getParameter(fieldName) != null ? true : false;
    }
}