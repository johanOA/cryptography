package util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;;

public class Util {
    public static Object Base64;

    public static String byteArrayToHexString(byte[] bytes, String separator) {
            String result = "";

        for (int i=0; i<bytes.length; i++) {
            result += String.format("%02x", bytes[i]) + separator;
        }
        return result.toString();
    }

    public static void saveObject(Object o, String filename) throws IOException {
        FileOutputStream fileOut;
        ObjectOutputStream out;

        fileOut = new FileOutputStream(filename);
        out = new ObjectOutputStream(fileOut);

        out.writeObject(o);

        out.flush();
        out.close();
    }

    public static Object loadObject (String filename) throws IOException, ClassNotFoundException, InterruptedException {
        FileInputStream fileIn;
        ObjectInputStream in;

        fileIn = new FileInputStream(filename);
        in = new ObjectInputStream(fileIn);

        Thread.sleep(100);
        
        Object o = in.readObject();

        fileIn.close();
        in.close();

        return o;
    }


    public static byte[] objectToByteArray(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(o);
        out.close();
        byte[] buffer = bos.toByteArray();

        return buffer;
    }
    public static Object byteArrayToObject(byte[] byteArray) throws IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        Object o = in.readObject();
        in.close();

        return o;
    }

    public static String showRSA(byte[] bs, String keyType) {
        // Codificar la clave en Base64
        String pr = util.Base64.encode(bs);
    
        // Dividir la cadena en líneas de 64 caracteres
        StringBuilder formattedKey = new StringBuilder();
        formattedKey.append("-----BEGIN ").append(keyType).append("-----\n");
        
        for (int i = 0; i < pr.length(); i += 64) {
            formattedKey.append(pr, i, Math.min(i + 64, pr.length())).append("\n");
        }
        formattedKey.append("-----END ").append(keyType).append("-----");
        return formattedKey.toString();
    }

    public static byte[][] split(byte[] data, int blockSize) {
        int length = data.length;
        
        // Calcula el número de bloques necesarios
        int numBlocks = (int) Math.ceil((double) length / blockSize);
        
        // Crear el arreglo de bloques
        byte[][] blocks = new byte[numBlocks][];
        
        for (int i = 0; i < numBlocks; i++) {
            int start = i * blockSize;
            int end = Math.min(start + blockSize, length);
            blocks[i] = new byte[end - start];
            System.arraycopy(data, start, blocks[i], 0, blocks[i].length);
        }
        
        return blocks;
    }

    public static byte[] join(byte[][] byteArrays) {
        // Calcular el tamaño total del nuevo arreglo
        int totalLength = 0;
        for (byte[] array : byteArrays) {
            totalLength += array.length;
        }

        // Crear el nuevo arreglo de bytes
        byte[] joinedArray = new byte[totalLength];
        int currentIndex = 0;

        // Copiar cada arreglo de bytes en el nuevo arreglo
        for (byte[] array : byteArrays) {
            System.arraycopy(array, 0, joinedArray, currentIndex, array.length);
            currentIndex += array.length;
        }

        return joinedArray;
    }

    public static String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return util.Base64.encode(encryptedBytes);
    }
    public static SecretKeySpec generateKey(String key) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        keyBytes = sha.digest(keyBytes);
        return new SecretKeySpec(Arrays.copyOf(keyBytes, 16), "AES");
    }
    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(util.Base64.decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
