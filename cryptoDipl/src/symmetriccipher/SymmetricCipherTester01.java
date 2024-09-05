package symmetriccipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import util.Util;

public class SymmetricCipherTester01 {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        
        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        String clearText = "In symmetric key crytography, the same key is used " + "to encrypt and decrypt the clear text.";
        System.out.println(clearText);

        byte[] encryptedText = cipher.encryptMessage(clearText);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));

        String clearText2 = cipher.decryptMessage(encryptedText);
        System.out.println(clearText2);
    }
}
