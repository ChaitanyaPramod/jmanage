/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.LoginForm;
import org.jmanage.core.auth.LoginCallbackHandler;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.config.JManageProperties;
import org.apache.struts.action.*;
import org.apache.struts.Globals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * date:  Jun 27, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class LoginAction extends BaseAction {
    final JManageProperties jManageProperties = JManageProperties.getInstance();
    private int MAX_LOGIN_ATTEMPTS_ALLOWED =
            Integer.parseInt(jManageProperties.getProperty(JManageProperties.maxLoginAttempts));

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

        // TODO: we should set this in startup or in startup script
        System.setProperty(AuthConstants.AUTH_CONFIG_SYS_PROPERTY,
                AuthConstants.AUTH_CONFIG_FILE_NAME);
        LoginContext loginContext =
                new LoginContext(AuthConstants.AUTH_CONFIG_INDEX,
                        callbackHandler);
        User user = null;
        UserManager userManager = UserManager.getInstance();
        try{
            loginContext.login();
        }catch(LoginException lex){
            ActionErrors errors = new ActionErrors();
            user = userManager.getUser(loginForm.getUsername());
            /* Conditionalize the error message */
            if(user == null){
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("invalid.login"));
            }else if("I".equals(user.getStatus())){
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("account.locked"));
            }else if(user.getLockCount() < MAX_LOGIN_ATTEMPTS_ALLOWED){
                int thisAttempt = user.getLockCount()+1;
                user.setLockCount(thisAttempt);
                if(thisAttempt == MAX_LOGIN_ATTEMPTS_ALLOWED){
                    user.setStatus("I");
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("account.locked"));
                }else{
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("invalid.login.attempt.count",
                                    String.valueOf(MAX_LOGIN_ATTEMPTS_ALLOWED - thisAttempt)));
                }
                userManager.updateUser(user);
            }else{
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("unknown.error"));
            }
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }
        /*  set Subject in session */
        context.setSubject(loginContext.getSubject());
        user = context.getUser();
        if(user.getLockCount() > 0){
            user.setLockCount(0);
            userManager.updateUser(user);
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}