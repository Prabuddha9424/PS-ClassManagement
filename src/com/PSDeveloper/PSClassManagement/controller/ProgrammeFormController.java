package com.PSDeveloper.PSClassManagement.controller;
import com.PSDeveloper.PSClassManagement.model.Programme;
import com.PSDeveloper.PSClassManagement.view.tm.ProgrammeTm;
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

public class ProgrammeFormController {
    public AnchorPane context;
    public JFXButton btnSaveUpdate;
    public TextField txtProgramme;
    public TextField txtProgrammeName;
    public TextField txtProgrammeDesc;
    public TextField txtSearch;
    public TableView<ProgrammeTm> tbl;
    public TableColumn colProgrammeId;
    public TableColumn colProgrammeName;
    public TableColumn colProgrammeDesc;
    public TableColumn colDelete;
    private String searchText="";
    public void initialize() throws SQLException, ClassNotFoundException {
        colProgrammeId.setCellValueFactory(new PropertyValueFactory<>("programmeId"));
        colProgrammeName.setCellValueFactory(new PropertyValueFactory<>("programmeName"));
        colProgrammeDesc.setCellValueFactory(new PropertyValueFactory<>("programmeDesc"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllProgrammes("");
        txtProgramme.setText(String.valueOf(getLastId()));
        tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                btnSaveUpdate.setText("Update Student");
                setData(newValue);
            }
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            try {
                loadAllProgrammes(searchText);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setData(ProgrammeTm newValue) {
        txtProgramme.setText(String.valueOf(newValue.getProgrammeId()));
        txtProgrammeName.setText(newValue.getProgrammeName());
        txtProgrammeDesc.setText(newValue.getProgrammeDesc());
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm","Dashboard");
    }

    public void btnSaveProgrammeOnAction(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if (btnSaveUpdate.getText().equals("Save Programme")){
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
            String sql="INSERT INTO programme VALUES (?,?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,getLastId());
            preparedStatement.setString(2,txtProgrammeName.getText());
            preparedStatement.setString(3,txtProgrammeDesc.getText());
            preparedStatement.setString(4,LoginFormController.newUser);
            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION,"Programme Saved").show();
                loadAllProgrammes(searchText);
                clearField();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }else {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
            String sql="UPDATE programme SET programme_name=?,programme_description=? WHERE p_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,txtProgrammeName.getText());
            preparedStatement.setString(2,txtProgrammeDesc.getText());
            preparedStatement.setInt(3,Integer.parseInt(txtProgramme.getText()));
            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION,"Programme Updated!").show();
                loadAllProgrammes(searchText);
                clearField();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }

    }

    private void loadAllProgrammes(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProgrammeTm> programmeTms= FXCollections.observableArrayList();
        for(Programme programme:searchText.length()>0?searchProgramme(searchText):findAllProgrammes()){
            Button btn=new Button("Delete");
            ProgrammeTm tm=new ProgrammeTm(
                    programme.getProgrammeId(),programme.getProgrammeName(),programme.getProgrammeDesc(),btn
            );
            programmeTms.add(tm);

            btn.setOnAction((event -> {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?", ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().equals(YES)){
                    deleteProgramme(String.valueOf(programme.getProgrammeId()));
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }));
        }
        tbl.setItems(programmeTms);
    }


    public void btnNewProgrammeOnAction(ActionEvent actionEvent) {
    }
    public static List<Programme> findAllProgrammes() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql="SELECT * FROM programme";
        PreparedStatement preparedStatement= connection.prepareStatement(sql);
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Programme> programmeData=new ArrayList<>();
        while (resultSet.next()){
            programmeData.add(new Programme(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return programmeData;
    }
    public static List<Programme> searchProgramme(String text) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
        String sql="SELECT * FROM programme WHERE programme_name LIKE ?";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setString(1,"%"+text+"%");
        ResultSet resultSet= preparedStatement.executeQuery();
        List<Programme> programmes=new ArrayList<>();
        while (resultSet.next()){
            programmes.add(new Programme(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return programmes;
    }
    private void deleteProgramme(String text) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PC_ClassManage","root","1234");
            String sql="DELETE FROM programme WHERE p_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,Integer.parseInt(text));
            if(preparedStatement.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION,"Programme Deleted!").show();
                loadAllProgrammes(searchText);
                clearField();
            }else {
                new Alert(Alert.AlertType.WARNING,"Not Deleted.Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static int getLastId(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/PC_ClassManage",
                    "root","1234");
            String sql="SELECT p_id FROM programme ORDER BY p_id DESC LIMIT 1";
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
    private void clearField() {
        txtProgramme.clear();
        txtProgrammeName.clear();
        txtProgrammeDesc.clear();
        txtProgramme.setText(String.valueOf(getLastId()));
    }
    private void setUi(String location,String title) throws IOException {
        Stage stage=(Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setTitle(title);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }
}
