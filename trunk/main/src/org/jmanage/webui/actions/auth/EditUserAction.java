package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.UserForm;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
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
        List roles = new ArrayList(1);
        roles.add(userForm.getRole());
        User user = new User(userForm.getUsername(), userForm.getPassword(), roles);
        return user;
    }
}
