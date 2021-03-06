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
import org.jmanage.webui.util.*;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.AttributeSelectionForm;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Date: Jun 28, 2005 9:12:52 PM
 * @author Bhavana
 * @author Shashank Bellary
 */
public class ShowMBeanAction extends BaseAction {
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        AttributeSelectionForm form = (AttributeSelectionForm)actionForm;
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        Map domainToObjectNameListMap = mbeanService.queryMBeansOutputMap
                        (Utils.getServiceContext(context),form.getObjectName(),
                                request.getParameterValues(RequestParams.DATA_TYPE),
                                request.getParameter(RequestParams.APPLY_ATTRIB_FILTER));
        request.setAttribute("domainToObjectNameListMap", domainToObjectNameListMap);
        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, form.getNavigation());
        if("true".equals(request.getParameter(RequestParams.SHOW_END_URL))){
            ActionForward forward =
                    new ActionForward(request.getParameter(RequestParams.END_URL));
            return forward;
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}
