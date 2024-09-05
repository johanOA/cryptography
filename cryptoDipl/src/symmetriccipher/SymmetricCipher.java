package symmetriccipher;


import java.lang.Thread;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import util.Base64;
import util.Util;

public class SymmetricCipher {
    private SecretKey secretKey;
    private Cipher cipher;

    public static void pause(int miliseconds) throws Exception {
		Thread.sleep(miliseconds);
	}


    public static void decryptFolder( String foldername,String key) throws Exception{

        SecretKey secretKey = (SecretKey) Util.loadObject(key);
        System.out.println("The symmetrical key has been recovered");
        
        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");


        File folder1 = new File(foldername);

        if(folder1.exists() && folder1.isDirectory()) {
			File[] files = folder1.listFiles();

			if (files != null) {
                System.out.println("Número de archivos en la carpeta: " + files.length);
                int count = 0;
                for (File file : files) {
                    if (count < files.length - 1) {
                        cipher.decryptFile(foldername + "/" + file.getName());
                        System.out.println("Desencriptado: " + file.getName());
                    }
                    count++;
                }
			}
		}
    }

    public static void encryptFolder( String routefolder, String foldername, String key) throws Exception{
		
        SecretKey secretKey = (SecretKey) Util.loadObject(key);
        System.out.println("The symmetrical key has been recovered");

        SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        File folder1 = new File(foldername);
		File newFolder = new File(routefolder, foldername);

        if (newFolder.mkdirs()) {
			System.out.println("Creado exitosamente.");
		} else {
			System.out.println("No se pudo crear");
		}
        if(folder1.exists() && folder1.isDirectory()) {
			File[] files = folder1.listFiles();

			if (files != null) {
                System.out.println("Número de archivos en la carpeta: " + files.length);
                int count = 0;
                for (File file : files) {
                    if (count < files.length - 1) {
                        cipher.encryptFile(foldername + "/" + file.getName());
                        System.out.println("Encriptado: " + file.getName());
                    }
                    count++;
                }
			}
		}
	}

    public void decryptFile(String fileRoute) throws Exception {
        
        String newPart = ".plain";

        List<String> lines64 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileRoute))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines64.add(line);
            }
        }

        List<String> destroyEncrypt = new ArrayList<>();
        List<byte[]> buildList = new ArrayList<>();

        for (String bs : lines64) {
            buildList.add(Base64.decode(bs));
        }
        
        for (byte[] bs : buildList) {
            destroyEncrypt.add(decryptMessage(bs));
        }
        String content = combineStringList(destroyEncrypt);

        String nameFile = modifyFileName(fileRoute, newPart);
        try {
            byte[] bytes = hexStringToByteArray(content);
            try(FileOutputStream fos = new FileOutputStream(nameFile)){
                fos.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len/2];
        for (int i=0;i<len;i+=2){
            data[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static String modifyFileName(String fileName, String newPart) {
        String[] parts = fileName.split("\\.");

        if (parts.length < 2) {
            //si no hay suficiente
            return fileName;
        }

        //Construir el nuevo nombre
        StringBuilder newFileName = new StringBuilder();
        newFileName.append(parts[0]);

        //Nueva parte en el medio
        newFileName.append(newPart);

        //Partes restantes, excluye la ultima
        for (int i = 1; i < parts.length - 1; i++) {
            newFileName.append(".").append(parts[i]);
        }

        //Añade ultima parte si la hay
        //newFileName.append(".").append(parts[parts.length - 1 ]);

        return newFileName.toString();

    }

    public static String combineStringList(List<String> stringsList) {
        StringBuilder sb = new StringBuilder();

        for (String string : stringsList) {
            sb.append(string);
        }

        return sb.toString();
    }
    public void encryptFile(String filename) throws Exception {
		
        if (filename == null) {
            throw new IllegalArgumentException("Filename or outputFilename cannot be null");
        }
        
        System.out.println("File to send: " + filename);
        File localFile = new File(filename);
        try (BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile))) {
            // Send the size of the file (in bytes)
            long size = localFile.length();
            System.out.println("Size: " + size);

            pause(50);

            // The file is sent one block at a time
            byte[] blockToSend = new byte[1024];
            int in;
            List<byte[]> blocks = new ArrayList<>();

            while ((in = fromFile.read(blockToSend)) != -1) {
                // Copy the read bytes into a new byte array of exact size and add it to the list
                byte[] block = new byte[in];
                System.arraycopy(blockToSend, 0, block, 0, in);
                blocks.add(block);
            }

            List<String> blockStrings = new ArrayList<>();

            try {
                for (byte[] br : blocks) {
                    blockStrings.add(Util.byteArrayToHexString(br, ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<byte[]> blockEncrypt = new ArrayList<>();
            
            for (String line : blockStrings) {
                blockEncrypt.add(encryptMessage(line));
            }

            List<String> blockBase64 = new ArrayList<>();
            
            for (byte[] bs : blockEncrypt) {
                blockBase64.add(Base64.encode(bs));
            }

            String content = String.join("\n", blockBase64);

            try {
                crearArchivo(filename+".encrypted", content);
                System.out.println("Archivo encriptado correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public static void crearArchivo(String rutaArchivo, String contenido) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(contenido);
        }
    }

    public SymmetricCipher(SecretKey secretKey, String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.secretKey = secretKey;
        cipher = Cipher.getInstance(transformation);
    }

    public byte[] encryptMessage(String input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] clearText = input.getBytes();
        byte[] cipherText = null;

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        cipherText = cipher.doFinal(clearText);

        return cipherText;
    }   
    
    public List<String> decryptTextFile(String inputRoute) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputRoute))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        List<String> destroyEncrypt = new ArrayList<>();
        List<byte[]> buildList = new ArrayList<>();

        for (String bs : lines) {
            buildList.add(Base64.decode(bs));
        }
        for (byte[] bs : buildList) {
            destroyEncrypt.add(decryptMessage(bs));
        }
        return destroyEncrypt;
    }
    public List<String> encryptTextFile(String inputRoute) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputRoute))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        
        List<byte[]> buildList = new ArrayList<>();
        
        for (String line : lines) {
            buildList.add(encryptMessage(line));
        }

        List<String> buildListBase64 = new ArrayList<>();
        
        for (byte[] bs : buildList) {
            buildListBase64.add(Base64.encode(bs));
        } 

        return buildListBase64;
        
    }

    public String decryptMessage(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String output = "";
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] clearText = cipher.doFinal(input);
        output = new String(clearText);

        return output;
    }

    public byte[] encryptObject(Object input) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherObject = null;
        byte[] clearObject = null;

        clearObject = Util.objectToByteArray(input);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        cipherObject = cipher.doFinal(clearObject);
        
        return cipherObject;
    }

    public Object descrypObject(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
        Object output = null;

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] clearObject = cipher.doFinal(input);

        output = Util.byteArrayToObject(clearObject);
        return output;
    }
    
}
