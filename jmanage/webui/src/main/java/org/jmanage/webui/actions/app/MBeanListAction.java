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
import org.jmanage.core.data.MBeanData;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.MBeanQueryForm;
import org.jmanage.webui.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * date:  Jun 10, 2004
 * @author	Rakesh Kalra
 */
public class MBeanListAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        MBeanQueryForm queryForm = (MBeanQueryForm)actionForm;
        final String queryObjectName = queryForm.getObjectName();
        
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        List mbeanDataList = mbeanService.queryMBeans(Utils.getServiceContext(context),
                queryObjectName);

        Map<String, Set<String>> domainToObjectNameListMap = new TreeMap<String, Set<String>>();
        ObjectNameTuple tuple = new ObjectNameTuple();
        for(Iterator it=mbeanDataList.iterator(); it.hasNext();){
            MBeanData mbeanData = (MBeanData)it.next();
            tuple.setObjectName(mbeanData.getName());
            String domain = tuple.getDomain();
            String name = tuple.getName();
            Set<String> objectNameList = domainToObjectNameListMap.get(domain);
            if(objectNameList == null){
                objectNameList = new TreeSet<String>();
                domainToObjectNameListMap.put(domain, objectNameList);
            }
            objectNameList.add(name);
        }

        request.setAttribute("domainToObjectNameListMap", domainToObjectNameListMap);
        
        /*set current page for navigation*/
        request.getSession().setAttribute(SessionAttributes.MBEAN_QUERY, queryObjectName);
        
        return mapping.findForward(Forwards.SUCCESS);
    }

    private static class ObjectNameTuple{
        String domain;
        String name;

        void setObjectName(String canonicalName){
            int index = canonicalName.indexOf(":");
            domain = canonicalName.substring(0, index);
            name = canonicalName.substring(index + 1);
        }

        String getName(){
            return name;
        }
        String getDomain(){
            return domain;
        }
    }
}
