package org.jmanage.webui.actions.app;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.forms.MBeanQueryForm;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.WebContext;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectInstance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

        ServerConnection serverConnection = context.getServerConnection();

        Set mbeans =
                serverConnection.queryObjects(new ObjectName(queryObjectName));
        List objectNameList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator(); it.hasNext();){
            ObjectInstance oi = (ObjectInstance)it.next();
            objectNameList.add(oi.getObjectName());
        }

        request.setAttribute("objectNameList", objectNameList);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
