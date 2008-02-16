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

import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.config.*;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.crypto.EncryptedKey;
import org.jmanage.core.crypto.KeyManager;
import org.jmanage.core.util.PasswordField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generates a symmetric key and encrypts it based on the given password.
 *
 * date:  Jul 22, 2004
 * @author	Rakesh Kalra
 */
public class EncryptedKeyGenerator {

    public static void main(String[] args)
        throws Exception{

        /* display info */
        message();
        reencryptWithNewKey() ;
    }

    private static void message(){
        System.out.println();
        System.out.println("This tool generates a 128 bit TripleDES key and then");
        System.out.println("encrypts it with Password Based Encryption (PBE),");
        System.out.println("before writing it to jmanage-key file.");     
        System.out.println();
    }

    private static char[] getOldPassword()
    throws Exception {
          final char[] password = PasswordField.getPassword("Enter old password: ");
          return password ;
    }
    private static char[] getPassword()
        throws Exception {

        final char[] password = PasswordField.getPassword("Enter new password:");
        final char[] password2 = PasswordField.getPassword("Re-enter new password:");
        if(!Arrays.equals(password, password2)){
            System.out.println("Passwords do not match. " +
                    "Key has not been generated.");
            return null;
        }
        return password;
    }

    private static void reencryptWithNewKey(  )
        throws Exception {
        
       /* get old password from user */
       char [] oldPassword = getOldPassword() ;
       UserManager userMgr = UserManager.getInstance() ;
       User user = userMgr.verifyUsernamePassword(AuthConstants.USER_ADMIN,oldPassword);
        if(user == null) {
            System.out.println("\nInvalid password") ;
            return ;
        }
         /* get new password from user */
        char[] newPassword = getPassword();
        if(newPassword == null){
            return;
        }

          /* Get list of configured applications */
        Crypto.init(oldPassword) ;
        Config config = ConfigReader.getInstance().read() ;
        if(config == null) {
            System.out.println("\nError in reading application passwords") ;
            return ;
        }

        /* Write new key to file */
      EncryptedKey encryptedKey = new EncryptedKey(newPassword);
      KeyManager.writeKey(encryptedKey);
      /* Change admin password */
      UserManager.getInstance().deleteUser(AuthConstants.USER_ADMIN);
      List roles = new ArrayList(1); // TODO: Shouldn't this be zero ? -rk
      UserManager.getInstance().addUser(new User(AuthConstants.USER_ADMIN,
      Crypto.hash(newPassword), roles, User.STATUS_ACTIVE, 0));
      Crypto.init(newPassword);
      ConfigWriter.getInstance().write(config);
      System.out.println("New key has been written to key file successfully..");
    }
}
