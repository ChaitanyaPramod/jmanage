package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.UserForm;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
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
        User user = buildUser(actionForm);
        UserManager.getInstance().updateUser(user);
        UserActivityLogger.getInstance().logActivity(
                context.getUser().getUsername(),
                "Edited user "+user.getName()+"/"+user.getPassword());
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
        roles.add(userForm.getRole());
        user.setRoles(roles);
        // TODO: this is bug while updating
        final String hashedPassword = Crypto.hash(userForm.getPassword());
        if(!hashedPassword.equals(user.getPassword())){
            user.setPassword(hashedPassword);
        }
        return user;
    }
}
