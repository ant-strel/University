package sample.controllers.dictionaries;

import javafx.scene.control.cell.PropertyValueFactory;
import sample.controllers.SQL.DbHandler;
import sample.models.Subject;
import sample.models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectListController {

    private ObservableList<Subject> initSubjects = FXCollections.observableArrayList();
    private ObservableList<String> initSpeciality = FXCollections.observableArrayList();
    private  Subject bufSubject = new Subject();

    @FXML
    private Button add,edit,del,save,menu;
    @FXML
    private Label statusMsg,labSubjectId;
    @FXML
    private TextField subjectId,subjectName;
    @FXML
    private TableView<Subject> tableSubject;
    @FXML
    private TableColumn<Subject,String> tabSubjectName,tabSpeciality;
    @FXML
    private TableColumn<Subject,Integer> tabSubjectId;
    @FXML
    private ComboBox specialityName;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        initSubjects.clear();
        initSpeciality.clear();

        String sql = "SELECT Speciality,Name,SubjectId FROM Subjects where Active = 1 ORDER BY SubjectId;";
        ResultSet rs = DbHandler.Select(sql);

        while(rs.next()){
            int subjectId = rs.getInt("SubjectId");
            String name = rs.getString("name");
            String speciality = rs.getString("speciality");

            Subject subject = new Subject();
            subject.Subject(name,speciality,subjectId);
            initSubjects.add(subject);

        }
        DbHandler.CloseDB();

        tabSubjectId.setCellValueFactory(new PropertyValueFactory<Subject,Integer>("subjectId"));
        tabSubjectName.setCellValueFactory(new PropertyValueFactory<Subject,String>("subjectName"));
        tabSpeciality.setCellValueFactory(new PropertyValueFactory<Subject,String>("speciality"));
        tableSubject.setItems(initSubjects);

        sql = "Select name from speciality";
        rs = DbHandler.Select(sql);
        while (rs.next()){
            String spec = rs.getString("name");
            initSpeciality.add(spec);
        }
        DbHandler.CloseDB();
        specialityName.setItems(initSpeciality);
    }


    public void AddSubject(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(subjectName.getText()!=null&&specialityName.getValue()!=null){
            String sql = "SELECT MAX(SubjectId) as result FROM Subjects";
            int newId = 0;
            ResultSet rs = DbHandler.Select(sql);
            int maxid = rs.getInt("result");
            newId = maxid + 1;
            DbHandler.CloseDB();

            sql = String.format("Select SpecialityId from Speciality where name = %s and active = 1",specialityName.getValue().toString());
            rs = DbHandler.Select(sql);
            int curSpecId = rs.getInt("SpecialityId");
            DbHandler.CloseDB();


            sql= String.format("INSERT INTO Subjects (SubjectId,Name,Speciality,SpecialityId,Active) VALUES (%s,'%s','%s',%s,%s)",
                    newId,
                    subjectName.getText(),
                    specialityName.getValue().toString(),
                    curSpecId,
                    1);
            DbHandler.Statement().execute(sql);

            initialize();
            ClearText();
        }
        else
        {
            statusMsg.setText("Заполните все поля!");
        }
    }

    public void EditSubject(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        labSubjectId.setVisible(true);
        subjectId.setVisible(true);
        statusMsg.setText(" ");
        save.setVisible(true);

        subjectId.setText(String.valueOf(tableSubject.getSelectionModel().getSelectedItem().getSubjectId()));
        subjectName.setText(tableSubject.getSelectionModel().getSelectedItem().getSubjectName());
        specialityName.setPromptText(tableSubject.getSelectionModel().getSelectedItem().getSpeciality());
        subjectId.setEditable(false);

        bufSubject.Subject(tableSubject.getSelectionModel().getSelectedItem().getSubjectName(),
                tableSubject.getSelectionModel().getSelectedItem().getSpeciality(),
                Integer.parseInt(String.valueOf(tableSubject.getSelectionModel().getSelectedItem().getSubjectId())));

        initialize();

    }

    public void DelSubject(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int subId = tableSubject.getSelectionModel().getSelectedItem().getSubjectId();
        String sql = String.format("UPDATE Subjects SET Active = 0 Where SubjectId = %s",subId);
        DbHandler.Statement().execute(sql);
        initialize();
    }

    public void SaveChanges(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        statusMsg.setText(" ");
        Subject newSubject = new Subject();
        newSubject.Subject(subjectName.getText(),
                specialityName.getValue().toString(),
                Integer.parseInt(subjectId.getText()));
        if(newSubject.equals(bufSubject)){
            save.setVisible(false);
            labSubjectId.setVisible(false);
            subjectId.setVisible(false);
            initialize();
            ClearText();
        }
        else {
            int subId = tableSubject.getSelectionModel().getSelectedItem().getSubjectId();
            String sql = String.format("UPDATE Subjects SET Active = 0 Where SubjectId = %s", subId);
            DbHandler.Statement().execute(sql);

            sql = String.format("Select SpecialityId from Speciality where name = %s and active = 1", specialityName.getValue().toString());
            ResultSet rs = DbHandler.Select(sql);
            int curSpecId = rs.getInt("SpecialityId");
            DbHandler.CloseDB();

            sql = String.format("INSERT INTO Subjects (SubjectId,Name,Speciality,SpecialityId,Active) VALUES (%s,'%s','%s',%s,%s)",
                    subId,
                    subjectName.getText(),
                    specialityName.getValue().toString(),
                    curSpecId,
                    1);
            DbHandler.Statement().execute(sql);
            save.setVisible(false);
            labSubjectId.setVisible(false);
            subjectId.setVisible(false);
            initialize();
            ClearText();

        }
    }
    public void Menu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/dictionariesMenu.fxml"));
        stage.setTitle("Главное меню");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

        public void ClearText(){
        subjectId.clear();
        subjectName.clear();
        specialityName.setValue("");
    }
}
