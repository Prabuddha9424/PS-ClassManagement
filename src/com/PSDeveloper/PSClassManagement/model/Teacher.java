package com.PSDeveloper.PSClassManagement.model;

public class Teacher {
    private int teacherId;
    private String email;
    private String fullName;
    private String teachingProgramme;
    private int programmeId;

    public int getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
    }

    public Teacher() {
    }

    public Teacher(int teacherId, String email, String fullName, String teachingProgramme,int programmeId) {
        this.teacherId = teacherId;
        this.email = email;
        this.fullName = fullName;
        this.teachingProgramme = teachingProgramme;
        this.programmeId = programmeId;
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

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", teachingProgramme='" + teachingProgramme + '\'' +
                ", programmeId='" + programmeId + '\'' +
                '}';
    }
}
