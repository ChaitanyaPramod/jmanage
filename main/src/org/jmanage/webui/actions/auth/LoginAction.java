package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.LoginForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import java.io.IOException;

/**
 *
 * date:  Jun 27, 2004
 * @author	Rakesh Kalra
 */
public class LoginAction extends BaseAction {

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        LoginForm loginForm = (LoginForm) actionForm;
        LoginCallbackHandler callbackHandler = new LoginCallbackHandler();
        callbackHandler.username = loginForm.getUsername();
        callbackHandler.password = loginForm.getPassword();

        LoginContext ctx =
                new LoginContext("JManageAuth", callbackHandler);
        ctx.login();

        return mapping.findForward(Forwards.SUCCESS);
    }


    private class LoginCallbackHandler implements CallbackHandler {

        private String username;
        private String password;

        public void handle(Callback[] callbacks)
                throws IOException, UnsupportedCallbackException {
            for (int i = 0; i < callbacks.length; i++) {
                if (callbacks[i] instanceof NameCallback) {
                    NameCallback nc = (NameCallback) callbacks[0];
                    //nc.setName(name);
                } else {
                    throw(new UnsupportedCallbackException(callbacks[i], "Callback handler not support"));
                }
            }
        }
    }

}
