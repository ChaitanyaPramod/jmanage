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
import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.UserForm;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date : Jul 28, 2004 12:25:24 AM
 * @author Shashank
 */
public class ShowEditUserAction extends BaseAction{

    /**
     * Display User info edit page.
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
        User user = UserManager.getInstance().getUser(username);
        prepareUserForm(actionForm, user);
        return mapping.findForward(Forwards.SUCCESS);
    }

    /**
     * Setup user form.
     *
     * @param form
     * @param user
     */
    private void prepareUserForm(ActionForm form, User user){
        UserForm userForm = (UserForm)form;
        userForm.setUsername(user.getUsername());
        userForm.setPassword(UserForm.FORM_PASSWORD);
        //TODO Need to handle multiple role scenario
        userForm.setRole(user.getRoles().get(0).toString());
        userForm.setStatus(user.getStatus());
    }
}
