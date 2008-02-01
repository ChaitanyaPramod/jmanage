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
package org.jmanage.core.tools;

import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.util.PasswordField;
import org.jmanage.core.crypto.EncryptedKey;
import org.jmanage.core.crypto.KeyManager;
import org.jmanage.core.crypto.Crypto;

import java.util.Arrays;

/**
 *
 * date:  Aug 3, 2004
 * @author	Rakesh Kalra, Shashank Bellary
 */
public class ChangeAdminPassword {

    public static void main(String[] args)
        throws Exception {
    	String jManageRoot = System.getProperty("JMANAGE_ROOT");
    	CoreUtils.initJmanageForCLIUtilities(jManageRoot);
        final UserManager userManager = UserManager.getInstance();
        final char[] oldPassword =
                PasswordField.getPassword("Enter Old password:");
        final User admin = userManager.verifyUsernamePassword(AuthConstants.USER_ADMIN,
                oldPassword);
        if(admin == null){
            System.out.println("Invalid password.");
            return;
        }

        final char[] newPassword = getNewPassword();
        if(newPassword == null){
            return;
        }

        /* update the password for admin */
        admin.setPassword(Crypto.hash(newPassword));
        userManager.updateUser(admin);

        /* re-encrypt the key */
        EncryptedKey encryptedKey = KeyManager.readKey(oldPassword);
        encryptedKey.setPassword(newPassword);
        /* write the encryptedKey to the key file */
        KeyManager.writeKey(encryptedKey);

        System.out.println("Password has been changed");
    }

    private static char[] getNewPassword()
        throws Exception {

        final char[] password = PasswordField.getPassword("Enter new password:");
        final char[] password2 = PasswordField.getPassword("Re-enter new password:");
        if(!Arrays.equals(password, password2)){
            System.out.println("Passwords do not match.");
            return null;
        }
        return password;
    }
}
