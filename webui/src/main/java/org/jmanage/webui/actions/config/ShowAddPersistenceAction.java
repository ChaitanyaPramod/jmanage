/**
 * jManage Application Management Platform
 * Copyright 2004-2008 jManage.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.PersistenceForm;
import org.jmanage.core.util.Expression;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date: Mar 09, 2008 6:10:37 PM
 * @author Avneet
 */
public class ShowAddPersistenceAction extends BaseAction{
    /**
     * @param context
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        AccessController.checkAccess(Utils.getServiceContext(context), ACL_ADD_GRAPH);
        PersistenceForm form = (PersistenceForm)actionForm;
        String[] attributes = form.getAttributes();
        String[] attributeNames = new String[attributes.length];
        String[] objectNames = new String[attributes.length];
        String[] displayNames = new String[attributes.length];
        for(int i=0; i<attributes.length;i++){
            Expression expression = new Expression(attributes[i]);
            attributeNames[i] = expression.getTargetName();
            displayNames[i] = expression.getTargetName();
            objectNames[i] = expression.getMBeanName();
        }
        request.setAttribute("attributeNames", attributeNames);
        request.setAttribute("objectNames",objectNames);
        request.setAttribute("displayNames",displayNames);
        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Persist Data");

        return mapping.findForward(Forwards.SUCCESS);
    }

}
