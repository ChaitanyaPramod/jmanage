package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.core.module.ModuleRegistry;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Lists all types of application that JManage currently supports.
 *
 * Date: Nov 2, 2004 10:18:16 PM
 * @author Shashank Bellary 
 */
public class ShowAvailableApplicationAction extends BaseAction{

    /**
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
        Map availableApplications = ModuleRegistry.getModules();
        request.setAttribute(RequestAttributes.AVAILABLE_APPLICATIONS,
                availableApplications);
        return mapping.findForward(Forwards.SUCCESS);
    }
}