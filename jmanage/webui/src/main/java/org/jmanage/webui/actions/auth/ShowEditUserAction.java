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
import org.jmanage.webui.util.RequestAttributes;
import org.jmanage.webui.forms.UserForm;
import org.jmanage.core.auth.*;
import org.jmanage.core.services.AccessController;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Iterator;
import java.util.List;

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
        AccessController.checkAccess(context.getServiceContext(), ACL_EDIT_USERS);
        String username = request.getParameter(RequestParams.USER_NAME);
        User user = UserManager.getInstance().getUser(username);
        prepareUserForm(actionForm, user);
        List roles = RoleManager.getAll();
        request.setAttribute(RequestAttributes.ROLES, roles);
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
        userForm.setConfirmPassword(UserForm.FORM_PASSWORD);
        //TODO Need to handle multiple role scenario
        if(!AuthConstants.USER_ADMIN.equals(user.getUsername())){
        	String[] roles = new String[user.getRoles().size()];
        	int ctr = 0;
        	for(Iterator it= user.getRoles().iterator(); it.hasNext();){
        		roles[ctr++] = ((Role)it.next()).getName();
        	}
            userForm.setRole(roles);
        }
        userForm.setStatus(user.getStatus());
    }
}
