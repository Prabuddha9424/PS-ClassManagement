package com.PSDeveloper.PSClassManagement.view.tm;

import javafx.scene.control.Button;

public class StudentTm {
    private int studentId;
    private String email;
    private String fullName;
    private int grade;

    public StudentTm() {
    }

    public StudentTm(int studentId, String email, String fullName, int grade, Button btn) {
        this.studentId = studentId;
        this.email = email;
        this.fullName = fullName;
        this.grade = grade;
        this.btn = btn;
    }

    private Button btn;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "StudentTm{" +
                "studentId=" + studentId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", grade='" + grade + '\'' +
                ", btn=" + btn +
                '}';
    }
}
