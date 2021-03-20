package sample.controllers.dictionaries;

import sample.controllers.SQL.DbHandler;
import sample.models.Speciality;
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
import java.util.ArrayList;


public class SpecialityListController {

    private ObservableList<Speciality> initSpeciality = FXCollections.observableArrayList();
    private ArrayList<String> bufferSpeciality = new ArrayList<>();

    @FXML
    private Button add,edit,del,save,menu;
    @FXML
    private Label statusMsg,labelId;
    @FXML
    private TextField specialityId,specialityName;
    @FXML
    private TableView<Speciality> tableSpeciality;
    @FXML
    private TableColumn<Speciality,String> tabSpecialityName,tabSpecialityId;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        initSpeciality.clear();
        bufferSpeciality.clear();
        String sql = "SELECT SpecialityId, Name FROM Speciality where Active = 1 ORDER BY SpecialityId;";
        ResultSet rs = DbHandler.Select(sql);
        while(rs.next()){
            int specialityId = rs.getInt("SpecialityId");
            String name = rs.getString("Name");
            Speciality speciality = new Speciality();
            speciality.Speciality(specialityId,name);
            initSpeciality.add(speciality);
        }
        System.out.println("");
        DbHandler.CloseDB();

        tabSpecialityId.setCellValueFactory(new PropertyValueFactory<Speciality,String>("specialityId"));
        tabSpecialityName.setCellValueFactory(new PropertyValueFactory<Speciality,String>("specialityName"));
        tableSpeciality.setItems(initSpeciality);


        System.out.print(" ");
    }

    public void AddSpeciality(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(specialityName.getText()!=null){
            String sql = "Select MAX(SpecialityId) as result from Speciality";
            int newId = 0;
            ResultSet rs = DbHandler.Select(sql);
            int maxid = rs.getInt("result");
            newId = maxid + 1;
            DbHandler.CloseDB();

            sql= String.format("INSERT INTO Speciality (SpecialityId,name,Active) VALUES (%s,'%s','%s')",
                    newId,
                    specialityName.getText(),
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

    public void EditSpeciality(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        labelId.setVisible(true);
        specialityId.setVisible(true);
        statusMsg.setText(" ");
        save.setVisible(true);

        specialityId.setText(String.valueOf(tableSpeciality.getSelectionModel().getSelectedItem().getSpecialityId()));
        specialityName.setText(tableSpeciality.getSelectionModel().getSelectedItem().getSpecialityName());
        specialityId.setEditable(false);

        bufferSpeciality.add(specialityId.getText());
        bufferSpeciality.add(specialityName.getText());

    }

    public void DelSpeciality(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int stId = tableSpeciality.getSelectionModel().getSelectedItem().getSpecialityId();
        String sql = String.format("UPDATE Speciality SET Active = 0 Where SpecialityId = %s",stId);
        DbHandler.Statement().execute(sql);
        initialize();
    }

    public void SaveChanges(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        statusMsg.setText(" ");

        ArrayList<String> newSpeciality = new ArrayList<>();
        newSpeciality.add(specialityId.getText());
        newSpeciality.add(specialityName.getText());

        boolean flag = true;
        for (int i = 0;i<newSpeciality.size();i++) {
            if(newSpeciality.get(i).equals(bufferSpeciality.get(i)) == false)
            {flag = false; break;}
        }


        if(flag){
            save.setVisible(false);
            labelId.setVisible(false);
            specialityId.setVisible(false);
            initialize();
            ClearText();
        }
        else
        {
            int specId = Integer.parseInt(specialityId.getText());
            String sql = String.format("UPDATE Speciality SET Active = 0 Where SpecialityId = %s",specId);
            DbHandler.Statement().execute(sql);

            sql= String.format("INSERT INTO Speciality (SpecialityId,name,Active) VALUES (%s,'%s','%s')",
                    Integer.parseInt(specialityId.getText()),
                    specialityName.getText(),
                    1);
            DbHandler.Statement().execute(sql);
            save.setVisible(false);
            labelId.setVisible(false);
            specialityId.setVisible(false);
            initialize();
            ClearText();
        }
    }

    public void ClearText(){
        specialityId.clear();
        specialityName.clear();
    }

    public void Menu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/dictionariesMenu.fxml"));
        stage.setTitle("Сводная таблица");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
