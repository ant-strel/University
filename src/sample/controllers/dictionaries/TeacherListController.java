package sample.controllers.dictionaries;

import sample.controllers.SQL.DbHandler;
import sample.models.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeacherListController {

    private ObservableList<Teacher> initTeachers = FXCollections.observableArrayList();
    private ObservableList<String> initChair = FXCollections.observableArrayList();
    private ObservableList<String> initPosition = FXCollections.observableArrayList("Лаборант","Старший лаборант",
            "Ассистент", "Преподаватель", "Старший преподаватель", "Доцент", "Профессор", "Завкафедрой", "Декан",
                    "Проректор", "Ректор");

    private Teacher bufTeacher = new Teacher();

    @FXML
    private Button add,edit,del,save,menu;
    @FXML
    private Label statusMsg,labelId;
    @FXML
    private TextField teacherLastName,teacherFirstName,teacherSecondName,teacherId;
    @FXML
    private TableView<Teacher> tableTeacher;
    @FXML
    private TableColumn<Teacher,String> tabLastName,tabFirstName,tabSecondName,tabPosition,tabChair,tabTeacherId;
    @FXML
    private ComboBox teacherChair,teacherPosition;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        initTeachers.clear();
        initChair.clear();
        String sql = "SELECT TeacherId, lastName, firstName,secondName,Chair,Position FROM Teachers where Active = 1 ORDER BY TeacherId;";
        ResultSet rs = DbHandler.Select(sql);
        while(rs.next()){
            String lastname = rs.getString("lastname");
            String firstname = rs.getString("firstname");
            String secondname = rs.getString("secondname");
            int teacherId = rs.getInt("teacherId");
            String chair = rs.getString("chair");
            String position = rs.getString("position");
            Teacher teacher = new Teacher();
            teacher.Teacher(firstname,secondname,lastname,position,chair,teacherId);
            initTeachers.add(teacher);
        }
        DbHandler.CloseDB();

        tabLastName.setCellValueFactory(new PropertyValueFactory<Teacher,String>("lastName"));
        tabFirstName.setCellValueFactory(new PropertyValueFactory<Teacher,String>("firstName"));
        tabSecondName.setCellValueFactory(new PropertyValueFactory<Teacher,String>("secondName"));
        tabPosition.setCellValueFactory(new PropertyValueFactory<Teacher,String>("position"));
        tabChair.setCellValueFactory(new PropertyValueFactory<Teacher,String>("chair"));
        tabTeacherId.setCellValueFactory(new PropertyValueFactory<Teacher,String>("teacherId"));
        tableTeacher.setItems(initTeachers);

        sql = "Select name from speciality";
        rs = DbHandler.Select(sql);
        while (rs.next()){
            String spec = rs.getString("name");
            initChair.add(spec);
        }
        DbHandler.CloseDB();
        teacherChair.setItems(initChair);
        teacherPosition.setItems(initPosition);

        for (Teacher k : initTeachers)//foreach
        {
            System.out.printf("%s %s %s %s %s\n",k.getFirstName(),k.getSecondName(),k.getLastName(),k.getPosition(),k.getChair());
        }

    }

    
    public void AddTeacher(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(teacherLastName.getText() != null&&teacherFirstName.getText()!=null&&teacherSecondName.getText()!=null&&teacherPosition.getValue()!=null&&teacherChair.getValue()!=null)
        {
            String sql = "SELECT MAX(TeacherId) as result FROM Teachers";
            int newId = 0;
            ResultSet rs = DbHandler.Select(sql);
            int maxid = rs.getInt("result");
            newId = maxid + 1;
            DbHandler.CloseDB();

            sql= String.format("INSERT INTO Teachers (TeacherId,LastName,FirstName,SecondName,Chair,Position,Active) VALUES (%s,'%s','%s','%s',%s,'%s',%s)",
                    newId,
                    teacherLastName.getText(),
                    teacherFirstName.getText(),
                    teacherSecondName.getText(),
                    teacherChair.getValue().toString(),
                    teacherPosition.getValue().toString(),
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

    public void EditTeacher(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        labelId.setVisible(true);
        teacherId.setVisible(true);
        statusMsg.setText(" ");
        save.setVisible(true);

        teacherId.setText(String.valueOf(tableTeacher.getSelectionModel().getSelectedItem().getTeacherId()));
        teacherLastName.setText(tableTeacher.getSelectionModel().getSelectedItem().getLastName());
        teacherFirstName.setText(tableTeacher.getSelectionModel().getSelectedItem().getFirstName());
        teacherSecondName.setText(tableTeacher.getSelectionModel().getSelectedItem().getSecondName());
        teacherPosition.setPromptText(tableTeacher.getSelectionModel().getSelectedItem().getPosition());
        teacherChair.setPromptText(tableTeacher.getSelectionModel().getSelectedItem().getChair());
        teacherId.setEditable(false);

        bufTeacher.Teacher(teacherFirstName.getText(),
                teacherSecondName.getText(),
                teacherLastName.getText(),
                teacherPosition.getValue().toString(),
                teacherChair.getValue().toString(),
                Integer.parseInt(teacherId.getText()));

        initialize();
    }

    public void DelTeacher(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int teachId = tableTeacher.getSelectionModel().getSelectedItem().getTeacherId();
        String sql = String.format("UPDATE Teachers SET Active = 0 Where StudentId = %s",teachId);
        DbHandler.Statement().execute(sql);
        initialize();
    }

    public void SaveChanges(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        statusMsg.setText(" ");
        Teacher newTeacher = new Teacher();
        newTeacher.Teacher(teacherFirstName.getText(),
                teacherSecondName.getText(),
                teacherLastName.getText(),
                teacherPosition.getValue().toString(),
                teacherChair.getValue().toString(),
                Integer.parseInt(teacherId.getText()));

        if(newTeacher.equals(bufTeacher)){
            save.setVisible(false);
            labelId.setVisible(false);
            teacherId.setVisible(false);
            initialize();
            ClearText();
        }
        else
        {
            int teachId = tableTeacher.getSelectionModel().getSelectedItem().getTeacherId();
            String sql = String.format("UPDATE Teachers SET Active = 0 Where StudentId = %s",teachId);
            DbHandler.Statement().execute(sql);

            sql= String.format("INSERT INTO Teachers (TeacherId,LastName,FirstName,SecondName,Chair,Position,Active) VALUES (%s,'%s','%s','%s',%s,'%s',%s)",
                    teachId,
                    teacherLastName.getText(),
                    teacherFirstName.getText(),
                    teacherSecondName.getText(),
                    teacherChair.getValue().toString(),
                    teacherPosition.getValue().toString(),
                    1);
            DbHandler.Statement().execute(sql);
            save.setVisible(false);
            labelId.setVisible(false);
            teacherId.setVisible(false);
            initialize();
            ClearText();

        }
    }
    public void ClearText(){
        teacherId.clear();
        teacherLastName.clear();
        teacherFirstName.clear();
        teacherSecondName.clear();
        teacherPosition.setValue("");
        teacherChair.setValue("");
    }


    public void Menu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/dictionariesMenu.fxml"));
        stage.setTitle("Сводная таблица");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}


