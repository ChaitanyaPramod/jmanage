package org.jmanage.core.crypto;

import org.jmanage.core.util.Tracer;

import javax.crypto.*;

/**
 * Crypto acts as a facade layer for the crypto classes.
 *
 * date:  Jul 23, 2004
 * @author	Rakesh Kalra
 */
public class Crypto {

    private static Cipher encrypter;
    private static Cipher decrypter;

    private static Object obj = new Object();

    public static void init(char[] password){
        EncryptedKey encryptedKey = KeyManager.readKey(password);
        SecretKey secretKey = encryptedKey.getSecretKey();
        encrypter = getCipher(Cipher.ENCRYPT_MODE, secretKey);
        assert encrypter != null;
        decrypter = getCipher(Cipher.DECRYPT_MODE, secretKey);
        assert decrypter != null;
        Tracer.message(Crypto.class, "Crypto initialized");
    }

    public static synchronized byte[] encrypt(String plaintext){
        try {
            return encrypter.doFinal(plaintext.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptToString(String plaintext){
        byte[] ciphertext = encrypt(plaintext);
        return byteArrayToHexString(ciphertext);
    }

    public static synchronized byte[] decrypt(byte[] ciphertext){
        try {
            return decrypter.doFinal(ciphertext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String ciphertext){
        byte[] plaintext = decrypt(hexStringToByteArray(ciphertext));
        return new String(plaintext);
    }

    private static Cipher getCipher(int mode, SecretKey secretKey){

        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(mode, secretKey);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String byteArrayToHexString(byte[] ba)
    {
        StringBuffer buffer = new StringBuffer(ba.length * 2);
        int n = ba.length;
        for (int i = 0; i < n; i++) {
            byte b = ba[i];
            appendASCII(b, buffer);
        }
        return buffer.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        char[] ca = s.toCharArray();
        int n_chars = ca.length;
        byte[] ba = new byte[n_chars / 2];
        int ci = 0;
        int bi = 0;
        while (ci < n_chars) {
            char c = ca[ci++];
            byte hi = hexCharToByte(c);
            c = ca[ci++];
            byte lo = hexCharToByte(c);
            ba[bi++] = (byte) ((hi << 4) | lo);
        }
        return ba;
    }

    private static void appendASCII(byte b, StringBuffer buffer) {
        byte hi = (byte) ((b >> 4) & 0xf);
        byte lo = (byte) (b & 0xf);
        buffer.append(hexByteToChar(hi));
        buffer.append(hexByteToChar(lo));
    }

    private static char hexByteToChar(byte b) {
        char c = 0;
        switch (b)
        {
        case 0x0: c = '0'; break;
        case 0x1: c = '1'; break;
        case 0x2: c = '2'; break;
        case 0x3: c = '3'; break;
        case 0x4: c = '4'; break;
        case 0x5: c = '5'; break;
        case 0x6: c = '6'; break;
        case 0x7: c = '7'; break;
        case 0x8: c = '8'; break;
        case 0x9: c = '9'; break;
        case 0xa: c = 'a'; break;
        case 0xb: c = 'b'; break;
        case 0xc: c = 'c'; break;
        case 0xd: c = 'd'; break;
        case 0xe: c = 'e'; break;
        case 0xf: c = 'f'; break;
        default: assert false:"Bad byte digit of '" + c + "' received"; break;
        }
        return c;
    }

    private static byte hexCharToByte(char c) {
        byte b = 0;
        switch (c)
        {
        case '0': b = 0x0; break;
        case '1': b = 0x1; break;
        case '2': b = 0x2; break;
        case '3': b = 0x3; break;
        case '4': b = 0x4; break;
        case '5': b = 0x5; break;
        case '6': b = 0x6; break;
        case '7': b = 0x7; break;
        case '8': b = 0x8; break;
        case '9': b = 0x9; break;
        case 'a': case 'A': b = 0xa; break;
        case 'b': case 'B': b = 0xb; break;
        case 'c': case 'C': b = 0xc; break;
        case 'd': case 'D': b = 0xd; break;
        case 'e': case 'E': b = 0xe; break;
        case 'f': case 'F': b = 0xf; break;
        default: assert false:"Bad hex digit of '" + c + "' received"; break;
        }
        return b;
    }
}
