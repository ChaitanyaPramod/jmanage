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
import java.util.ArrayList;
import java.util.List;

/**
 * Date : Jul 28, 2004 1:14:51 AM
 * @author Shashank
 */
public class EditUserAction extends BaseAction{

    /**
     * Edit user info.
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
        User user = buildUser(actionForm);
        UserManager.getInstance().updateUser(user);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Edited user "+user.getName());
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
        User user = UserManager.getInstance().getUser(userForm.getUsername());
        assert user != null;

        List roles = new ArrayList(1);
        roles.add(new Role(userForm.getRole()));
        user.setRoles(roles);
        if(!userForm.getPassword().equals(UserForm.FORM_PASSWORD)){
            String hashedPassword = Crypto.hash(userForm.getPassword());
            user.setPassword(hashedPassword);
        }
        user.setStatus(userForm.getStatus());
        return user;
    }
}
