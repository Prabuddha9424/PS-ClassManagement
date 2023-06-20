package com.PSDeveloper.PSClassManagement.view.tm;

import javafx.scene.control.Button;

public class ProgrammeTm {
    private int programmeId;
    private String programmeName;
    private String programmeDesc;
    private Button btn=new Button();

    public ProgrammeTm() {
    }

    public ProgrammeTm(int programmeId, String programmeName, String programmeDesc, Button btn) {
        this.programmeId = programmeId;
        this.programmeName = programmeName;
        this.programmeDesc = programmeDesc;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "ProgrammeTm{" +
                "programmeId=" + programmeId +
                ", programmeName='" + programmeName + '\'' +
                ", programmeDesc='" + programmeDesc + '\'' +
                ", btn=" + btn +
                '}';
    }
}
