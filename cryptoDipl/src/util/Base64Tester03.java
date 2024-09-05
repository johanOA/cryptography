package util;
import java.io.IOException;
import java.util.ArrayList;

import model.Person;

public class Base64Tester03 {
    public static void main(String[] args) {
        ArrayList<Person> persons = new ArrayList<Person>();
        Person francisco = new Person("Francisco", 29, 1.78);
        Person diana = new Person("diana", 22, 1.58);
        Person alberto = new Person("alberto", 35, 1.82);
        Person laura = new Person("laura", 19, 1.63);

        persons.add(francisco);
        persons.add(diana);
        persons.add(alberto);
        persons.add(laura);

        System.out.println(persons);

        try {
            byte[] personsBA = Util.objectToByteArray(persons);
            String personsB64 = Base64.encode(personsBA);
            System.out.println(personsB64);

            byte[] personsBA2 = Base64.decode(personsB64);
            ArrayList<Person> persons2 = (ArrayList<Person>) Util.byteArrayToObject(personsBA2);
            System.out.println(persons2);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }   
}
