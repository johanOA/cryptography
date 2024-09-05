package symmetriccipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.util.Scanner;

import java.io.*;

import util.Util;

public class SymmetricCipherTesterTextFile {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException, InterruptedException {
        
        @SuppressWarnings("resource")
        final Scanner SCANNER = new Scanner(System.in);
        
        //Pide el archivo
        System.out.println("Ruta del archivo a encriptar: ");
        String pathFileEncrypt = SCANNER.nextLine();

        String nameFile = obtenerNombreArchivo(pathFileEncrypt);


        //Generar la llave
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        Util.saveObject(secretKey, "secretkey.key");
        System.out.println("The secret key has been saved");

        //Cargar la llave
        secretKey = (SecretKey) Util.loadObject("secretKey.key");
        System.out.println("The symmetrical key has been recovered");

        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        //Encripta el archivo
        List<String> encryptFile = cipher.encryptTextFile(nameFile);

        //contenido para el archivo a encriptar
        String content = String.join("\n", encryptFile);

        //nombre para el archivo que se crea encryptado
        String pathWithEncryptedFile = pathFileEncrypt + ".encrypted";

        try {
            crearArchivo(pathWithEncryptedFile, content);
            System.out.println("Archivo creado correctamente: " + pathWithEncryptedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Pide el archivo
        System.out.println("Ruta del archivo a desencriptar: ");
        String pathFiledecryption = SCANNER.nextLine();

        String nameFile2 = obtenerNombreArchivo(pathFiledecryption);

        //Desencripta el archivo
        List<String> decryptionFile = cipher.decryptTextFile(nameFile2);

        //contenido para el archivo a encriptar
        String content2 = String.join("\n", decryptionFile);

        //nombre para el archivo que se crea encryptado
        String pathWithDecryptedFile = pathFiledecryption + ".dencrypted";

        try {
            crearArchivo(pathWithDecryptedFile, content2);
            System.out.println("Archivo creado correctamente: " + pathWithDecryptedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String obtenerNombreArchivo(String rutaArchivo) {
        int index = rutaArchivo.lastIndexOf("/");
        if (index >= 0) {
            return rutaArchivo.substring(index + 1);
        }
        return rutaArchivo;
    }

    public static void crearArchivo(String rutaArchivo, String contenido) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(contenido);
        }
    }
        
}
