package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML Button takeAttestation,studentCard,groupCard,dictionaries,exit;

    public void viewTakeAttestation(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) takeAttestation.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/TakeAttestation/takeAttestation.fxml"));
        stage.setTitle("Принять экзамен");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void viewStudentCard(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) studentCard.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/StudentCard/findStudent.fxml"));
        stage.setTitle("Найти студента");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void viewDictionaries(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) dictionaries.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/dictionariesMenu.fxml"));
        stage.setTitle("Справочники");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void viewGroupCard(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) takeAttestation.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/TakeAttestation/takeAttestation.fxml"));
        stage.setTitle("Карточка группы");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }



}
