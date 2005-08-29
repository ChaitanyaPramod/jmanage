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
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.core.util.Expression;
import org.jmanage.core.services.ServiceContextImpl;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.management.ObjectAttribute;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Reads the "attributes" parameter and creates ObjectAttribute object for
 * each attribute. The "attributes" parameter is of the following format:
 * <p>
 * [app1/mbean1/attribute1],[app2/mbean2/attribute2]
 *
 *
 * Date:  Jun 11, 2005
 * @author	Rakesh Kalra
 */
public class MBeanAttributeValuesAction extends BaseAction {


    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception{

        String attributes = request.getParameter("attributes");
        assert attributes != null;
        List exprList = parse(attributes);
        List objectAttrList = new LinkedList();
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        for(Iterator it=exprList.iterator(); it.hasNext();){
            Expression expression = (Expression)it.next();
            ServiceContext srvcContext =
                    getServiceContext(context, expression);
            ObjectAttribute objAttribute =
                    mbeanService.getObjectAttribute(srvcContext,
                            expression.getTargetName());
            assert objAttribute != null;
            assert objAttribute.getStatus() == ObjectAttribute.STATUS_OK;
            objectAttrList.add(objAttribute);
        }
        request.setAttribute("objectAttrList", objectAttrList);
        return mapping.findForward(Forwards.SUCCESS);
    }

    /**
     *
     * @param attributes
     * @return list of Expression objects
     */
    private List parse(String attributes){
        List exprList = new LinkedList();
        StringTokenizer tokenizer = new StringTokenizer(attributes, ",");
        while(tokenizer.hasMoreTokens()){
            String expression = tokenizer.nextToken();
            assert expression.startsWith("[");
            while(!expression.endsWith("]")){
                assert tokenizer.hasMoreTokens();
                expression += tokenizer.nextToken();
            }
            expression = expression.substring(1, expression.length()-1);
            exprList.add(new Expression(expression));
        }
        return exprList;
    }

    private ServiceContext getServiceContext(WebContext context,
                                             Expression expression){
        ServiceContextImpl srvcContext = new ServiceContextImpl();
        srvcContext.setUser(context.getUser());
        srvcContext.setApplicationName(expression.getAppName());
        srvcContext.setMBeanName(expression.getMBeanName());
        return srvcContext;
    }
}
