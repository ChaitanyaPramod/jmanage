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
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.forms.LoginForm;
import org.jmanage.core.services.AuthService;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.services.ServiceException;
import org.jmanage.core.util.ErrorCodes;
import org.apache.struts.action.*;
import org.apache.struts.Globals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Jun 27, 2004
 * @author	Rakesh Kalra, Shashank Bellary
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
        ActionErrors errors = new ActionErrors();
        LoginForm loginForm = (LoginForm) actionForm;
        AuthService authService = ServiceFactory.getAuthService();

        try{
            authService.login(Utils.getServiceContext(context),
                    loginForm.getUsername(),
                    loginForm.getPassword());
        }catch(ServiceException se){
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(ErrorCodes.WEB_UI_ERROR_KEY, se.getMessage()));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }catch(Exception ex){
            //TODO Architcture should handle this (ExceptionHandler)
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(ErrorCodes.UNKNOWN_ERROR));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }
        return mapping.findForward(Forwards.SUCCESS);
    }
}