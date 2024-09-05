package signature;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import util.Util;

public class SignerTester02 {
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String filename = "images.jpeg";
        byte[] digitalSignature = Siigner.signFile(filename, "SHA256withRSA", privateKey);
        System.out.println(Util.byteArrayToHexString(digitalSignature, ""));

        boolean isVerified = Signer.verifyFileSignature(filename, "SHA256withRSA", publicKey, digitalSignature);
        System.out.println("Firma verificada: "+ isVerified);
    }
}
