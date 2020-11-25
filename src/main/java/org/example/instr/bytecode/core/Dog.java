package org.example.instr.bytecode.core;

public class Dog {
    String dogName;
    int dogAge;

    public Dog() {
        dogName = "Kaaju";
        dogAge = 5;
    }

    public String getDog(String bark) {
        return String.format("%s! %s is %s years old.", bark, dogName, dogAge);
    }

    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.getDog("Boof");
    }
}
