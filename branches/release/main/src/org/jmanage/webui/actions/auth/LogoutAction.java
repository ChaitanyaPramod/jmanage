package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.LoginCallbackHandler;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 * Date : Jul 5, 2004 10:54:41 PM
 * @author Shashank
 */
public class LogoutAction extends BaseAction{

    /**
     * Logout the user.
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
        Subject subject = context.getSubject();
        if(subject != null){
            LoginContext loginContext =
                    new LoginContext(AuthConstants.AUTH_CONFIG_INDEX,
                            subject, new LoginCallbackHandler());
            loginContext.logout();
            context.removeSubject();
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}
