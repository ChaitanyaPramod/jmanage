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

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.AttributeSelectionForm;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.ObjectAttributeInfo;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;

/**
 * Date: Jun 29, 2005 4:48:09 PM
 * @author Bhavana
 */
public class ShowAttributesAction extends BaseAction{
    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        AttributeSelectionForm form = (AttributeSelectionForm)actionForm;
        form.setEndURL(request.getParameter(RequestParams.END_URL));
        String[] mbeans = form.getMbeans();
        ServerConnection serverConn = context.getServerConnection();
        ObjectName objectName = null;
        Map mbeanAttributesListMap = new TreeMap();
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        for(int i=0; i<mbeans.length;i++){
            objectName = new ObjectName(mbeans[i]);
            ObjectInfo objInfo = serverConn.getObjectInfo(objectName);
            ObjectAttributeInfo[] objAttrInfo = objInfo.getAttributes();
            List objAttrInfoList = mbeanService.filterAttributes(
                    Utils.getServiceContext(context),
                    objAttrInfo,
                    form.getDataTypes());
            mbeanAttributesListMap.put(mbeans[i],objAttrInfoList);
        }
        request.setAttribute("mbeanAttributesMap",mbeanAttributesListMap);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
