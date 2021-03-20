package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.controllers.SQL.DbHandler;
import sample.models.Attestation;
import sample.models.Student;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TakeAttestationController {
    private ObservableList<Integer> initGroups = FXCollections.observableArrayList(1,2,3,4,5);
    private ObservableList<Student> initStudents = FXCollections.observableArrayList();
    private ObservableList<Attestation> initAttestation = FXCollections.observableArrayList();
    private ObservableList<String> attList = FXCollections.observableArrayList();
    private ObservableList<Integer> markExam = FXCollections.observableArrayList(2,3,4,5);
    private ObservableList<String> markTest = FXCollections.observableArrayList("Зачет", "Незачет");
    private int attestId = 0;
    private String type = "";
    private ObservableList<Student> tempStudent = FXCollections.observableArrayList();


    @FXML
    private Button btnSelectAtt,btnSelectGroup,btnPutRate,btnMainMenu;
    @FXML
    private Label statusMsg,fieldAttestation,fieldGroup;
    @FXML
    private ComboBox selectAttestation,selectGroup,selectRate;
    @FXML
    private TableView<Student> tableStudent;
    @FXML
    private TableColumn<Student,String> tabF,tabI,tabO,tabId,tabVal;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        selectGroup.setItems(initGroups);
    }


    public void BtnSelectGroup(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        selectAttestation.setVisible(true);
        fieldAttestation.setVisible(true);
        btnSelectAtt.setVisible(true);
        initStudents.clear();
        initAttestation.clear();
        attList.clear();

        fieldGroup.setText(selectGroup.getValue().toString());
        String sql = String.format("SELECT * FROM Students WHERE IdGroup = %s",selectGroup.getValue().toString());
        ResultSet rs = DbHandler.Select(sql);
        String attestations = "";
            while (rs.next()){
                int studentId = rs.getInt("StudentId");
                String lastname = rs.getString("lastname");
                String firstname = rs.getString("firstname");
                String secondname = rs.getString("secondname");
                int groupid = rs.getInt("IdGroup");
                String speciality = rs.getString("Speciality");
                attestations = rs.getString("Attestation");
                Student student = new Student();
                student.Student(studentId,firstname,secondname,lastname,groupid,speciality,0,attestations);
                initStudents.add(student);
            }
            DbHandler.CloseDB();

        JSONArray jsonArray = new JSONArray(attestations);

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

        for (Attestation k: initAttestation) {
            attList.add(String.valueOf(k.getAttestationId()) + "-" + k.getSubject());
        }
        selectAttestation.setItems(attList);

    }

    public void BtnSelectAtt(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String[] attArr = selectAttestation.getValue().toString().split("-");
        attestId = Integer.parseInt(attArr[0]);
        fieldAttestation.setText(attArr[1]);

        String sql = String.format("SELECT * FROM Students WHERE IdGroup = %s",selectGroup.getValue().toString());
        ResultSet rs = DbHandler.Select(sql);
            while (rs.next()){
                int studentId = rs.getInt("StudentId");
                String lastname = rs.getString("lastname");
                String firstname = rs.getString("firstname");
                String secondname = rs.getString("secondname");
                int groupid = rs.getInt("IdGroup");
                String speciality = rs.getString("Speciality");
                String attestations = rs.getString("Attestation");
                Student student = new Student();
                student.Student(studentId,firstname,secondname,lastname,groupid,speciality,0,attestations);
                initStudents.add(student);
            }
            DbHandler.CloseDB();

        sql = String.format("SELECT AttestationId, Subject,Type,Date,Teacher,SpecialityId FROM Attestations WHERE AttestationId = '%s' AND Active = 1",attestId);
        System.out.println("");
        rs = DbHandler.Select(sql);
        while(rs.next()){
        type = rs.getString("Type");
        }
        DbHandler.CloseDB();

        switch (type){
            case "Экзамен":
                selectRate.setItems(markExam);
                break;
            case "Зачет":
                selectRate.setItems(markTest);
                break;
        }

        sql = String.format("SELECT * FROM Students WHERE IdGroup = %s",selectGroup.getValue().toString());
        rs = DbHandler.Select(sql);
        while (rs.next()){
            int studentId = rs.getInt("StudentId");
            String lastname = rs.getString("lastname");
            String firstname = rs.getString("firstname");
            String secondname = rs.getString("secondname");
            int groupid = rs.getInt("IdGroup");
            String speciality = rs.getString("Speciality");
            String attestations = rs.getString("Attestation");
            Student student = new Student();
            student.Student(studentId,firstname,secondname,lastname,groupid,speciality,0,attestations);
            tempStudent.add(student);
        }
        DbHandler.CloseDB();

        tabF.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));
        tabI.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
        tabO.setCellValueFactory(new PropertyValueFactory<Student,String>("secondName"));
        tabId.setCellValueFactory(new PropertyValueFactory<Student,String>("studentId"));
        tabVal.setCellValueFactory(new PropertyValueFactory<Student,String>("mark"));
        tableStudent.setItems(tempStudent);



    }

    public void BtnPutRate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int mark = 0;
        switch (selectRate.getValue().toString()) {
            case "Зачет":
                mark = 1;
                break;
            case "Незачет":
                mark = 0;
                break;
            case "2":
                mark = 2;
                break;
            case "3":
                mark = 3;
                break;
            case "4":
                mark = 4;
                break;
            case "5":
                mark = 5;
                break;


        }
        for (Student k: tempStudent) {
            if(k.getStudentId() == tableStudent.getSelectionModel().getSelectedItem().getStudentId())
            {
                k.setMark(mark);
            }
        }
        String sql = String.format("Select Attestation from Students where StudentId=%s and Active=1",tableStudent.getSelectionModel().getSelectedItem().getStudentId());
        ResultSet rs = DbHandler.Select(sql);
        String jsonAttestation = rs.getString("Attestation");
        DbHandler.CloseDB();

        JSONArray jsonArray = new JSONArray(jsonAttestation);

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
        for (Attestation l:initAttestation
             ) {
            if(l.getAttestationId()==attestId)
                l.setMark(String.valueOf(mark));
        }
        JSONArray jsonArray1 = new JSONArray(initAttestation);
        sql = String.format("UPDATE Students SET Attestation = '%s' WHERE StudentId = %s",jsonArray1,tableStudent.getSelectionModel().getSelectedItem().getStudentId());
        DbHandler.Statement().execute(sql);

        sql = String.format("SELECT * FROM Students WHERE IdGroup = %s",selectGroup.getValue().toString());
        rs = DbHandler.Select(sql);
        while (rs.next()){
            int studentId = rs.getInt("StudentId");
            String lastname = rs.getString("lastname");
            String firstname = rs.getString("firstname");
            String secondname = rs.getString("secondname");
            int groupid = rs.getInt("IdGroup");
            String speciality = rs.getString("Speciality");
            String attestations = rs.getString("Attestation");
            Student student = new Student();
            student.Student(studentId,firstname,secondname,lastname,groupid,speciality,0,attestations);
            tempStudent.add(student);
        }
        DbHandler.CloseDB();

        tabF.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));
        tabI.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
        tabO.setCellValueFactory(new PropertyValueFactory<Student,String>("secondName"));
        tabId.setCellValueFactory(new PropertyValueFactory<Student,String>("studentId"));
        tabVal.setCellValueFactory(new PropertyValueFactory<Student,String>("mark"));
        tableStudent.setItems(tempStudent);

    }

    public void BtnMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnMainMenu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/mainMenu.fxml"));
        stage.setTitle("Меню");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
