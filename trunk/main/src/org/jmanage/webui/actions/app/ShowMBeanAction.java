/*
 * Copyright 2000-2004 by Upromise Inc.
 * 117 Kendrick Street, Suite 200, Needham, MA, 02494, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Upromise, Inc. ("Confidential Information").  You shall not disclose
 * such Confidential Information and shall use it only in accordance with
 * the terms of an agreement between you and Upromise.
 */
package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.Forwards;
import org.jmanage.core.connector.MBeanServerConnectionFactory;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MBeanInfo;

/**
 *
 * date:  Jun 13, 2004
 * @author	Rakesh Kalra
 */
public class ShowMBeanAction extends BaseAction{

    public ActionForward execute(ActionMapping mapping,
                             ActionForm actionForm,
                             HttpServletRequest request,
                             HttpServletResponse response)
        throws Exception {

        final String applicationId=
                request.getParameter(RequestParams.APPLICATION_ID);
        final String objectName =
                request.getParameter(RequestParams.OBJECT_NAME);

        ApplicationConfig config =
                ApplicationConfigManager.getApplicationConfig(
                        applicationId);
        MBeanServer mbeanServer =
                MBeanServerConnectionFactory.getConnection(config);
        MBeanInfo mbeanInfo = mbeanServer.getMBeanInfo(new ObjectName(objectName));

        request.setAttribute("mbeanInfo", mbeanInfo);

        // TODO: move to central location (RequestProcessor ?)
        request.setAttribute(RequestAttributes.APPLICATION_CONFIG, config);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
