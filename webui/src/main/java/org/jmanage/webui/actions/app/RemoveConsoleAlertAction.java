/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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