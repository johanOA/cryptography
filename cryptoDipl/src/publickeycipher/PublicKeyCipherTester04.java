package publickeycipher;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Util;

public class PublicKeyCipherTester04 {
    public static void main(String[] args) throws
    NoSuchAlgorithmException,
    NoSuchPaddingException,
    InvalidKeyException,
    IllegalBlockSizeException,
    BadPaddingException, IOException{
        String algoritm = "RSA";
            
            //Generacion de llaves publicas y privadas
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algoritm);
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();


            PublicKeyCipher cipher = new PublicKeyCipher(algoritm);
            String clearText = "In public key cryptography, the same key is used to encrypt and decrypt the text.";
            byte[] encryptedText = cipher.encryptMessage(clearText, publicKey);


            
            String rsaPublic = Util.showRSA(publicKey.getEncoded(), "PUBLIC KEY");
            String rsaPrivate = Util.showRSA(privateKey.getEncoded(), "PRIVATE KEY");

            Util.saveObject(rsaPublic, "public_key.pem");
            System.out.println("Public key has been saved");
            Util.saveObject(rsaPrivate, "private_key.pem");
            System.out.println("Private key has been saved");
            Util.saveObject(encryptedText, "text.encrypted");
            System.out.println("Text has been saved");
    }
}
