/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.webui.actions.app;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.core.services.AlertService;
import org.jmanage.core.services.ServiceFactory;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>
 * Date:  Aug 8, 2005
 * @author	Rakesh Kalra
 */
public class RemoveConsoleAlertAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception{

        AlertService alertService = ServiceFactory.getAlertService();
        alertService.removeConsoleAlert(context.getServiceContext(),
                request.getParameter("alertId"));
        return mapping.findForward("success");
    }
}