package org.jmanage.webui.actions.config;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.core.config.ApplicationConfigManager;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * Date: Jun 19, 2004
 * @author  Shashank
 */
public class ApplicationListAction extends BaseAction {

    /**
     * List all configured applications.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        Map applications = ApplicationConfigManager.getApplications();
        request.setAttribute(RequestAttributes.APPLICATIONS, applications);
        return mapping.findForward(Forwards.SUCCESS);
    }
}