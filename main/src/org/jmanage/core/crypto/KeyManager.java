package org.jmanage.core.crypto;

import org.jmanage.core.util.CoreUtils;

import javax.crypto.SecretKey;
import java.io.*;

/**
 *
 * date:  Jul 23, 2004
 * @author	Rakesh Kalra
 */
public class KeyManager {

    private static final String KEY_FILE_NAME =
                CoreUtils.getConfigDir() + File.separatorChar + "jmanage-key";

    public static void writeKey(EncryptedKey encryptedKey)
        throws FileNotFoundException, IOException {

        File file = new File(KEY_FILE_NAME);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(encryptedKey.get());
        fos.flush();
        fos.close();
    }
    public static EncryptedKey readKey(char[] password) {

        try {
            byte[] encryptedKey = readKey();
            return new EncryptedKey(encryptedKey, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] readKey()
        throws FileNotFoundException, IOException {

        File file = new File(KEY_FILE_NAME);
        BufferedInputStream is =
                new BufferedInputStream(new FileInputStream(file));
        byte[] encryptedKey = new byte[1000];
        int data = is.read();
        int index = 0;
        while(data != -1){
            encryptedKey[index] = (byte)data;
            index ++;
            data = is.read();
        }
        byte[] actualLengthKey = new byte[index];
        for(int i=0; i<index; i++){
            actualLengthKey[i] = encryptedKey[i];
        }
        is.close();
        return actualLengthKey;
    }
}
