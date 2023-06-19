package com.PSDeveloper.PSClassManagement.controller;

import com.PSDeveloper.PSClassManagement.util.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class SignUpFormController {
    public TextField txtEmail;
    public TextField txtPassword;
    public TextField txtFirstName;
    public TextField txtLastName;
    public AnchorPane context;

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage",
                    "root","1234");
            String sql="INSERT INTO user VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,txtEmail.getText().toLowerCase());
            preparedStatement.setString(2, PasswordManager.encryptPassword(txtPassword.getText().trim()));
            preparedStatement.setString(3,txtFirstName.getText());
            preparedStatement.setString(4,txtLastName.getText());
            if (preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION,"User Saved!").show();
                clearFields();
                setUi("LoginForm","Login");
            }else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        }
    }

    private void clearFields() {
        txtEmail.clear();
        txtPassword.clear();
        txtFirstName.clear();
        txtLastName.clear();
    }

    public void btnAllreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm","Login");
    }
    private void setUi(String location,String title) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }
}
