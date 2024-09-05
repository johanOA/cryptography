package restriccionesRSA;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC_STRING.length());
            sb.append(ALPHANUMERIC_STRING.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // Prueba del método con diferentes tamaños
        int[] rsaKeySizes = {1024, 2048, 3072, 4096};
        for (int keySize : rsaKeySizes) {
            int maxSize = calculateMaxEncryptableSize(keySize);
            String randomString = generateRandomString(maxSize);
            System.out.println("RSA Key Size: " + keySize + " bits");
            System.out.println("Max Encryptable Size: " + maxSize + " bytes");
            System.out.println("Random String: " + randomString);
            System.out.println();
        }
    }

    private static int calculateMaxEncryptableSize(int keySize) {
        
        int keySizeInBytes = keySize / 8;
        return keySizeInBytes - 11; // espacio para padding 
    }
}

