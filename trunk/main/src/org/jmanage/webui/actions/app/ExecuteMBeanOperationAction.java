package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.core.util.CoreUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.management.ObjectName;
import javax.management.MBeanServer;

/**
 *
 * date:  Jun 21, 2004
 * @author	Rakesh Kalra
 */
public class ExecuteMBeanOperationAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception{

        final ObjectName objectName = context.getObjectName();
        final MBeanServer mbeanServer =  context.getMBeanServer();

        final String operationName = request.getParameter("operationName");
        final int paramCount =
                Integer.parseInt(request.getParameter("paramCount"));
        Object[] params = new Object[paramCount];
        String[] signature = new String[paramCount];
        for(int paramIndex=0; paramIndex < paramCount; paramIndex ++){
            String type =
                    request.getParameter(operationName + paramIndex + "_type");
            String value =
                    request.getParameter(operationName + paramIndex + "_value");
            params[paramIndex] = CoreUtils.getTypedValue(value, type);
            signature[paramIndex] = type;
        }

        final Object result = mbeanServer.invoke(objectName, operationName,
                params, signature);
        request.setAttribute("operationResult", result);
        return mapping.findForward(Forwards.SUCCESS);
    }

}
