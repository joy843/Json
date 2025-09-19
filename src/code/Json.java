package com.company;
import  com.company.Json1035;
import com.company.reflectors.SerialisationReflector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Json {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {

        // Création d'instances pour la sérialisation
        Address address = new Address("123 Main St", "Springfield");
        Address add = new Address("Douala", "Springfield");
        Person person = new Person("Alice", 30, address, Arrays.asList("Lecture", "Voyage", "Sport"));
        Person perso = new Person("Marcy", 3, add, Arrays.asList("Manger", "Boire", "Dormir"));

        // Utilitaire JSON
        Json1035 jsonUtil = new Json1035();
        SerialisationReflector reflector = new SerialisationReflector(person);
        reflector.writeToFile("Person.json", person);

        // Test de sérialisation
        System.out.println("---- Test de sérialisation ----");
        String json = jsonUtil.serialize(person);
        System.out.println("Objet sérialisé en JSON :");
        System.out.println(json);
        String filePath = "Person.json";

        // Test de désérialisation
        System.out.println("\n---- Test de désérialisation ----");
        String jsonChaine = new String(Files.readAllBytes(Paths.get(filePath)));
        //String jsonInput = "{ name: Alice; age: 25; address: { street: 123 Main St; city: Springfield } }";

        // Désérialisation en un objet Person
        Person deserializedPerson = jsonUtil.deserialize(jsonChaine, Person.class);
        System.out.println("Objet désérialisé : " + deserializedPerson);

    }
}