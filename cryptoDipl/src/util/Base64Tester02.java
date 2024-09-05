package util;

import java.util.ArrayList;
import java.io.*;

import model.Person;

public class Base64Tester02 {
    public static void main(String[] args) throws IOException{
        String personBase64 = "";
        Person person = new Person("Francisco", 29, 1.78);

        System.out.println(person);

        try (FileOutputStream fileOut = new FileOutputStream("person.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(person);
            System.out.println("Objeto Person serializado exitosamente");
        } catch (IOException i) {
            i.printStackTrace();
        }

        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(person);
            byte[] personBytes = byteOut.toByteArray();
            
            personBase64 = Base64.encode(personBytes);

            System.out.println("Objeto Person en Base64: " + personBase64);
        } catch (IOException i) {
            i.printStackTrace();
        }
        
        

        byte[] personBytes = Base64.decode(personBase64);

        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(personBytes);
             ObjectInputStream in = new ObjectInputStream(byteIn)) {
            Person person2Person = (Person) in.readObject();
            System.out.println("Objeto Person deserializado desde Base64: " + person2Person);
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }



    }
}
