package symmetriccipher;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import util.Util;

public class SymmetricCipherTesterFile {
    public static void main(String[] args) throws Exception {
        //Cargar la llave
        //SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        //secretKey = (SecretKey) Util.loadObject("secretKey.key");
        //System.out.println("The symmetrical key has been recovered");

        //SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        //cipher.encryptFile("Dragones copy.webp");
        //cipher.decryptFile("Dragones copy.webp.encrypted");
        SymmetricCipher.encryptFolder("./Server", "Adios", "./Adios/secretKey1.key");
    }
}
