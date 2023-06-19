package com.PSDeveloper.PSClassManagement.controller;

import com.PSDeveloper.PSClassManagement.model.Teacher;
import com.PSDeveloper.PSClassManagement.view.tm.TeacherTm;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeachersFormController {

    public AnchorPane context;
    public TextField txtTeacherId;
    public TextField txtEmail;
    public TextField txtFullName;
    public TextField txtSearch;
    public TableView<TeacherTm> tbl;
    public TableColumn colTeacherId;
    public TableColumn colTeacherEmail;
    public TableColumn colTeacherFullName;
    public TableColumn colTeachingProgramme;
    public JFXButton btnSaveUpdate;
    public TextField txtTeachingProgramme;
    public TableColumn colDelete;
    private String searchText="";
    public void initialize() throws SQLException, ClassNotFoundException {  //inbuild method
        colTeacherId.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        colTeacherEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTeacherFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colTeachingProgramme.setCellValueFactory(new PropertyValueFactory<>("teachingProgramme"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllTeachers(searchText);
        txtTeacherId.setText(Integer.toString(getLastId()));

        tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                btnSaveUpdate.setText("Update Teacher");
                setData(newValue);
            }
        });

    }

    private void setData(TeacherTm newValue) {
        txtTeacherId.setEditable(false);
        txtEmail.setEditable(false);
        txtTeacherId.setText(String.valueOf(newValue.getTeacherId()));
        txtEmail.setText(newValue.getEmail());
        txtFullName.setText(newValue.getFullName());
        txtTeachingProgramme.setText(newValue.getTeachingProgramme());
    }

    private void loadAllTeachers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<TeacherTm> teacherTms= FXCollections.observableArrayList();
        for(Teacher teacher:findAllTeachers()){
            Button btn=new Button("Delete");
            TeacherTm tm=new TeacherTm(
                    teacher.getTeacherId(),teacher.getEmail(),teacher.getFullName(),teacher.getTeachingProgramme(),btn
            );
            teacherTms.add(tm);
        }
        tbl.setItems(teacherTms);
    }


    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm","Dashboard");
    }

    public void btnSaveTeacherOnAction(ActionEvent actionEvent) {
        try {
            if(btnSaveUpdate.getText().equals("Save Teacher")){
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
                String sql="INSERT INTO teacher VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setInt(1, getLastId());
                preparedStatement.setString(2,txtEmail.getText());
                preparedStatement.setString(3,txtFullName.getText());
                preparedStatement.setString(4,txtTeachingProgramme.getText());
                preparedStatement.setInt(5, 1);
                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Teacher Saved!").show();
                    clearFields();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try again!").show();
                }
            }else {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
                String sql="UPDATE teacher SET full_name=?, teaching_programme=?";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,txtFullName.getText());
                preparedStatement.setString(2,txtTeachingProgramme.getText());
                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Teacher Updated!").show();
                    clearFields();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try again!").show();
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Teacher> findAllTeachers() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql="SELECT * FROM teacher";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Teacher> teacherData=new ArrayList<>();
        while (resultSet.next()){
            teacherData.add(new Teacher(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
            ));
        }
        return teacherData;
    }
    public void btnNewTeacherOnAction(ActionEvent actionEvent) {
        btnSaveUpdate.setText("Save Teacher");
        txtEmail.setEditable(true);
        clearFields();
    }
    public static int getLastId(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage",
                    "root","1234");
            String sql="SELECT t_id FROM teacher ORDER BY t_id DESC LIMIT 1";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }else{
                return 1;
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setUi(String location,String title) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }
    private void clearFields() {
        txtEmail.clear();
        txtFullName.clear();
        txtTeachingProgramme.clear();
    }
}
