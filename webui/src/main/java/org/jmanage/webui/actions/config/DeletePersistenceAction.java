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

import org.jmanage.monitoring.data.dao.ObservedMBeanAttributeDAO;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date: Apr 07, 2008 9:23:40 PM
 * @author Avneet
 */
public class DeletePersistenceAction extends BaseAction{
    public ActionForward execute(WebContext context, ActionMapping mapping,
                                     ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response)
            throws Exception {
        AccessController.checkAccess(Utils.getServiceContext(context),ACL_EDIT_PERSISTENCE);
    		ObservedMBeanAttributeDAO dao = new ObservedMBeanAttributeDAO();    		
        int result = dao.remove(Integer.parseInt(request.getParameter(RequestParams.PERSISTENCE_ID)));
        assert result==1;
        return mapping.findForward(Forwards.SUCCESS);
    }

}
