package integrity;

import java.util.List;

public class HasherTester2 {
    public static void main(String[] args) throws Exception{
        Hasher hasher = new Hasher();
        //String directory = "files";
        String fileName = "hasherfile.txt";
        
        
        List<String> fileNames1 = hasher.getFileNames("Hola");
        List<String> fileNames2 = hasher.getFileNames("Adios");
    
        System.out.println("Archivos:" + fileNames1);
        System.out.println("Archivos:" + fileNames2);        

        Hasher.generateIntegrityCheckerFile("./Hola/", fileName, fileNames1);
        Hasher.generateIntegrityCheckerFile("./Adios/", fileName, fileNames2);

        //String filePath = "./files/hasherfile.txt"; 

        List<List<String>> wordsInFile1 = Hasher.readFileAndProcessLines("./Hola/hasherfile.txt");
        List<List<String>> wordsInFile2 = Hasher.readFileAndProcessLines("./Adios/hasherfile.txt");

        //System.out.println("Integridad archivos origen:");
        //hasher.checkIntegrityFile("./Hola/hasherfile.txt");
        //hasher.checkIntegrityFile("./Adios/hasherfile.txt");

        // Imprime las palabras procesadas
        //for (List<String> lineWords : wordsInFile) {
        //    System.out.println(lineWords);
        //}

    }
}
