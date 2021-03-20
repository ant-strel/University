package sample.controllers.dictionaries;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import sample.controllers.SQL.DbHandler;
import sample.models.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class AttestationListController {
    private ObservableList<Attestation> initAttestation = FXCollections.observableArrayList();
    private ObservableList<String> initSubjects = FXCollections.observableArrayList();
    private ObservableList<String> initTeacher = FXCollections.observableArrayList();
    private ObservableList<String> initSpeciality = FXCollections.observableArrayList();
    private ObservableList<String> initType = FXCollections.observableArrayList("Экзамен","Зачет");
    private ArrayList<String> bufferAttestation = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private Button add,edit,del,save,menu;
    @FXML
    private Label statusMsg,labelId;
    @FXML
    private TextField attId;
    @FXML
    private ComboBox attSubject,attType,attTeacher,attSpecId;
    @FXML
    private DatePicker attDate;
    @FXML
    private TableView<Attestation> tableAttestation;
    @FXML
    private TableColumn<Attestation,String> tabAttSubject,tabAttType, tabTeacher;
    @FXML
    private TableColumn<Attestation, Date> tabAttDate;
    @FXML
    private TableColumn<Attestation,Integer> tabAttId,tabAttSpecId;
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        initAttestation.clear();
        initSpeciality.clear();
        initTeacher.clear();
        initSubjects.clear();
        bufferAttestation.clear();

        String sql = "SELECT AttestationId, Subject, Type,Date,Teacher,SpecialityId FROM Attestations where Active = 1 ORDER BY AttestationId;";
        ResultSet rs = DbHandler.Select(sql);

        while(rs.next()){
            int attestationId = rs.getInt("AttestationId");
            String subject = rs.getString("Subject");
            String type = rs.getString("Type");
            LocalDate date = LocalDate.parse(rs.getString("Date"),formatter);
            String teacher = rs.getString("Teacher");
            int specId = rs.getInt("SpecialityId");
            Attestation attestation = new Attestation();
            attestation.Attestation(attestationId,subject,type,"0",date,teacher,specId);
            initAttestation.add(attestation);
        }
        DbHandler.CloseDB();

        tabAttId.setCellValueFactory(new PropertyValueFactory<Attestation,Integer>("attestationId"));
        tabAttSubject.setCellValueFactory(new PropertyValueFactory<Attestation,String>("subject"));
        tabAttType.setCellValueFactory(new PropertyValueFactory<Attestation,String>("type"));
        tabAttDate.setCellValueFactory(new PropertyValueFactory<Attestation,Date>("pointDate"));
        tabTeacher.setCellValueFactory(new PropertyValueFactory<Attestation,String>("teacher"));
        tabAttSpecId.setCellValueFactory(new PropertyValueFactory<Attestation,Integer>("specialityId"));
        tableAttestation.setItems(initAttestation);

        sql = "Select name, specialityId from speciality";
        rs = DbHandler.Select(sql);
        while (rs.next()){
            String id = rs.getString("specialityId");
            String name = rs.getString("name");
            String view = name + "-" + id;
            initSpeciality.add(view);
        }
        DbHandler.CloseDB();

        sql = "Select name from subjects";
        rs = DbHandler.Select(sql);
        while(rs.next()){
            String subname = rs.getString("name");
            initSubjects.add(subname);
        }
        DbHandler.CloseDB();

        sql = "Select lastname,firstname,secondname from teachers";
        rs = DbHandler.Select(sql);
        while(rs.next()){
            String lastname = rs.getString("lastname");
            String firstname = rs.getString("firstname");
            String secondName = rs.getString("secondName");
            String teachName = String.format("%s %s %s",lastname,firstname,secondName);
            initTeacher.add(teachName);
        }
        DbHandler.CloseDB();
        attSpecId.setItems(initSpeciality);
        attSubject.setItems(initSubjects);
        attTeacher.setItems(initTeacher);
        attType.setItems(initType);


    }

    public void AddAttestation(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(attSubject.getValue()!=null&&
                attType.getValue()!=null&&
                attTeacher.getValue()!=null&&
                attDate.getValue()!=null&&
                attSpecId.getValue()!=null)
        {
            String sql = "SELECT MAX(AttestationId) as result FROM Attestations";
            int newId = 0;
            ResultSet rs = DbHandler.Select(sql);
            int maxid = rs.getInt("result");
            newId = maxid + 1;
            DbHandler.CloseDB();
            String[] arr = attSpecId.getValue().toString().split("-");
            sql= String.format("INSERT INTO Attestations (AttestationId,Subject,Type,Date,Teacher,SpecialityId,Active) VALUES (%s,'%s','%s','%s','%s',%s,%s)",
                    newId,
                    attSubject.getValue().toString(),
                    attType.getValue().toString(),
                    attDate.getValue(),
                    attTeacher.getValue().toString(),
                    Integer.parseInt(arr[1]),
                    1);
            System.out.println("");
            DbHandler.Statement().execute(sql);

            initialize();
            ClearText();
        }
        else
        {
            statusMsg.setText("Заполните все поля!");
        }
    }


    public void EditAttestation(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        labelId.setVisible(true);
        attId.setVisible(true);
        statusMsg.setText(" ");
        save.setVisible(true);

        int spId = tableAttestation.getSelectionModel().getSelectedItem().getSpecialityId();
        String sql = String.format("Select name from speciality where SpecialityId = %s and active = 1",spId);
        ResultSet rs = DbHandler.Select(sql);
        String name = rs.getString("name");
        DbHandler.CloseDB();
        String specName = name + "-" + String.valueOf(spId);




        attId.setText(String.valueOf(tableAttestation.getSelectionModel().getSelectedItem().getAttestationId()));
        attSubject.setValue(tableAttestation.getSelectionModel().getSelectedItem().getSubject());
        attType.setValue(tableAttestation.getSelectionModel().getSelectedItem().getType());
        attDate.setValue(LocalDate.parse(tableAttestation.getSelectionModel().getSelectedItem().getPointDate().toString()));
        attTeacher.setValue(tableAttestation.getSelectionModel().getSelectedItem().getTeacher());
        attSpecId.setValue(specName);
        attId.setEditable(false);

        bufferAttestation.add(attId.getText());
        bufferAttestation.add(attSubject.getValue().toString());
        bufferAttestation.add(attType.getValue().toString());
        bufferAttestation.add("0");
        bufferAttestation.add(attDate.getValue().toString());
        bufferAttestation.add(attTeacher.getValue().toString());
        bufferAttestation.add(String.valueOf(spId));

    }

    public void DelAttestation(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        int attestId = tableAttestation.getSelectionModel().getSelectedItem().getAttestationId();
        String sql = String.format("UPDATE Attestations SET Active = 0 Where AttestationId = %s",attestId);
        DbHandler.Statement().execute(sql);
        initialize();
    }

    public void Menu(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menu.getScene().getWindow();
        Pane root = FXMLLoader.load(getClass().getResource("/sample/views/dictionariesMenu.fxml"));
        stage.setTitle("Сводная таблица");
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void SaveChanges(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        statusMsg.setText(" ");
        String[] arr = attSpecId.getValue().toString().split("-");

        ArrayList<String> newAttestation = new ArrayList<>();
        newAttestation.add(attId.getText());
        newAttestation.add(attSubject.getValue().toString());
        newAttestation.add(attType.getValue().toString());
        newAttestation.add("0");
        newAttestation.add(attDate.getValue().toString());
        newAttestation.add(attTeacher.getValue().toString());
        newAttestation.add(arr[1]);
        boolean flag = true;
        for (int i = 0;i<newAttestation.size();i++) {
            if(newAttestation.get(i).equals(bufferAttestation.get(i)) == false)
            {flag = false; break;}
        }


        if(flag){
            save.setVisible(false);
            labelId.setVisible(false);
            attId.setVisible(false);
            initialize();
            ClearText();
        }
        else
        {
            int attestId = tableAttestation.getSelectionModel().getSelectedItem().getAttestationId();
            String sql = String.format("UPDATE Attestations SET Active = 0 Where AttestationId = %s",attestId);
            DbHandler.Statement().execute(sql);

            sql= String.format("INSERT INTO Attestations (AttestationId,Subject,Type,Date,Teacher,SpecialityId,Active) VALUES (%s,'%s','%s','%s','%s',%s,%s)",
                    attestId,
                    attSubject.getValue().toString(),
                    attType.getValue().toString(),
                    attDate.getValue().toString(),
                    attTeacher.getValue().toString(),
                    Integer.parseInt(arr[1]),
                    1);
            System.out.println(" ");
            DbHandler.Statement().execute(sql);
            save.setVisible(false);
            labelId.setVisible(false);
            attId.setVisible(false);
            initialize();
            ClearText();

        }
    }

    private void ClearText() {
        attId.clear();
        attSubject.setPromptText("");
        attType.setPromptText(null);
        attType.setPromptText("");
        attSpecId.setPromptText("");
        attTeacher.setPromptText("");
        attType.setPromptText("");

    }
}
