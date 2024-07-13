package com.example.gsfc.ui.about;

public class Person {
    private String name;
    private String designation;
    private String phone;

    public Person() {

    }


    public Person(String name, String designation, String phone) {
        this.name = name;
        this.designation = designation;
        this.phone = phone;
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
}

