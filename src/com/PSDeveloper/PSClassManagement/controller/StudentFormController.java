package com.PSDeveloper.PSClassManagement.controller;
import com.PSDeveloper.PSClassManagement.db.DbConnection;
import com.PSDeveloper.PSClassManagement.model.Student;
import com.PSDeveloper.PSClassManagement.view.tm.StudentTm;
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
import java.util.Optional;

import static javafx.scene.control.ButtonType.YES;

public class StudentFormController {
    public AnchorPane context;
    public JFXButton btnSaveUpdate;
    public TextField txtStudentId;
    public TextField txtEmail;
    public TextField txtFullName;
    public TextField txtSearch;
    public TableView<StudentTm> tbl;
    public TableColumn colStudentId;
    public TableColumn colStudentEmail;
    public TableColumn colStudentFullName;
    public TableColumn colGrade;
    public TableColumn colDelete;
    public TextField txtGrade;
    String searchText="";
    public void initialize() throws SQLException, ClassNotFoundException {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStudentFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllStudents("");
        txtStudentId.setText(String.valueOf(getLastId()));
        tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                btnSaveUpdate.setText("Update Student");
                setData(newValue);
            }else {
                txtEmail.setEditable(true);
            }
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            try {
                loadAllStudents(searchText);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadAllStudents(String searchText) throws SQLException, ClassNotFoundException{
        ObservableList<StudentTm> studentTms= FXCollections.observableArrayList();
            for(Student student:searchText.length()>0?searchStudent(searchText):findAllStudents()){
                Button btn=new Button("Delete");
                StudentTm tm=new StudentTm(
                        student.getId(),student.getEmail(),student.getFullName(),student.getGrade(),btn
                );
                studentTms.add(tm);

                btn.setOnAction((event -> {
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?", ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if(buttonType.get().equals(YES)){
                        deleteStudent(String.valueOf(student.getId()));
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                    }
                }));
            }
            tbl.setItems(studentTms);
    }

    private void deleteStudent(String text) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
            String sql="DELETE FROM student WHERE id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,Integer.parseInt(text));
            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION,"Student Deleted!").show();
                loadAllStudents(searchText);
                clearField();
            }else {
                new Alert(Alert.AlertType.WARNING,"Not Deleted.Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm","Dashboard");
    }

    public void btnSaveStudentOnAction(ActionEvent actionEvent) {
        try {
            if(btnSaveUpdate.getText().equals("Save Student")){
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
                String sql="INSERT INTO student VALUES (?,?,?,?)";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setInt(1,getLastId());
                preparedStatement.setString(2,txtEmail.getText());
                preparedStatement.setString(3,txtFullName.getText());
                preparedStatement.setInt(4,Integer.parseInt(txtGrade.getText()));
                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Student Saved").show();
                    loadAllStudents(searchText);
                    clearField();
                    txtStudentId.setText(String.valueOf(getLastId()));
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }else {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
                String sql="UPDATE student SET full_name=?,grade=? WHERE id=?";
                PreparedStatement preparedStatement=connection.prepareStatement(sql);
                preparedStatement.setString(1,txtFullName.getText());
                preparedStatement.setInt(2,Integer.parseInt(txtGrade.getText()));
                preparedStatement.setInt(3,Integer.parseInt(txtStudentId.getText()));
                if(preparedStatement.executeUpdate()>0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Student Updated!").show();
                    loadAllStudents(searchText);
                    clearField();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public static List<Student> findAllStudents() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql="SELECT * FROM student";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Student> studentData=new ArrayList<>();
        while (resultSet.next()){
            studentData.add(new Student(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)
            ));
        }
        return studentData;
    }
    public static List<Student> searchStudent(String text) throws ClassNotFoundException, SQLException {
        String sql="SELECT * FROM student WHERE email LIKE ? || full_name LIKE ?";
        PreparedStatement preparedStatement= DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,"%"+text+"%");
        preparedStatement.setString(2,"%"+text+"%");
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Student> students=new ArrayList<>();
        while (resultSet.next()){
            students.add(new Student(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4)
            ));
        }
        return students;
    }
    public void btnNewStudentOnAction(ActionEvent actionEvent) {
        clearField();
        txtEmail.setEditable(true);
        btnSaveUpdate.setText("Save Student");
    }
    public static int getLastId(){
        try {
            String sql="SELECT id FROM student ORDER BY id DESC LIMIT 1";
            PreparedStatement preparedStatement=DbConnection.getInstance().getConnection().prepareStatement(sql);
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
    private void setData(StudentTm newValue) {
        txtEmail.setEditable(false);
        txtStudentId.setText(String.valueOf(newValue.getStudentId()));
        txtEmail.setText(newValue.getEmail());
        txtFullName.setText(newValue.getFullName());
        txtGrade.setText(String.valueOf(newValue.getGrade()));
    }
    private void clearField() {
        txtStudentId.clear();
        txtEmail.clear();
        txtFullName.clear();
        txtGrade.clear();
        txtStudentId.setText(String.valueOf(getLastId()));
    }
    private void setUi(String location,String title) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }

}
