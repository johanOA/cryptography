package symmetriccipher;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javax.crypto.SecretKey;

import util.Util;

import javax.crypto.KeyGenerator;

public class SymmetricCipherTester04 {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException{
        SecretKey secretKey = null;

        secretKey = KeyGenerator.getInstance("DES").generateKey();

        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        ArrayList<String> clearObject = new ArrayList<String>();
        byte[] encryptObject = null;

        clearObject.add("Ana");
        clearObject.add("Bety");
        clearObject.add("Carolina");
        clearObject.add("Daniela");
        clearObject.add("Elena");

        System.out.println(clearObject);

        encryptObject = cipher.encryptObject(clearObject);

        System.out.println(Util.byteArrayToHexString(encryptObject, " "));

        clearObject = (ArrayList<String>) cipher.descrypObject(encryptObject);
        System.out.println(clearObject);
    }
}
