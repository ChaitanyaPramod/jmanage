package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Forwards;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.util.UserActivityLogger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date : Jul 27, 2004 11:39:21 PM
 * @author Shashank
 */
public class DeleteUserAction extends BaseAction{

    /**
     * Delete the selected user.
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
        String username = request.getParameter(RequestParams.USER_NAME);
        UserManager.getInstance().deleteUser(username);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(), "Deleted user "+username);
        return mapping.findForward(Forwards.SUCCESS);
    }
}
