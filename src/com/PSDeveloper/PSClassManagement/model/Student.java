package com.PSDeveloper.PSClassManagement.model;

public class Student {
    private int id;
    private String email;
    private String fullName;
    private int grade;

    public Student() {
    }

    public Student(int id, String email, String fullName, int grade) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.grade = grade;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
