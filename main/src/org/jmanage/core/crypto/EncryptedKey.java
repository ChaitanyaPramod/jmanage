package org.jmanage.core.crypto;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.Cipher;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.DESKeySpec;
import java.util.Arrays;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * date:  Jul 22, 2004
 * @author	Rakesh Kalra
 */
public class EncryptedKey {

    /* Salt */
    private static final byte[] salt = {
        (byte) 0xd7, (byte) 0x73, (byte) 0x31, (byte) 0x8c,
        (byte) 0x8e, (byte) 0xb7, (byte) 0xee, (byte) 0x91
    };

    /* Iteration count */
    private static final int iteration_count = 1000;

    private SecretKey secretKey;
    private byte[] encryptedKey;

    public EncryptedKey(SecretKey key, char[] password)
            throws Exception {

        this.secretKey = key;
        this.encryptedKey = getEncrypted(secretKey.getEncoded(), password);
    }

    public EncryptedKey(byte[] encryptedKey, char[] password) {

        this.encryptedKey = encryptedKey;
        /* get SecretKey from encryptedKey */
        byte[] key = getDecrypted(encryptedKey, password);
        try {
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DES");
            this.secretKey = keyFac.generateSecret(new DESKeySpec(key));;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] get() {
        return encryptedKey;
    }

    private static byte[] getEncrypted(byte[] plaintext, char[] password) {

        try {
            /* Create PBE Cipher */
            Cipher pbeCipher = getCipher(Cipher.ENCRYPT_MODE, password);
            /* Encrypt the plaintext */
            return pbeCipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getDecrypted(byte[] cyphertext, char[] password){

        try {
            /* Create PBE Cipher */
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, password);
            /* get the plaintext */
            return cipher.doFinal(cyphertext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Cipher getCipher(int mode, char[] password)
            throws Exception {

        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        /* Create PBE parameter set */
        pbeParamSpec = new PBEParameterSpec(salt, iteration_count);

        pbeKeySpec = new PBEKeySpec(password);
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        /* Create PBE Cipher */
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        /* Initialize PBE Cipher with key and parameters */
        pbeCipher.init(mode, pbeKey, pbeParamSpec);
        return pbeCipher;
    }
}
