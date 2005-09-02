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
package org.jmanage.webui.actions.config.alert;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.AlertForm;
import org.jmanage.core.config.AlertSourceConfig;
import org.jmanage.core.config.ApplicationConfig;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>
 * Date:  Aug 3, 2005
 * @author	Rakesh Kalra
 */
public class SelectAlertSourceTypeAction extends BaseAction{

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {


        AlertForm alertForm = (AlertForm)actionForm;
        String sourceType = alertForm.getAlertSourceType();
        assert sourceType != null;
        if(sourceType.equals(AlertSourceConfig.SOURCE_TYPE_GAUGE_MONITOR)){
            ApplicationConfig appConfig= context.getApplicationConfig();
            String url = "/config/showMBeans.do?"
                    +  RequestParams.APPLICATION_ID + "="
                    + appConfig.getApplicationId() + "&"
                    + RequestParams.END_URL + "="
                    + Utils.encodeURL("/config/showAddAlert.do")+ "&"
                    + RequestParams.MULTIPLE + "=false&"
                    + RequestParams.ALERT_SOURCE_TYPE + "=gauge";
            return new ActionForward(url);
        }
        return mapping.findForward(sourceType);
    }
}
