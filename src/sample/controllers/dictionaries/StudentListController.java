package sample.controllers.dictionaries;


import org.json.JSONArray;
import org.json.JSONObject;
import org.sqlite.JDBC;
import sample.controllers.SQL.DbHandler;
import sample.models.Attestation;
import sample.models.Student;
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

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;


public class StudentListController {

    private ObservableList<Student> initStudents = FXCollections.observableArrayList();
    private ObservableList<String> initSpeciality = FXCollections.observableArrayList();
    private  ObservableList<Integer> initGroups = FXCollections.observableArrayList(1,2,3,4,5);
    private Student bufStudent = new Student();
    private ArrayList<String> bufferStudent = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @FXML
    private Button add,edit,del,save,menu;
    @FXML
    private Label statusMsg,labelId;
    @FXML
    private TextField studentId,studentLastName,studentFirstName,studentSecondName;
    @FXML
    private ComboBox studentSpeciality,studentGroup;
    @FXML
    private TableView<Student> tableStudent;
    @FXML
    private TableColumn<Student,String> tabLastName,tabFirstName,tabSecondName,tabSpeciality;
    @FXML
    private TableColumn<Student,Integer> tabStudentId,tabGroup;

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
            initStudents.clear();
            initSpeciality.clear();
            bufferStudent.clear();
            String sql = "SELECT StudentId, lastName, firstName,secondName,idGroup,Speciality,Attestation FROM Students where Active = 1 ORDER BY StudentId;";

            ResultSet rs = DbHandler.Select(sql);
            while (rs.next()){
                int studentId = rs.getInt("StudentId");
                String lastname = rs.getString("lastname");
                String firstname = rs.getString("firstname");
                String secondname = rs.getString("secondname");
                int groupid = rs.getInt("IdGroup");
                String speciality = rs.getString("Speciality");
                Student student = new Student();
                String attestations = rs.getString("Attestation");
                student.Student(studentId,firstname,secondname,lastname,groupid,speciality,0,attestations);
                initStudents.add(student);
            }
            DbHandler.CloseDB();



        tabStudentId.setCellValueFactory(new PropertyValueFactory<Student,Integer>("studentId"));
        tabLastName.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));
        tabFirstName.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
        tabSecondName.setCellValueFactory(new PropertyValueFactory<Student,String>("secondName"));
        tabGroup.setCellValueFactory(new PropertyValueFactory<Student,Integer>("group"));
        tabSpeciality.setCellValueFactory(new PropertyValueFactory<Student,String>("speciality"));
        tableStudent.setItems(initStudents);

        sql = "Select name from speciality";
        rs = DbHandler.Select(sql);
        while (rs.next()){
            String spec = rs.getString("name");
            initSpeciality.add(spec);
        }
        DbHandler.CloseDB();
        studentSpeciality.setItems(initSpeciality);
        studentGroup.setItems(initGroups);


        for (Student k : initStudents)//foreach
        {
            System.out.printf("%s %s %s %s %s %s\n",k.getStudentId(),k.getFirstName(),k.getSecondName(),k.getLastName(),k.getGroup(),k.getSpeciality());
        }


    }

    public void AddStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
      if(studentLastName.getText() != null&&studentFirstName.getText()!=null&&studentSecondName.getText()!=null&&studentGroup.getValue()!=null&&studentSpeciality.getValue()!=null)
        {
            String sql = "SELECT MAX(StudentId) as result FROM Students";
            int newId = 0;
            ResultSet rs = DbHandler.Select(sql);
            int maxid = rs.getInt("result");
            newId = maxid + 1;
            DbHandler.CloseDB();

            sql= String.format("INSERT INTO Students (StudentId,LastName,FirstName,SecondName,IdGroup,Speciality,Active) VALUES (%s,'%s','%s','%s',%s,'%s',%s)",
                        newId,
                        studentLastName.getText(),
                        studentFirstName.getText(),
                        studentSecondName.getText(),
                        Integer.parseInt(studentGroup.getValue().toString()),
                        studentSpeciality.getValue().toString(),
                        1);
            DbHandler.Statement().execute(sql);

            AddAttestation(studentSpeciality.getValue().toString(),newId);

            initialize();
            ClearText();
        }
        else
        {
            statusMsg.setText("Заполните все поля!");
        }

    }

    public void DelStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int stId = tableStudent.getSelectionModel().getSelectedItem().getStudentId();
        String sql = String.format("UPDATE Students SET Active = 0 Where StudentId = %s",stId);
        DbHandler.Statement().execute(sql);
        initialize();
    }

    public void EditStudent(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        labelId.setVisible(true);
        studentId.setVisible(true);
        statusMsg.setText(" ");
        save.setVisible(true);

        //значения полей
       // String oldSpec = tableStudent.getSelectionModel().getSelectedItem().getSpeciality();
        studentId.setText(String.valueOf(tableStudent.getSelectionModel().getSelectedItem().getStudentId()));
        studentLastName.setText(tableStudent.getSelectionModel().getSelectedItem().getLastName());
        studentFirstName.setText(tableStudent.getSelectionModel().getSelectedItem().getFirstName());
        studentSecondName.setText(tableStudent.getSelectionModel().getSelectedItem().getSecondName());
        studentGroup.setValue(String.valueOf(tableStudent.getSelectionModel().getSelectedItem().getGroup()));
        studentSpeciality.setValue(tableStudent.getSelectionModel().getSelectedItem().getSpeciality());

        studentId.setEditable(false);
        //буферный объект
        bufferStudent.add(studentId.getText());
        bufferStudent.add(studentFirstName.getText());
        bufferStudent.add(studentSecondName.getText());
        bufferStudent.add(studentLastName.getText());
        bufferStudent.add(studentGroup.getValue().toString());
        bufferStudent.add(studentSpeciality.getPromptText());

    }

    public void SaveChanges(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        statusMsg.setText(" ");
        ArrayList<String> newStudent = new ArrayList<>();
        newStudent.add(studentId.getText());
        newStudent.add(studentFirstName.getText());
        newStudent.add(studentSecondName.getText());
        newStudent.add(studentLastName.getText());
        newStudent.add(studentGroup.getValue().toString());
        newStudent.add(studentSpeciality.getPromptText());

        boolean flag = true;
        for (int i = 0;i<newStudent.size();i++) {
            if(newStudent.get(i).equals(bufferStudent.get(i)) == false)
            {flag = false; break;}
        }



        if(flag){
            save.setVisible(false);
            labelId.setVisible(false);
            studentId.setVisible(false);
            initialize();
            ClearText();
        }
        else
        {
            int stId = Integer.parseInt(newStudent.get(0));
            String sql = String.format("Select Attestation From Students Where Active = 1 And StudentId = %s",stId);
            ResultSet rs = DbHandler.Select(sql);
            String attestationJson = rs.getString("Attestation");
            DbHandler.CloseDB();

            sql = String.format("UPDATE Students SET Active = 0 Where StudentId = %s",stId);
            DbHandler.Statement().execute(sql);

            sql= String.format("INSERT INTO Students (StudentId,LastName,FirstName,SecondName,IdGroup,Speciality,Attestation,Active) VALUES (%s,'%s','%s','%s',%s,'%s','%s',%s)",
                    stId,
                    studentLastName.getText(),
                    studentFirstName.getText(),
                    studentSecondName.getText(),
                    Integer.parseInt(studentGroup.getValue().toString()),
                    studentSpeciality.getValue().toString(),
                    attestationJson,
                    1);
            DbHandler.Statement().execute(sql);

            save.setVisible(false);
            labelId.setVisible(false);
            studentId.setVisible(false);
            initialize();
            ClearText();

        }

    }

    public void ClearText(){
        studentLastName.clear();
        studentFirstName.clear();
        studentSecondName.clear();
        studentGroup.setValue("");
        studentId.clear();
        studentSpeciality.setValue("");
    }

    public void Menu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/dictionariesMenu.fxml"));
        stage.setTitle("Справочники");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void AddAttestation(String specialityName, int studentId ) throws SQLException, ClassNotFoundException {
        String sql = String.format("SELECT SpecialityId FROM Speciality where Active = 1 AND Name = '%s';",specialityName);
        ResultSet rs = DbHandler.Select(sql);
        int specialId = rs.getInt("SpecialityId");
        DbHandler.CloseDB();


        sql = String.format("SELECT * FROM Attestations where Active = 1 AND SpecialityId = %s ORDER BY AttestationId;",specialId);
        ArrayList<Attestation> attestations = new ArrayList<>();
        rs = DbHandler.Select(sql);

        while (rs.next()){
            int attestationId = rs.getInt("AttestationId");
            String subject = rs.getString("Subject");
            String type = rs.getString("Type");
            LocalDate date = LocalDate.parse(rs.getString("Date"),formatter);
            String teacher = rs.getString("Teacher");
            int specId = rs.getInt("SpecialityId");
            Attestation attestation = new Attestation();
            attestation.Attestation(attestationId,subject,type,"0",date,teacher,specId);
            attestations.add(attestation);
        }
        DbHandler.CloseDB();

        JSONArray jsonArray = new JSONArray(attestations);
        sql = String.format("UPDATE Students SET Attestation = '%s' WHERE StudentId = %s",jsonArray,studentId);
        DbHandler.Statement().execute(sql);

    }
}
