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

import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.auth.UserManager;
import org.jmanage.core.auth.User;
import org.jmanage.core.auth.AuthConstants;
import org.jmanage.core.auth.Role;
import org.jmanage.core.crypto.EncryptedKey;
import org.jmanage.core.crypto.KeyManager;
import org.jmanage.core.crypto.Crypto;
import org.jmanage.core.crypto.PasswordField;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
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

        /* get password from user */
        char[] password = getPassword();
        if(password == null){
            return;
        }

        EncryptedKey encryptedKey = new EncryptedKey(password);
        /* write the encryptedKey to the key file */
        KeyManager.writeKey(encryptedKey);

        List roles = new ArrayList(1);
        UserManager.getInstance().addUser(new User(AuthConstants.USER_ADMIN,
                Crypto.hash(password), roles, "A", 0));

        /* clear the password, for security reasons */
        Arrays.fill(password, ' ');

        System.out.println();
        System.out.println("Encrypted key written to "
                + KeyManager.KEY_FILE_NAME
                + " file.");
    }

    private static void message(){
        System.out.println();
        System.out.println("This tool generates a 128 bit TripleDES key and then");
        System.out.println("encrypts it with Password Based Encryption (PBE),");
        System.out.println("before writing it to jmanage-key file.");
        System.out.println();
        System.out.println("Please select a password below. This password will");
        System.out.println("be required to start jmanage.");
        System.out.println();
    }

    private static char[] getPassword()
        throws Exception {

        final char[] password = PasswordField.getPassword("Enter password:");
        final char[] password2 = PasswordField.getPassword("Re-enter password:");
        if(!Arrays.equals(password, password2)){
            System.out.println("Passwords do not match. " +
                    "Key has not been generated.");
            return null;
        }
        return password;
    }
}
