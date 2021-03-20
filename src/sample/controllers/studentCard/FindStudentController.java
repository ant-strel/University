package sample.controllers.studentCard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.controllers.SQL.DbHandler;
import sample.models.Student;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindStudentController {
    private ObservableList<String> initSearchFrom = FXCollections.observableArrayList("№ Зачетки", "Фамилия", "Группа");
    private ObservableList<Student> initStudents = FXCollections.observableArrayList();
    private Student initStudent = new Student();

    @FXML
    private Button btnMainMenu,btnSearch,btnOpenStudent;
    @FXML
    private Label statusMsg;
    @FXML
    private TextField fieldSearch;
    @FXML
    private ComboBox comboSearchFrom;
    @FXML
    private TableView<Student> tableStudent;
    @FXML
    private TableColumn<Student,String> tabLastName,tabFirstName,tabSecondName,tabSpeciality;
    @FXML
    private TableColumn<Student,Integer> tabStudentId,tabGroup;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        comboSearchFrom.setItems(initSearchFrom);
    }

    public void BtnSearch(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        initStudents.clear();
        String sql = "DELETE FROM TempStudent";
        DbHandler.Statement().execute(sql);
        tableStudent.setItems(null);
        if(comboSearchFrom.getValue().toString()==null||fieldSearch.getText()==null) {
            statusMsg.setText("Ничего не найдено");
            return;
        }


        switch (comboSearchFrom.getValue().toString()){
            case "№ Зачетки":
                sql = String.format("SELECT StudentId, lastName, firstName,secondName,idGroup,Speciality,Attestation FROM Students where Active = 1 AND StudentId = %s ORDER BY StudentId;",Integer.parseInt(fieldSearch.getText()));
                break;
            case "Фамилия":
                sql = String.format("SELECT StudentId, lastName, firstName,secondName,idGroup,Speciality,Attestation FROM Students where Active = 1 AND LastName LIKE '%s%s%s' ORDER BY StudentId;","%",fieldSearch.getText(),"%");
                break;
            case "Группа":
                sql = String.format("SELECT StudentId, lastName, firstName,secondName,idGroup,Speciality,Attestation FROM Students where Active = 1 AND IdGroup = %s ORDER BY StudentId;",Integer.parseInt(fieldSearch.getText()));
                break;
        }



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

        if(initStudents.size()==0){
            statusMsg.setText("Ничего не найдено");
            return;
        }

        tabStudentId.setCellValueFactory(new PropertyValueFactory<Student,Integer>("studentId"));
        tabLastName.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));
        tabFirstName.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
        tabSecondName.setCellValueFactory(new PropertyValueFactory<Student,String>("secondName"));
        tabGroup.setCellValueFactory(new PropertyValueFactory<Student,Integer>("group"));
        tabSpeciality.setCellValueFactory(new PropertyValueFactory<Student,String>("speciality"));
        tableStudent.setItems(initStudents);
    }

    public void BtnOpenStudent(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String sql = String.format("INSERT INTO TempStudent (StudentId,LastName,FirstName,SecondName,IdGroup,Speciality,Attestation,Active)" +
                "SELECT StudentId,LastName,FirstName,SecondName,IdGroup,Speciality,Attestation,Active " +
                "FROM Students WHERE StudentId=%s AND Lastname='%s' AND FirstName='%s' AND SecondName='%s' AND Active = 1",
                tableStudent.getSelectionModel().getSelectedItem().getStudentId(),
                tableStudent.getSelectionModel().getSelectedItem().getLastName(),
                tableStudent.getSelectionModel().getSelectedItem().getFirstName(),
                tableStudent.getSelectionModel().getSelectedItem().getSecondName());
        DbHandler.Statement().execute(sql);


        Stage stage = (Stage) btnOpenStudent.getScene().getWindow();
        AnchorPane root = FXMLLoader.load(getClass().getResource("/sample/views/StudentCard/studentCard.fxml"));
        stage.setTitle("Карточка студента");
        Scene scene = new Scene(root);
        stage.setScene(scene);



       // new StudentCardController().InitStudent(student);


    }

    public void BtnMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnMainMenu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/mainMenu.fxml"));
        stage.setTitle("Справочники");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void Student(ActionEvent actionEvent) throws IOException {
        Student student = new Student();
        student.Student(tableStudent.getSelectionModel().getSelectedItem().getStudentId(),
                tableStudent.getSelectionModel().getSelectedItem().getFirstName(),
                tableStudent.getSelectionModel().getSelectedItem().getSecondName(),
                tableStudent.getSelectionModel().getSelectedItem().getLastName(),
                tableStudent.getSelectionModel().getSelectedItem().getGroup(),
                tableStudent.getSelectionModel().getSelectedItem().getSpeciality(),0,"");

    }
}
