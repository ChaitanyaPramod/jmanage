package org.jmanage.webui.actions.auth;

import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;
import org.jmanage.webui.util.Forwards;
import org.jmanage.webui.forms.ChangePasswordForm;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.crypto.Crypto;
import org.apache.struts.action.*;
import org.apache.struts.Globals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Dec 29, 2004
 * @author	Vandana Taneja
 */
public class ChangePasswordAction extends BaseAction{

    public ActionForward execute(WebContext context,
                                 ActionMapping mapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        ChangePasswordForm changePasswordForm = (ChangePasswordForm)actionForm;
        ActionErrors errors = new ActionErrors();

        /*Make sure that entered password is valid*/
        if(!Crypto.hash(changePasswordForm.getOldPassword()).equals
                (context.getUser().getPassword())){
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("invalid.oldPassword"));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }

        /*Make sure that both entered passwords match */
        if(!changePasswordForm.getNewPassword().equals
                (changePasswordForm.getConfirmPassword())){
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("mismatch.password"));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }

        String username = context.getUser().getUsername();
        String password = changePasswordForm.getNewPassword();
        UserManager.getInstance().updatePassword(username, password);

        return mapping.findForward(Forwards.SUCCESS);

       }
}
