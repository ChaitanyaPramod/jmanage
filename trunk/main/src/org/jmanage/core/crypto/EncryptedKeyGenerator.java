package org.jmanage.core.crypto;

import org.jmanage.core.util.CoreUtils;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Generates a symmetric key and encrypts it based on the given password.
 *
 * date:  Jul 22, 2004
 * @author	Rakesh Kalra
 */
public class EncryptedKeyGenerator {

    private static final int KEY_SIZE = 56; /* 56 bits */

    public static void main(String[] args)
        throws Exception{

        final char[] password = PasswordField.getPassword("Enter password:");
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(KEY_SIZE);
        SecretKey key = keyGen.generateKey();
        EncryptedKey encryptedKey = new EncryptedKey(key, password);
        /* clear the password, for security reasons */
        Arrays.fill(password, ' ');
        /* write the encryptedKey to the key file */
        KeyManager.writeKey(encryptedKey);
    }
}
