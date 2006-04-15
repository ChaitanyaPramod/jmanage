/*
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.core.data.ApplicationConfigData;
import org.jmanage.core.services.ConfigurationService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.ConnectorForm;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.util.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * date:  Feb 4, 2006
 * 
 * @author	Tak-Sang Chan
 */
public class AddConnectorAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        ConnectorForm connForm = (ConnectorForm) actionForm;
        String[] paramNames = connForm.getConfigNames();
        String[] paramValues = connForm.getConfigValues();

        /* create ApplicationConfigData from this form */
        ApplicationConfigData appConfigData = new ApplicationConfigData();
        Map<String, String> paramValueMap = new HashMap<String, String>();

        paramValueMap.put(ConnectorForm.CONNECTOR_ID, connForm.getConnectorId());

        for (int i = 0; i < paramNames.length; i++) {
            paramValueMap.put(paramNames[i], paramValues[i]);
        }

        appConfigData.setName(connForm.getName());
        appConfigData.setType(connForm.getType());
        appConfigData.setParamValues(paramValueMap);

        ConfigurationService service = ServiceFactory.getConfigurationService();

        service.addApplication(Utils.getServiceContext(context), appConfigData);

        return mapping.findForward(Forwards.SUCCESS);
    }
}
