package restriccionesRSA;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.Cipher;

import util.Util;

public class RSAEncryptionDemo {

    public static void main(String[] args) throws Exception {
        // Generar un par de llaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Generar datos que superen el tamaño máximo permitido por RSA
        byte[] data = new byte[256];
        Arrays.fill(data, (byte) 'A');

        // Dividir los datos en bloques que pueden ser encriptados
        int maxBlockSize = 245; // Máximo tamaño permitido para encriptar con RSA 2048 bits con padding PKCS1
        byte[][] dataBlocks = Util.split(data, maxBlockSize);
        

        // Encriptar cada bloque usando la llave pública
        byte[][] encryptedBlocks = new byte[dataBlocks.length][];
        for (int i = 0; i < dataBlocks.length; i++) {
            encryptedBlocks[i] = encrypt(dataBlocks[i], publicKey);
        }

        // Desencriptar cada bloque usando la llave privada
        byte[][] decryptedBlocks = new byte[encryptedBlocks.length][];
        for (int i = 0; i < encryptedBlocks.length; i++) {
            decryptedBlocks[i] = decrypt(encryptedBlocks[i], privateKey);
        }

        // Unir los bloques desencriptados para obtener el byte[] inicial
        byte[] decryptedData = Util.join(decryptedBlocks);

        // Verificar que los datos desencriptados coincidan con los datos originales
        boolean areEqual = Arrays.equals(data, decryptedData);
        System.out.println("Are original and decrypted data equal? " + areEqual);
    }

    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
}

