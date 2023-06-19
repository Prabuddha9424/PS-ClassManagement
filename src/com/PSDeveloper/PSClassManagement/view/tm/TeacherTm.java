package com.PSDeveloper.PSClassManagement.view.tm;

import javafx.scene.control.Button;

public class TeacherTm {//Tm-Table Model
    private int teacherId;
    private String email;
    private String fullName;
    private String teachingProgramme;
    private Button btn;

    public TeacherTm() {
    }

    public TeacherTm(int teacherId, String email, String fullName, String teachingProgramme, Button btn) {
        this.teacherId = teacherId;
        this.email = email;
        this.fullName = fullName;
        this.teachingProgramme = teachingProgramme;
        this.btn = btn;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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

    public String getTeachingProgramme() {
        return teachingProgramme;
    }

    public void setTeachingProgramme(String teachingProgramme) {
        this.teachingProgramme = teachingProgramme;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "TeacherTm{" +
                "teacherId=" + teacherId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", teachingProgramme='" + teachingProgramme + '\'' +
                ", btn=" + btn +
                '}';
    }
}
