package org.jmanage.core.crypto;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

/**
 *
 * date:  Jul 22, 2004
 * @author	Rakesh Kalra
 */
public class Test {


    private static final String PWD = "test";

    public static void main(String[] args)
            throws Exception {

        String plaintext = "this is plaintext";
        byte[] cyphertext = getEncrypted(plaintext);
        System.out.println(cyphertext);
        String plaintext1 = getDecrypted(cyphertext);
        System.out.println(plaintext1);
    }

    private static byte[] getEncrypted(String plaintext)
        throws Exception {

        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Salt
        byte[] salt = {
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
        };

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);

        pbeKeySpec = new PBEKeySpec(PWD.toCharArray());
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        // Our cleartext
        byte[] cleartext = plaintext.getBytes();

        // Encrypt the cleartext
        return pbeCipher.doFinal(cleartext);
    }


    private static String getDecrypted(byte[] cyphertext)
        throws Exception {

        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Salt
        byte[] salt = {
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
        };

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);

        pbeKeySpec = new PBEKeySpec(PWD.toCharArray());
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        // Encrypt the cleartext
        return new String(pbeCipher.doFinal(cyphertext));
    }
}
