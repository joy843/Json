package code;

import annotations.Alias;
import annotations.Ignored;

import java.util.ArrayList;
import java.util.List;

public class Person {
    public String name;
    public int age;
    public Address address;
    public List<String> hobbies;
    // Constructeur par défaut
    public Person() {
        this.hobbies = new ArrayList<>(); // Initialise la liste des hobbies
    }
    // Constructeur avec arguments
    public Person(String name, int age, Address address, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies != null ? hobbies : new ArrayList<>();
    }

    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public List<String> getHobbies() { return hobbies; }
    public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", hobbies=" + hobbies +
                '}';
    }
}
