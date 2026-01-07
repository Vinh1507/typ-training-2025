package com.example.qlsv.model;

public class Student {
    private int id;
    private String name;
    private int age;
    private String nganhhoc;

    public Student () {}

    public Student (int id, String name, int age, String nganhhoc) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nganhhoc = nganhhoc;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getNganhhoc() {
        return nganhhoc;
    }
    public void setNganhhoc(String nganhhoc) {
        this.nganhhoc = nganhhoc;
    }
}
