package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.forms.MBeanQueryForm;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigManager;
import org.jmanage.core.connector.MBeanServerConnectionFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.management.*;
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

        MBeanServer mbeanServer = context.getMBeanServer();

        Set mbeans = mbeanServer.queryMBeans(new ObjectName(queryObjectName), null);
        List objectNameList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator(); it.hasNext();){
            ObjectInstance oi = (ObjectInstance)it.next();
            objectNameList.add(oi.getObjectName());
        }

        request.setAttribute("objectNameList", objectNameList);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
