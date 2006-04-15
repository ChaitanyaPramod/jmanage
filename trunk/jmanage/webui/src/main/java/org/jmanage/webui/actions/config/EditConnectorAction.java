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
package org.jmanage.webui.actions.config;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.core.alert.AlertEngine;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.util.UserActivityLogger;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.ConnectorForm;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * date:  Feb 4, 2006
 * 
 * @author	Tak-Sang Chan
 */
public class EditConnectorAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        AccessController.checkAccess(context.getServiceContext(),
                ACL_EDIT_APPLICATIONS);

        ConnectorForm connForm = (ConnectorForm) actionForm;

        ApplicationConfig config =
                ApplicationConfigManager.getApplicationConfig(
                        connForm.getApplicationId());

        assert config != null;

        config.setName(connForm.getName());

        Map<String, String> paramValueMap = config.getParamValues();

        String[] paramNames = connForm.getConfigNames();
        String[] paramValues = connForm.getConfigValues();

        for (int i = 0; i < paramNames.length; i++) {
            if(paramValues[i] != null) {
                paramValueMap.put(paramNames[i], paramValues[i]);
            }
            else {
                paramValueMap.remove(paramNames[i]);
            }
        }

        config.setParamValues(paramValueMap);

        ApplicationConfigManager.updateApplication(config);

        /* update the AlertEngine */
        AlertEngine.getInstance().updateApplication(config);

        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Updated application " + "\"" + config.getName() + "\"");

        return mapping.findForward(Forwards.SUCCESS);
    }
}
