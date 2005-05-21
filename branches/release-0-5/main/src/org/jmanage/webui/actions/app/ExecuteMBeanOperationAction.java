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
package org.jmanage.webui.actions.app;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.core.data.OperationResultData;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.Loggers;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.RequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Executes mbean operation for Application as well as Application Cluster.
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class ExecuteMBeanOperationAction extends BaseAction {

    private static final Logger logger =
            Loggers.getLogger(ExecuteMBeanOperationAction.class);

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception{

        /* get the information about the operation to be executed */
        final String operationName = request.getParameter("operationName");
        final int paramCount =
                Integer.parseInt(request.getParameter("paramCount"));
        String[] params = new String[paramCount];
        String[] signature = new String[paramCount];
        for(int paramIndex=0; paramIndex < paramCount; paramIndex ++){
            params[paramIndex] =
                    request.getParameter(operationName + paramIndex + "_value");
            signature[paramIndex] =
                    request.getParameter(operationName + paramIndex + "_type");
        }

        MBeanService service = ServiceFactory.getMBeanService();
        OperationResultData[] resultData =
                service.invoke(Utils.getServiceContext(context),
                        operationName,
                        params, signature);
        request.setAttribute("operationResultData", resultData);

        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Execute Operation");
        return mapping.findForward(Forwards.SUCCESS);
    }
}
