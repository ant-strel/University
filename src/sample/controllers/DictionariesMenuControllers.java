package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;



public class DictionariesMenuControllers {

    @FXML
    Button students,teachers,subjects,specialities,groups,attestations,mainMenu;

    public void viewStudentList(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) students.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/Dictionaries/studentList.fxml"));
        stage.setTitle("Справочник студентов");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void viewTeacherList(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) teachers.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/Dictionaries/teacherList.fxml"));
        stage.setTitle("Справочник преподавателей");
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }

    public void viewSubjectList(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) subjects.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/Dictionaries/subjectList.fxml"));
        stage.setTitle("Справочник дисциплин");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }


    public void viewSpecialityList(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) specialities.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/Dictionaries/specialityList.fxml"));
        stage.setTitle("Справочник Специальностей");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void viewGroupList(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) groups.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/Dictionaries/groupList.fxml"));
        stage.setTitle("Справочник групп");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void viewAttestationList(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) attestations.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/Dictionaries/attestationList.fxml"));
        stage.setTitle("Справочник аттестаций");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void viewMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/mainMenu.fxml"));
        stage.setTitle("Меню");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
