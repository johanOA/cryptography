package restriccionesRSA;

import util.Util;

public class TestSplitJoin {
    public static void main(String[] args) {
        // Datos de prueba
        String testData = "Este es un ejemplo de datos que se dividirán en bloques y luego se unirán de nuevo.";
        byte[] originalData = testData.getBytes();
        
        // Tamaño de bloque 
        int blockSize = 20;

        // Dividir los datos en bloques
        byte[][] blocks = Util.split(originalData, blockSize);

        // Unir los bloques de nuevo en un solo arreglo
        byte[] joinedData = Util.join(blocks);

        // Verificar que los datos originales y los datos unidos sean iguales
        boolean areEqual = java.util.Arrays.equals(originalData, joinedData);
        
        // Imprimir resultados
        System.out.println("Original Data: " + new String(originalData));
        System.out.println("Joined Data: " + new String(joinedData));
        System.out.println("Are original and joined data equal? " + areEqual);
    }

}
