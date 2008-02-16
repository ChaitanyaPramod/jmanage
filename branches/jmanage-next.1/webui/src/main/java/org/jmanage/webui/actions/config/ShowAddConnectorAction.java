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
package org.jmanage.webui.actions.config;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ConnectorForm;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.config.ApplicationType;
import org.jmanage.core.config.ApplicationTypes;
import org.jmanage.core.config.ModuleConfig;
import org.jmanage.core.config.MetaApplicationConfig;
import org.jmanage.connector.framework.ConnectorConfigData;
import org.jmanage.connector.framework.ConnectorConfigRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * date:  Jan 7, 2006
 * 
 * @author	Tak-Sang Chan
 */
public class ShowAddConnectorAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        AccessController.checkAccess(
                context.getServiceContext(), ACL_ADD_APPLICATIONS);

        ConnectorForm connForm = (ConnectorForm)actionForm;
        ApplicationType appType =
                ApplicationTypes.getApplicationType(connForm.getType());

        assert appType.getId().equals("connector"): "Invalid app type: " + connForm.getType();

        ModuleConfig moduleConfig = appType.getModule();
        MetaApplicationConfig metaAppConfig = moduleConfig.getMetaApplicationConfig();
        request.setAttribute(RequestAttributes.META_APP_CONFIG, metaAppConfig);

        if (connForm.getConnectorId() == null
                || connForm.getConnectorId().length() == 0
                || connForm.getConnectorId().equals("none")) {
            connForm.setConfigNames(new String[0]);
        }
        else {
            ConnectorConfigData cfgData =
                    ConnectorConfigRegistry.getConnectorConfigData(
                            connForm.getConnectorId());
            connForm.setConfigNames(cfgData.getFieldNames());
            connForm.setConfigValues(cfgData.getFieldDefaultValues());
        }

        connForm.setConnectorNames(ConnectorConfigRegistry.getConnectorNames());
        connForm.setConnectorIds(ConnectorConfigRegistry.getConnectorIdList());

        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Add Connector");
        return mapping.findForward(Forwards.SUCCESS);
    }

}
