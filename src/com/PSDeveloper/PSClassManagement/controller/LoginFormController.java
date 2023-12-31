package com.PSDeveloper.PSClassManagement.controller;

import com.PSDeveloper.PSClassManagement.db.DbConnection;
import com.PSDeveloper.PSClassManagement.util.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public TextField txtPassword;
    public static String newUser;

    public void btnLoginOnAction(ActionEvent actionEvent) throws RuntimeException {
        try {
            String sql="SELECT * FROM user WHERE email=?";
            PreparedStatement preparedStatement= DbConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1,txtEmail.getText());

            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                if(PasswordManager.checkPassword(txtPassword.getText().trim(),resultSet.getString("password"))){
                    setUi("DashboardForm","Dashboard");
                    newUser=txtEmail.getText();
                    System.out.println(newUser);
                }
                else {
                    new Alert(Alert.AlertType.WARNING,"Password wrong! Please try again").show();
                }

            }else {
                new Alert(Alert.AlertType.WARNING,"User email not found!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void btnCreateAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignUpForm","SignUp");
    }

    public void forgotPasswordOnAction(ActionEvent actionEvent) {
    }
    private void setUi(String location,String title) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }
}
