package com.PSDeveloper.PSClassManagement.model;

public class Programme {
    private int programmeId;
    private String programmeName;
    private String programmeDesc;
    private String userEmail;

    public Programme() {
    }

    public Programme(int programmeId, String programmeName, String programmeDesc, String userEmail) {
        this.programmeId = programmeId;
        this.programmeName = programmeName;
        this.programmeDesc = programmeDesc;
        this.userEmail = userEmail;
    }

    public int getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getProgrammeDesc() {
        return programmeDesc;
    }

    public void setProgrammeDesc(String programmeDesc) {
        this.programmeDesc = programmeDesc;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "programmeId=" + programmeId +
                ", programmeName='" + programmeName + '\'' +
                ", programmeDesc='" + programmeDesc + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
