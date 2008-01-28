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

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.Utils;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.ServiceException;
import org.jmanage.core.data.AttributeListData;
import org.jmanage.core.util.ErrorCodes;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 * @author  Shashank Bellary
 */
public class UpdateMBeanAttributesAction extends BaseAction {


    /**
     * Updates MBean attributes at a stand alone application level or at a
     * cluster level.
     *
     * @param context
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        MBeanService mbeanService = ServiceFactory.getMBeanService();
        AttributeListData[] attrListData =
                mbeanService.setAttributes(Utils.getServiceContext(context),
                        request.getParameterMap());
        StringBuffer erroneousApps = new StringBuffer();
        for(int i=0; i<attrListData.length; i++){
            if(attrListData[i].isError()){
                if(erroneousApps.length() > 0){
                    erroneousApps.append(", ");
                }
                erroneousApps.append(attrListData[i].getAppName());
            }
        }
        if(erroneousApps.length() > 0){
            throw new ServiceException(ErrorCodes.ERRONEOUS_APPS, erroneousApps);
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}
