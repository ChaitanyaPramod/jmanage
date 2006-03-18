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
import org.jmanage.webui.forms.ChangePasswordForm;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.crypto.EncryptedKey;
import org.jmanage.core.crypto.KeyManager;
import org.jmanage.core.util.ErrorCodes;
import org.apache.struts.action.*;
import org.apache.struts.Globals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * date:  Dec 29, 2004
 * @author	Vandana Taneja
 * @author Bhavana Kalra
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
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(ErrorCodes.INVALID_OLD_PASSWORD));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }

        /*Make sure that both entered passwords match */
        if(!changePasswordForm.getNewPassword().equals
                (changePasswordForm.getConfirmPassword())){
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError(ErrorCodes.PASSWORD_MISMATCH));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.getInputForward();
        }

        /* TODO: there is some odd behavior with this code - rk*/
        if(context.getUser().getName().equals(AuthConstants.USER_ADMIN)){
            /* re-encrypt the key */
            EncryptedKey encryptedKey = KeyManager.readKey(changePasswordForm.getOldPassword().toCharArray());
            encryptedKey.setPassword(changePasswordForm.getNewPassword().toCharArray());
            /* write the encryptedKey to the key file */
            KeyManager.writeKey(encryptedKey);
        }

        String username = context.getUser().getUsername();
        String password = changePasswordForm.getNewPassword();
        UserManager.getInstance().updatePassword(username, password);

        return mapping.findForward(Forwards.SUCCESS);

    }
}
