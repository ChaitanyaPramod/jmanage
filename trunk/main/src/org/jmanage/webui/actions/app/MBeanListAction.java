package org.jmanage.webui.actions.app;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.WeblogicApplicationConfig;
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

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        Map attributes = new HashMap(1);
        attributes.put(WeblogicApplicationConfig.SERVER_NAME, "loyaltyServer");
        ApplicationConfig config =
                ApplicationConfigFactory.create("test",
                        ApplicationConfig.TYPE_WEBLOGIC,
                        "localhost", 7001,
                        "system", "12345678",
                        attributes);

        MBeanServer mbeanServer =
                MBeanServerConnectionFactory.getConnection(config);

        Set mbeans = mbeanServer.queryMBeans(new ObjectName("*:*"), null);
        List objectNameList = new ArrayList(mbeans.size());
        for(Iterator it=mbeans.iterator(); it.hasNext();){
            ObjectInstance oi = (ObjectInstance)it.next();
            objectNameList.add(oi.getObjectName());
        }
        request.setAttribute("objectNameList", objectNameList);
        return mapping.findForward("success");
    }
}
