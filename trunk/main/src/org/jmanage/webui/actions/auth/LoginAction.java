package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.LoginForm;
import org.jmanage.core.auth.LoginCallbackHandler;
import org.jmanage.core.auth.AuthConstants;
import org.apache.struts.action.*;
import org.apache.struts.Globals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * date:  Jun 27, 2004
 * @author	Rakesh Kalra
 */
public class LoginAction extends BaseAction {

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

        LoginForm loginForm = (LoginForm) actionForm;
        LoginCallbackHandler callbackHandler = new LoginCallbackHandler();
        callbackHandler.setUsername(loginForm.getUsername());
        callbackHandler.setPassword(loginForm.getPassword());

        System.setProperty(AuthConstants.AUTH_CONFIG_SYS_PROPERTY,
                AuthConstants.AUTH_CONFIG_FILE_NAME);
        LoginContext loginContext =
                new LoginContext(AuthConstants.AUTH_CONFIG_INDEX,
                        callbackHandler);

        try{
            loginContext.login();
        }catch(LoginException lex){
            ActionErrors errors = new ActionErrors();
            /* set error message */
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("invalid.login"));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }
        /*  Need LoginContext for logout at a later stage   */
        context.setSubject(loginContext.getSubject());
        return mapping.findForward(Forwards.SUCCESS);
    }
}