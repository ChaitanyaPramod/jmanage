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
import org.jmanage.webui.forms.UserForm;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.Role;
import org.jmanage.core.services.AccessController;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.util.UserActivityLogger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/**
 * Date : Jul 28, 2004 1:45:27 AM
 * @author Shashank
 */
public class AddUserAction extends BaseAction{

    /**
     * Add new user to the list.
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
        AccessController.checkAccess(context.getServiceContext(), ACL_ADD_USERS);
        User user = buildUser(actionForm);
        UserManager.getInstance().addUser(user);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(), "Added user "+user.getName());
        return mapping.findForward(Forwards.SUCCESS);
    }

    /**
     * Build User object from Action form.
     *
     * @param form
     * @return
     */
    private User buildUser(ActionForm form){
        UserForm userForm = (UserForm)form;
        List<Role> roles = new ArrayList<Role>(1);
        String[] rolesString = userForm.getRole();
        for(int ctr=0; ctr < rolesString.length; ctr++){
        	roles.add(new Role(rolesString[ctr]));
        }
        User user = new User(userForm.getUsername(), Crypto.hash(userForm.getPassword()),
                roles, userForm.getStatus(), 0);
        return user;
    }
}
