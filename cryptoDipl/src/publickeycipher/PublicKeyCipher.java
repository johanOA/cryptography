package publickeycipher;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Util;

public class PublicKeyCipher {
    private Cipher cipher;

    public PublicKeyCipher(String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance(algorithm);
    }
    public byte[] encryptMessage(String input, Key key ) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherText = null;
        byte[] clearText = input.getBytes();

        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(clearText);

        return cipherText;
    }

    public String decryptMessage(byte[] input, Key key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String output = "";

        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] clearText = cipher.doFinal(input);
        output = new String(clearText);

        return output;
    }

    public byte[] encryptObject(Object input, Key key)  throws
        InvalidKeyException,
        IllegalBlockSizeException,
        BadPaddingException, IOException {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] clearObject = Util.objectToByteArray(input);
            byte[] cipherObject = cipher.doFinal(clearObject);

            return cipherObject;
    }

    public Object decrypyObject(byte[] input, Key key) throws
        InvalidKeyException,
        IllegalBlockSizeException,
        BadPaddingException, IOException, ClassNotFoundException {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] clearText = cipher.doFinal(input);
            Object output = Util.byteArrayToObject(clearText);

            return output;
        }
    
}
