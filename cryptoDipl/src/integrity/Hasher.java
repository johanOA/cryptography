package integrity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import util.Util;

public class Hasher {
    public static String getHashFile(String filename, String algorithm) throws Exception, IOException, NoSuchAlgorithmException {
        MessageDigest hasher = MessageDigest.getInstance(algorithm);
        
        try (FileInputStream fis = new FileInputStream(filename)){
            byte[] buffer = new byte[1024];
            int in;
            while ((in = fis.read(buffer)) != -1) {
                hasher.update(buffer, 0, in);    
            }
            fis.close();
        }

        return Util.byteArrayToHexString(hasher.digest(), "");
    }

    public List<String> getFileNames(String directory) {
        List<String> fileNames = new ArrayList<>();

        try {
            Files.list(Paths.get(directory)).forEach(path -> {
                if(Files.isRegularFile(path)) {
                    fileNames.add(path.getFileName().toString());
                }
            });
        } catch (IOException e) {
            System.out.println("Error al leer directorio" + e.getMessage());
        }

        return fileNames;
    }

    public static void generateIntegrityCheckerFile(String foldername, String filehash, List<String> data) {
        String filePath = Paths.get(foldername, filehash).toString();

        StringBuilder content = new StringBuilder();

        for(String item : data ) {
            try {
                String hash = getHashFile(foldername+item, "SHA-256");
                content.append(hash).append("  ").append(foldername).append(item).append("\n");
            } catch (IOException | NoSuchAlgorithmException e) {
                System.err.println("Error al calcular el hash para el archivo " + item + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error insperado" + item + ": " + e.getMessage());
            }
        }

        //Escribe
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content.toString());
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo " + e.getMessage());
        }

    }

    public static List<List<String>> readFileAndProcessLines(String filePath) {
        List<List<String>> allWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");

                List<String> wordList = new ArrayList<>();

                for (String word : words) {
                    wordList.add(word);
                }

                allWords.add(wordList);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo " + e.getMessage());
        }
        return allWords;
    }

    public static void checkIntegrityFile(String hashFilePath) {
        List<String> results = new ArrayList<>();
        int filesNotFound = 0;
        int checksumMismatches = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(hashFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length < 2) {
                    results.add(line + ": FAILED - Malformed line");
                    continue;
                }

                String expectedHash = parts[0];
                String filePath = parts[1];

                try {
                    String actualHash = getHashFile(filePath, "SHA-256");

                    if (expectedHash.equals(actualHash)) {
                        results.add(filePath + ": OK");
                    } else {
                        results.add(filePath + ": FAILED");
                        checksumMismatches++;
                    }
                } catch (IOException e) {
                    results.add(filePath + ": No such file or directory");
                    filesNotFound++;
                } catch (Exception e) {
                    results.add(filePath + ": FAILED open or read");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de hashes: " + e.getMessage());
        }

        results.forEach(System.out::println);

        if (filesNotFound > 0) {
            String message = "WARNING: " + filesNotFound + " listed file" + (filesNotFound > 1 ? "s could not be read" : " could not be read");
            System.out.println(message);
        }

        if (checksumMismatches > 0) {
            String message = "WARNING: " + checksumMismatches + " computed checksum" + (checksumMismatches > 1 ? "s did NOT match" : " did NOT match");
            System.out.println(message);
        }
    }
    /* 
    public static void checkIntegrityFile(List<List<String>> matriz, List<String> data) {
        List<String> matchingRows = new ArrayList<>(); 
        int columnToCompare = 1;
        int itemnumber = 2;
        String hash = "";

        for(int i = 0; i<matriz.size(); i++) {
            List<String> row = matriz.get(i);
            if (columnToCompare < row.size()) {
                String value = row.get(columnToCompare);
                hash = getHashFile(data.get(i), "SHA-256");
                
                if(value.equals(hash)) {
                    matchingRows.add(hash);
                    System.out.println(matriz.get(i));
                }
                
            }
        }
    } 
        */

    
}
