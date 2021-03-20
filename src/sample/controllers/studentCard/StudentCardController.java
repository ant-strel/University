package sample.controllers.studentCard;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.controllers.SQL.DbHandler;
import sample.models.Attestation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StudentCardController {

    @FXML
    private AnchorPane parent;
    @FXML
    private Button btnMainMenu,btnSelectStudent;
    @FXML
    private Label labLastName,labFirstName,labSecondName,labGroup,labSpeciality,labStudentId;
    @FXML
    private TableView<Attestation> tableAttestation;
    @FXML
    private TableColumn<Attestation, String> tabSubject;
    @FXML
    private TableColumn<Attestation, Date> tabDate;
    @FXML
    private TableColumn<Attestation, String> tabTeacher;
    @FXML
    private TableColumn<Attestation, String> tabType;
    @FXML
    private TableColumn<Attestation, String> tabRate;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
       String attJson = "";
        String sql = "SELECT * from TempStudent";
       ResultSet rs = DbHandler.Select(sql);
       labLastName.setText(rs.getString("LastName"));
       labFirstName.setText(rs.getString("FirstName"));
       labSecondName.setText(rs.getString("SecondName"));
       labGroup.setText(rs.getString("IdGroup").toString());
       labSpeciality.setText(rs.getString("Speciality"));
       labStudentId.setText(rs.getString("StudentId").toString());
       attJson = rs.getString("Attestation");
       DbHandler.CloseDB();

        JSONArray jsonArray = new JSONArray(attJson);

        ObservableList<Attestation> initAttestation = FXCollections.observableArrayList();

        for (int i =0;i<jsonArray.length();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Attestation attestation = new Attestation();
            attestation.Attestation(object.getInt("attestationId"),
                                    object.getString("subject"),
                                    object.getString("type"),
                                    object.getString("mark"),
                    LocalDate.parse(object.getString("pointDate")),
                                    object.getString("teacher"),
                                    object.getInt("specialityId"));
            initAttestation.add(attestation);
        }

        tabSubject.setCellValueFactory(new PropertyValueFactory<Attestation,String>("subject"));
        tabRate.setCellValueFactory(new PropertyValueFactory<Attestation,String>("mark"));
        tabType.setCellValueFactory(new PropertyValueFactory<Attestation,String>("type"));
        tabDate.setCellValueFactory(new PropertyValueFactory<Attestation, Date>("pointDate"));
        tabTeacher.setCellValueFactory(new PropertyValueFactory<Attestation,String>("teacher"));
        tableAttestation.setItems(initAttestation);




    }
    public void BtnMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnMainMenu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/mainMenu.fxml"));
        stage.setTitle("Меню");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void BtnSelectStudent(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnSelectStudent.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/StudentCard/findStudent.fxml"));
        stage.setTitle("Найти студента");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
