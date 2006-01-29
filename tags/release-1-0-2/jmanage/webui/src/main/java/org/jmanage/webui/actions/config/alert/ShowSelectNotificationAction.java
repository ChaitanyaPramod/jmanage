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
package org.jmanage.webui.actions.config.alert;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * <p>
 * Date:  Aug 3, 2005
 * @author	Rakesh Kalra
 */
public class ShowSelectNotificationAction extends BaseAction{

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        String objName = request.getParameter(RequestParams.OBJECT_NAME);
        /* find mbeans that have notifications */
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        Map mbeanToNotificationsMap = new HashMap();
        Map mbeansToNotificationsMap =
                mbeanService.queryMBeansWithNotifications(context.getServiceContext());
        if(objName != null){
            mbeanToNotificationsMap.put(objName, mbeansToNotificationsMap.get(objName));
            request.setAttribute("mbeanToNotificationsMap", mbeanToNotificationsMap);
        }else{
            request.setAttribute("mbeanToNotificationsMap", mbeansToNotificationsMap);
        }
        /*set current page for navigation*/
        request.setAttribute(RequestAttributes.NAV_CURRENT_PAGE, "Add Alert");
        return mapping.findForward(Forwards.SUCCESS);
    }
}
