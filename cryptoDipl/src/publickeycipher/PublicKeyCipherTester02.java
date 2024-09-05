package publickeycipher;

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

public class PublicKeyCipherTester02 {
    public static void main(String[] args) throws
        NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        IllegalBlockSizeException,
        BadPaddingException {
            String algoritm = "RSA";
            
            //Generacion de llaves publicas y privadas
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algoritm);
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            System.out.println(Util.showRSA(publicKey.getEncoded(), "PUBLIC KEY"));
            System.out.println(Util.showRSA(privateKey.getEncoded(), "PRIVATE KEY"));


    }

}
