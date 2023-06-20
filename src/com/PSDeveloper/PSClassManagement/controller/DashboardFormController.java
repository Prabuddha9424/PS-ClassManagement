package com.PSDeveloper.PSClassManagement.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DashboardFormController {
    public Label lblDate;
    public Label lblTime;
    public AnchorPane context;

    public void initialize(){
        setData();
    }

    private void setData() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Timeline timeline=new Timeline(
                new KeyFrame(Duration.seconds(0),
                        e->{
                            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("HH:mm:ss");
                            lblTime.setText(LocalTime.now().format(dateTimeFormatter));
                        }
                        ),new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void btnProgrammeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProgrammeForm","Programme Management");
    }

    public void btnTeachersOnAction(ActionEvent actionEvent) throws IOException {
        setUi("TeachersForm","Teacher Management");
    }

    public void btnSitudentsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("StudentForm","Student Management");
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm","Login");
    }
    private void setUi(String location,String title) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }

}
