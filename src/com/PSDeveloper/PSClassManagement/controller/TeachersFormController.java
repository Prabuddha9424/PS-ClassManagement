package com.PSDeveloper.PSClassManagement.controller;

import com.PSDeveloper.PSClassManagement.model.Teacher;
import com.PSDeveloper.PSClassManagement.view.tm.TeacherTm;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.Optional;

import static javafx.scene.control.ButtonType.YES;

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
    public TableColumn colDelete;
    public ChoiceBox choiceBox;
    private String searchText="";
    private int programmeId;
    public String choice="";
    public void initialize() throws SQLException, ClassNotFoundException {  //inbuild method
        colTeacherId.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        colTeacherEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTeacherFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colTeachingProgramme.setCellValueFactory(new PropertyValueFactory<>("teachingProgramme"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btn"));
        choiceBox.getItems().addAll(setChoiceBox());
        loadAllTeachers(searchText);
        txtTeacherId.setText(Integer.toString(getLastId()));


        tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                btnSaveUpdate.setText("Update Teacher");
                setData(newValue);
            }
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            try {
                loadAllTeachers(searchText);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
        choiceBox.setOnAction(this::setID);

    }

    public void setID(Event event) {
        choice=String.valueOf(choiceBox.getValue());
    }
    public int setIdData() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql1="SELECT p_id FROM programme WHERE programme_name=?";
        PreparedStatement preparedStatement1= connection.prepareStatement(sql1);
        preparedStatement1.setString(1,choice);
        ResultSet resultSet1=preparedStatement1.executeQuery();
        if(resultSet1.next()){
            programmeId=resultSet1.getInt(1);
        }

        return programmeId;
    }
    private void setData(TeacherTm newValue) {
        txtTeacherId.setEditable(false);
        txtEmail.setEditable(false);
        txtTeacherId.setText(String.valueOf(newValue.getTeacherId()));
        txtEmail.setText(newValue.getEmail());
        txtFullName.setText(newValue.getFullName());
    }

    private void loadAllTeachers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<TeacherTm> teacherTms= FXCollections.observableArrayList();
        for(Teacher teacher:searchText.length()>0?searchTeacher(searchText):findAllTeachers()){
            Button btn=new Button("Delete");
            TeacherTm tm=new TeacherTm(
                    teacher.getTeacherId(),teacher.getEmail(),teacher.getFullName(),teacher.getTeachingProgramme(),btn
            );
            teacherTms.add(tm);
            btn.setOnAction((event -> {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?", ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().equals(YES)){
                    deleteTeacher(String.valueOf(teacher.getTeacherId()));
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }));
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
                Connection connection= DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
                String sql="INSERT INTO teacher VALUES (?,?,?,?,?)";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setInt(1, getLastId());
                preparedStatement.setString(2,txtEmail.getText());
                preparedStatement.setString(3,txtFullName.getText());
                preparedStatement.setString(4,choice);
                preparedStatement.setInt(5, setIdData());
                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Teacher Saved!").show();
                    loadAllTeachers(searchText);
                    clearFields();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try again!").show();
                }
            }else {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
                String sql="UPDATE teacher SET full_name=?, teaching_programme=? WHERE t_id=?";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,txtFullName.getText());
                preparedStatement.setString(2,choice);
                preparedStatement.setInt(3,Integer.parseInt(txtTeacherId.getText()));
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
    private void deleteTeacher(String text) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
            String sql="DELETE FROM teacher WHERE t_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,Integer.parseInt(text));
            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION,"Student Deleted!").show();
                loadAllTeachers(searchText);
                clearFields();
            }else {
                new Alert(Alert.AlertType.WARNING,"Not Deleted.Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Teacher> searchTeacher(String text) throws ClassNotFoundException, SQLException {
        //text="%"+text+"%";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql="SELECT * FROM teacher WHERE email LIKE ? || full_name LIKE ?";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setString(1,"%"+text+"%");
        preparedStatement.setString(2,"%"+text+"%");
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Teacher> teachers=new ArrayList<>();
        while (resultSet.next()){
            teachers.add(new Teacher(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
            ));
        }
        return teachers;
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
                return (resultSet.getInt(1)+1);
            }else{
                return 1;
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private ArrayList setChoiceBox() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql="SELECT * FROM programme";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        ArrayList programmeData=new ArrayList<>();
        while (resultSet.next()){
            programmeData.add(
                    resultSet.getString(2)
            );
        }

        return programmeData;
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
        txtTeacherId.setText(String.valueOf(getLastId()));
    }
}
