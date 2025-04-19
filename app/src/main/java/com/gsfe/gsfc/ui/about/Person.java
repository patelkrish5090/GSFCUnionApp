package com.gsfe.gsfc.ui.about;

public class Person {
    private String name;
    private String designation;
    private String phone;
    private String position; // New field

    public Person() {
        // Default constructor required for Firebase
    }

    public Person(String name, String designation, String phone, String position) {
        this.name = name;
        this.designation = designation;
        this.phone = phone;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }
}