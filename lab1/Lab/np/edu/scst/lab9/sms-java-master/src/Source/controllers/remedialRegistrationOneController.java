package Source.controllers;

import Source.sharedUtils.Phone;
import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

import static Source.sharedUtils.Phone.countryCodesList;

public class remedialRegistrationOneController implements Initializable {

    @FXML
    private StackPane mainContainer;
    @FXML
    private JFXComboBox<String> sexCombo;
    @FXML
    private JFXComboBox<String> nationalityCombo;
    @FXML
    private JFXComboBox<String> titleCombo1;
    @FXML
    private JFXComboBox<String> titleCombo2;
    @FXML
    private JFXComboBox<String> titleCombo3;
    @FXML
    private JFXComboBox<Phone.CountryCodes> countryCodeCombo1;
    @FXML
    private JFXComboBox<Phone.CountryCodes> countryCodeCombo2;
    @FXML
    private JFXComboBox<Phone.CountryCodes> countryCodeCombo3;
    @FXML
    private JFXComboBox<Phone.CountryCodes> countryCodeCombo4;
    @FXML
    private JFXDatePicker dobDatePicker;
//    @FXML
//    private JFXDatePicker fromDatePicker;
//    @FXML
//    private JFXDatePicker toDatePicker;
    @FXML
    private JFXDatePicker adDatePicker;
    @FXML
    private JFXDatePicker comDatePicker;
    @FXML
    private JFXTextField studentEmailTextField;
    @FXML
    private JFXTextField fatherEmailTextField;
    @FXML
    private JFXTextField motherEmailTextField;
    @FXML
    private JFXTextField sponsorEmailTextField;
    @FXML
    private JFXTextField studentPhoneTextField;
    @FXML
    private JFXTextField fatherPhoneTextField;
    @FXML
    private JFXTextField motherPhoneTextField;
    @FXML
    private JFXTextField sponsorPhoneTextField;
    @FXML
    private JFXTextField stud_surnameTextField;
    @FXML
    private JFXTextField stud_other_nameTextField;
    @FXML
    private JFXTextField stud_religionTextField;
//    @FXML
//    private JFXTextField stud_futureTextField;
//    @FXML
//    private JFXTextField stud_jhsTextField;
//    @FXML
//    private JFXTextField stud_beceTextField;
    @FXML
    private JFXTextField stud_houseNo;
    @FXML
    private JFXTextField stud_Location;
    @FXML
    private JFXTextField fatherSurnameTextField;
    @FXML
    private JFXTextField fatherOtherNameTextField;
    @FXML
    private JFXTextField motherSurnameTextField;
    @FXML
    private JFXTextField motherOtherNameTextField;
    @FXML
    private JFXTextField sponsorSurnameTextField;
    @FXML
    private JFXTextField sponsorOtherNameTextField;
    @FXML
    private JFXTextField fatherOccupationTextField;
    @FXML
    private JFXTextField father_P_AddressTextField;
    @FXML
    private JFXTextField father_R_AddressTextField;
    @FXML
    private JFXTextField motherOccupationTextField;
    @FXML
    private JFXTextField mother_P_AddressTextField;
    @FXML
    private JFXTextField mother_R_AddressTextField;
    @FXML
    private JFXTextField sponsorOccupationTextField;
    @FXML
    private JFXTextField sponsor_P_AddressTextField;
    @FXML
    private JFXTextField sponsor_R_AddressTextField;
    @FXML
    private JFXComboBox<String> residentialStatus;
    @FXML
    private JFXComboBox<Programmes> programmesCombo;
    @FXML
    private CheckBox addFather;
    @FXML
    private CheckBox addMother;
    @FXML
    private CheckBox addSponsor;
    @FXML
    private VBox fatherContainer;
    @FXML
    private VBox motherContainer;
    @FXML
    private VBox sponsorContainer;
    @FXML
    private ScrollPane scrollContainer;
    @FXML
    private Pane snackBarPane;

    private MaskerPane maskerPane = new MaskerPane();



    private ObservableList<String> listOfNations = FXCollections.observableArrayList();
    private ObservableList<Programmes> listOfProgrammes = FXCollections.observableArrayList();
    private ObservableList<String> sexList = FXCollections.observableArrayList("Male", "Female", "Other");
    private ObservableList<String> resList = FXCollections.observableArrayList("Boarding", "Day", "Other");
    private ObservableList<String> titleList = FXCollections.observableArrayList(
            "Mr.", "Mrs.", "Dr.", "Rev.", "Prof.", "Eng.","Lawyer", "Barrister","Bishop","Master", "Justice", "Hon.", "Madam");

    private String studentInputEmail = "";
    private String fatherInputEmail = "";
    private String motherInputEmail = "";
    private String sponsorInputEmail = "";

    private String studentInputPhone = "";
    private String fatherInputPhone = "";
    private String motherInputPhone = "";
    private String sponsorInputPhone = "";

    private String studentInputSurname = "";
    private String studentInputOther_names = "";
    private String studentInputSex = "";
    private String studentInputDoB= "";
    private String studentInputNationality = "";
    private String studentInputReligion = "";
//    private String studentInputFuture = "";
//    private String studentInputJHS = "";
//    private String studentInputFromDate = "";
//    private String studentInputToDate = "";
//    private String studentInputBECE = "";

    private String code1 = "";
    private String code2 = "";
    private String code3 = "";
    private String code4 = "";

    private String fatherTitle = "";
    private String motherTitle = "";
    private String sponsorTitle = "";

    private String fatherSurname = "";
    private String fatherOtherName = "";
    private String fatherOccupation = "";
    private String father_P_Address = "";
    private String father_R_Address = "";

    private String motherSurname = "";
    private String motherOtherName = "";
    private String motherOccupation = "";
    private String mother_P_Address = "";
    private String mother_R_Address = "";

    private String sponsorSurname = "";
    private String sponsorOtherName = "";
    private String sponsorOccupation = "";
    private String sponsor_P_Address = "";
    private String sponsor_R_Address = "";

    private String admissionDate = "";
    private String completionDate = "";

    private String studentResStatus = "";
    private Programmes studentProgramme = null;

    private UUID generatedStudentsID = null;
    private UUID generatedFatherID = null;
    private UUID generatedMotherID = null;
    private UUID generatedSponsorID = null;


    private boolean student_details_error = false;
    private boolean student_contact_error = false;
    private boolean father_details_error = false;
    private boolean father_contact_error = false;
    private boolean mother_details_error = false;
    private boolean mother_contact_error = false;
    private boolean sponsor_details_error = false;
    private boolean sponsor_contact_error = false;
//    private boolean student_edu_error = false;
    private boolean student_programme_error = false;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mainContainer.getChildren().add(maskerPane);
        maskerPane.setVisible(false);

        getListOfNations();

        setUpLists();
        setCountryCodeCombo1();
        setCountryCodeCombo2();
        setCountryCodeCombo3();
        setCountryCodeCombo4();
        setProgrammeComboBox();
        setDateFormat();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {
                getInstitutionsProgrammes();
                return null;
            }
        };
        new Thread(task).start();

        // get default values
        studentInputNationality = nationalityCombo.getValue();
        code1 = countryCodeCombo1.getValue().getCode();
        code2 = countryCodeCombo2.getValue().getCode();
        code3 = countryCodeCombo3.getValue().getCode();
        code4 = countryCodeCombo4.getValue().getCode();
        studentResStatus = residentialStatus.getValue();

    }

    private void getListOfNations(){
        String [] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes){
            Locale obj = new Locale("", countryCode);

            listOfNations.add(obj.getDisplayCountry());
        }

        Collections.sort(listOfNations);
    }

    private void setUpLists(){
        sexCombo.setItems(sexList);

        nationalityCombo.setItems(listOfNations);
        nationalityCombo.setValue("Ghana");

        titleCombo1.setItems(titleList);
        titleCombo2.setItems(titleList);
        titleCombo3.setItems(titleList);

        residentialStatus.setItems(resList);

        residentialStatus.setValue("Day");
    }

    private void setCountryCodeCombo1(){
        Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>> cellFactory =
                new Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>>() {

                    @Override
                    public ListCell<Phone.CountryCodes> call(ListView<Phone.CountryCodes> l) {
                        return new ListCell<Phone.CountryCodes>() {

                            @Override
                            protected void updateItem(Phone.CountryCodes item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getCodeAndCountry());
                                }
                            }
                        } ;
                    }
                };

        countryCodeCombo1.setConverter(new StringConverter<Phone.CountryCodes>() {
            @Override
            public String toString(Phone.CountryCodes code) {
                if (code == null){
                    return null;
                } else {
                    return code.getCode();
                }
            }

            @Override
            public Phone.CountryCodes fromString(String m_code) {
                return null;
            }
        });


        countryCodeCombo1.setButtonCell(new ListCell<Phone.CountryCodes>() {
            @Override
            protected void updateItem(Phone.CountryCodes t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getCode());
                } else {
                    setText(null);
                }

            }
        });

        countryCodeCombo1.setCellFactory(cellFactory);
        countryCodeCombo1.setItems(countryCodesList);

        //set default value to GH
        countryCodeCombo1.setValue(new Phone.CountryCodes("+233", "GH +233"));

    }

    private void setCountryCodeCombo2(){
        Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>> cellFactory =
                new Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>>() {

                    @Override
                    public ListCell<Phone.CountryCodes> call(ListView<Phone.CountryCodes> l) {
                        return new ListCell<Phone.CountryCodes>() {

                            @Override
                            protected void updateItem(Phone.CountryCodes item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getCodeAndCountry());
                                }
                            }
                        } ;
                    }
                };

        countryCodeCombo2.setConverter(new StringConverter<Phone.CountryCodes>() {
            @Override
            public String toString(Phone.CountryCodes code) {
                if (code == null){
                    return null;
                } else {
                    return code.getCode();
                }
            }

            @Override
            public Phone.CountryCodes fromString(String m_code) {
                return null;
            }
        });

        countryCodeCombo2.setButtonCell(new ListCell<Phone.CountryCodes>() {
            @Override
            protected void updateItem(Phone.CountryCodes t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getCode());
                } else {
                    setText(null);
                }

            }
        });
        countryCodeCombo2.setCellFactory(cellFactory);
        countryCodeCombo2.setItems(countryCodesList);

        //set default value to GH
        countryCodeCombo2.setValue(new Phone.CountryCodes("+233", "GH +233"));

    }

    private void setCountryCodeCombo3(){
        Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>> cellFactory =
                new Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>>() {

                    @Override
                    public ListCell<Phone.CountryCodes> call(ListView<Phone.CountryCodes> l) {
                        return new ListCell<Phone.CountryCodes>() {

                            @Override
                            protected void updateItem(Phone.CountryCodes item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getCodeAndCountry());
                                }
                            }
                        } ;
                    }
                };

        countryCodeCombo3.setConverter(new StringConverter<Phone.CountryCodes>() {
            @Override
            public String toString(Phone.CountryCodes code) {
                if (code == null){
                    return null;
                } else {
                    return code.getCode();
                }
            }

            @Override
            public Phone.CountryCodes fromString(String m_code) {
                return null;
            }
        });

        countryCodeCombo3.setButtonCell(new ListCell<Phone.CountryCodes>() {
            @Override
            protected void updateItem(Phone.CountryCodes t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getCode());
                } else {
                    setText(null);
                }

            }
        });
        countryCodeCombo3.setCellFactory(cellFactory);
        countryCodeCombo3.setItems(countryCodesList);

        //set default value to GH
        countryCodeCombo3.setValue(new Phone.CountryCodes("+233", "GH +233"));

    }

    private void setCountryCodeCombo4(){
        Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>> cellFactory =
                new Callback<ListView<Phone.CountryCodes>, ListCell<Phone.CountryCodes>>() {

                    @Override
                    public ListCell<Phone.CountryCodes> call(ListView<Phone.CountryCodes> l) {
                        return new ListCell<Phone.CountryCodes>() {

                            @Override
                            protected void updateItem(Phone.CountryCodes item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getCodeAndCountry());
                                }
                            }
                        } ;
                    }
                };

        countryCodeCombo4.setConverter(new StringConverter<Phone.CountryCodes>() {
            @Override
            public String toString(Phone.CountryCodes code) {
                if (code == null){
                    return null;
                } else {
                    return code.getCode();
                }
            }

            @Override
            public Phone.CountryCodes fromString(String m_code) {
                return null;
            }
        });

        countryCodeCombo4.setButtonCell(new ListCell<Phone.CountryCodes>() {
            @Override
            protected void updateItem(Phone.CountryCodes t, boolean bln) {
                super.updateItem(t, bln);
                if (t != null) {
                    setText(t.getCode());
                } else {
                    setText(null);
                }

            }
        });
        countryCodeCombo4.setCellFactory(cellFactory);
        countryCodeCombo4.setItems(countryCodesList);

        //set default value to GH
        countryCodeCombo4.setValue(new Phone.CountryCodes("+233", "GH +233"));

    }

    private void setProgrammeComboBox(){
        Callback<ListView<Programmes>, ListCell<Programmes>> cellFactory =
                new Callback<ListView<Programmes>, ListCell<Programmes>>() {

                    @Override
                    public ListCell<Programmes> call(ListView<Programmes> l) {
                        return new ListCell<Programmes>() {

                            @Override
                            protected void updateItem(Programmes item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getProgName());
                                }
                            }
                        } ;
                    }
                };

        programmesCombo.setConverter(new StringConverter<Programmes>() {
            @Override
            public String toString(Programmes prog) {
                if (prog == null){
                    return null;
                } else {
                    return prog.getProgName();
                }
            }

            @Override
            public Programmes fromString(String _prog_) {
                return null;
            }
        });
        programmesCombo.setCellFactory(cellFactory);
        programmesCombo.setItems(listOfProgrammes);
    }

    private void setDateFormat(){
        Utils.convertDate(dobDatePicker);
//        Utils.convertDate(fromDatePicker);
//        Utils.convertDate(toDatePicker);
        Utils.convertDate(adDatePicker);
        Utils.convertDate(comDatePicker);
    }

    public void validateStudentEmailWileTyping(){

        studentInputEmail = studentEmailTextField.getText().trim();
        boolean valid = Utils.isEmailValid(studentInputEmail);
        if (!valid){
            studentEmailTextField.setStyle("-fx-border-color: #FA8072");
            studentInputEmail = "";
        }else {
            studentEmailTextField.setStyle("-fx-border-color: white");

        }

    }

    public void validateFatherEmailWileTyping(){

        fatherInputEmail = fatherEmailTextField.getText().trim();
        boolean valid = Utils.isEmailValid(fatherInputEmail);
        if (!valid){
            fatherEmailTextField.setStyle("-fx-border-color: #FA8072");
            fatherInputEmail = "";
        }else {
            fatherEmailTextField.setStyle("-fx-border-color: white");

        }

    }

    public void validateMotherEmailWileTyping(){

        motherInputEmail = motherEmailTextField.getText().trim();
        boolean valid = Utils.isEmailValid(motherInputEmail);
        if (!valid){
            motherEmailTextField.setStyle("-fx-border-color: #FA8072");
            motherInputEmail = "";
        }else {
            motherEmailTextField.setStyle("-fx-border-color: white");

        }

    }

    public void validateSponsorEmailWileTyping(){

        sponsorInputEmail = sponsorEmailTextField.getText().trim();
        boolean valid = Utils.isEmailValid(sponsorInputEmail);
        if (!valid){
            sponsorEmailTextField.setStyle("-fx-border-color: #FA8072");
            sponsorInputEmail = "";
        }else {
            sponsorEmailTextField.setStyle("-fx-border-color: white");

        }
    }

    public void validateStudentPhoneNumberWhileTyping(){
        studentInputPhone = studentPhoneTextField.getText().trim();

        boolean valid = Utils.validateInputAsNumber(studentInputPhone);
        if (!valid){
            studentPhoneTextField.setStyle("-fx-border-color: #FA8072");
            studentInputPhone = "";
        }else {
            studentPhoneTextField.setStyle("-fx-border-color: white");
            studentInputPhone =  code1 + studentInputPhone;

        }
    }

    public void validateFatherPhoneNumberWhileTyping(){
        fatherInputPhone = fatherPhoneTextField.getText().trim();

        boolean valid = Utils.validateInputAsNumber(fatherInputPhone);
        if (!valid){
            fatherPhoneTextField.setStyle("-fx-border-color: #FA8072");
            fatherInputPhone = "";
        }else {
            fatherPhoneTextField.setStyle("-fx-border-color: white");
            fatherInputPhone =  code2 + fatherInputPhone;
        }
    }

    public void validateMotherPhoneNumberWhileTyping(){
        motherInputPhone = motherPhoneTextField.getText().trim();

        boolean valid = Utils.validateInputAsNumber(motherInputPhone);
        if (!valid){
            motherPhoneTextField.setStyle("-fx-border-color: #FA8072");
            motherInputPhone = "";
        }else {
            motherPhoneTextField.setStyle("-fx-border-color: white");
            motherInputPhone =  code3 + motherInputPhone;
        }
    }

    public void validateSponsorPhoneNumberWhileTyping(){
        sponsorInputPhone = sponsorPhoneTextField.getText().trim();

        boolean valid = Utils.validateInputAsNumber(sponsorInputPhone);
        if (!valid){
            sponsorPhoneTextField.setStyle("-fx-border-color: #FA8072");
            sponsorInputPhone = "";
        }else {
            sponsorPhoneTextField.setStyle("-fx-border-color: white");
            sponsorInputPhone =  code4 + sponsorInputPhone;
        }
    }

    private void getInstitutionsProgrammes() throws IOException {
        String sql = "select * from programmes";
        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Programmes_ID");
                String name = rs.getString("Programme_Name");
                listOfProgrammes.add(new Programmes(id, name));

            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void getStudentSurnameWhileTyping(){
        studentInputSurname = stud_surnameTextField.getText().trim();
    }

    public void getStudentOtherNamesWhileTyping(){
        studentInputOther_names = stud_other_nameTextField.getText().trim();
    }

    public void getStudentSexOnChange(){
        studentInputSex = sexCombo.getValue();
    }

    public void getStudentDoBOnChange(){
        String[] d = dobDatePicker.getEditor().getText().split("/");
        studentInputDoB = d[2] + "-" + d[1] + "-" + d[0];
    }

    public void getStudentNationalityOnChange(){
        studentInputNationality = nationalityCombo.getValue();
    }

    public void getStudentReligion(){
        studentInputReligion = stud_religionTextField.getText().trim();
    }

//    public void getStudentFuture(){
//        studentInputFuture = stud_futureTextField.getText().trim();
//    }

//    public void getStudentJHS(){
//        studentInputJHS = stud_jhsTextField.getText().trim();
//    }

//    public void getStudentFromDate(){
//        String[] d = fromDatePicker.getEditor().getText().split("/");
//        studentInputFromDate = d[2] + "-" + d[1] + "-" + d[0];
//    }

//    public void getStudentToDate(){
//        String[] d = toDatePicker.getEditor().getText().split("/");
//        studentInputToDate = d[2] + "-" + d[1] + "-" + d[0];
//    }

//    public void getStudentBECE(){
//        studentInputBECE = stud_beceTextField.getText().trim();
//    }

    public void getCode1(){
        code1 = countryCodeCombo1.getValue().getCode();
        validateStudentPhoneNumberWhileTyping();
    }
    public void getCode2(){
        code2 = countryCodeCombo2.getValue().getCode();
        validateFatherPhoneNumberWhileTyping();
    }
    public void getCode3(){
        code3 = countryCodeCombo3.getValue().getCode();
        validateMotherPhoneNumberWhileTyping();
    }
    public void getCode4(){
        code4 = countryCodeCombo4.getValue().getCode();
        validateSponsorPhoneNumberWhileTyping();
    }

    public void getFatherTitle(){
        fatherTitle = titleCombo1.getValue();
    }
    public void getMotherTitle(){
        motherTitle = titleCombo2.getValue();
    }
    public void getSponsorTitle(){
        sponsorTitle = titleCombo3.getValue();
    }

    public void getFatherSurname(){
        fatherSurname = fatherSurnameTextField.getText().trim();
    }

    public void getFatherOtherName(){
        fatherOtherName = fatherOtherNameTextField.getText().trim();
    }

    public void getFatherOccupation(){
        fatherOccupation = fatherOccupationTextField.getText().trim();
    }

    public void getFatherPostAddress(){
        father_P_Address = father_P_AddressTextField.getText().trim();
    }

    public void getFatherResAddress(){
        father_R_Address = father_R_AddressTextField.getText().trim();
    }

    public void getMotherSurname(){
        motherSurname = motherSurnameTextField.getText().trim();
    }

    public void getMotherOtherName(){
        motherOtherName = motherOtherNameTextField.getText().trim();
    }

    public void getMotherOccupation(){
        motherOccupation = motherOccupationTextField.getText().trim();
    }

    public void getMotherPostAddress(){
        mother_P_Address = mother_P_AddressTextField.getText().trim();
    }

    public void getMotherResAddress(){
        mother_R_Address = mother_R_AddressTextField.getText().trim();
    }

    public void getSponsorSurname(){
        sponsorSurname = sponsorSurnameTextField.getText().trim();
    }

    public void getSponsorOtherName(){
        sponsorOtherName = sponsorOtherNameTextField.getText().trim();
    }

    public void getSponsorOccupation(){
        sponsorOccupation = sponsorOccupationTextField.getText().trim();
    }

    public void getSponsorPostAddress(){
        sponsor_P_Address = sponsor_P_AddressTextField.getText().trim();
    }

    public void getSponsorResAddress(){
        sponsor_R_Address = sponsor_R_AddressTextField.getText().trim();
    }

    public void getAdmissionDate(){
        String[] d = adDatePicker.getEditor().getText().split("/");
        admissionDate = d[2] + "-" + d[1] + "-" + d[0];
    }

    public void getCompletionDate(){
        String[] d = comDatePicker.getEditor().getText().split("/");
        completionDate = d[2] + "-" + d[1] + "-" + d[0];
    }

    public void getResidentialStatus(){
        studentResStatus = residentialStatus.getValue();
    }

    public void getStudentProgramme(){
        studentProgramme = programmesCombo.getValue();
    }

    private boolean validateInputs(){

        resetStyle();
        String color = "#FA8072";
        // student details
        if (studentInputSurname.isEmpty()){
            stud_surnameTextField.setStyle("-fx-border-color: "+color);
            return false;
        }else if (studentInputOther_names.isEmpty()){
            stud_other_nameTextField.setStyle("-fx-border-color: "+color);
            return false;
        }else if (studentInputSex.isEmpty()){
            sexCombo.setStyle("-fx-border-color: "+color);
            return false;
        }else if (studentInputDoB.isEmpty()){
            dobDatePicker.setStyle("-fx-border-color: "+color);
            return false;
        }
//        else if (studentInputJHS.isEmpty()){
//            stud_jhsTextField.setStyle("-fx-border-color: "+color);
//            return false;
//        }else if (studentInputFromDate.isEmpty()){
//            fromDatePicker.setStyle("-fx-border-color: "+color);
//            return false;
//        }else if (studentInputToDate.isEmpty()){
//            toDatePicker.setStyle("-fx-border-color: "+color);
//            return  false;
//        }else if (studentInputBECE.isEmpty()){
//            stud_beceTextField.setStyle("-fx-border-color: "+color);
//            return false;
//            // student address
//        }
        else if (studentInputPhone.isEmpty()){
            studentPhoneTextField.setStyle("-fx-border-color: "+color);
            return false;
        }else if (studentInputEmail.isEmpty()){
            studentEmailTextField.setStyle("-fx-border-color: "+color);
            return false;
            // admission details
        }else if (admissionDate.isEmpty()){
            adDatePicker.setStyle("-fx-border-color: "+color);
            return false;
        }else if (completionDate.isEmpty()){
            comDatePicker.setStyle("-fx-border-color: "+color);
            return false;
        }else if (studentResStatus.isEmpty()){
            residentialStatus.setStyle("-fx-border-color: "+color);
            return false;
        }else if (studentProgramme == null){
            programmesCombo.setStyle("-fx-border-color: "+color);
            return false;
        }else return true;

    }

    private boolean validateFatherInput(){
        resetStyle();
        String color = "#FA8072";

        boolean proceed = false;
        if (addFather.isSelected()){
            if(fatherSurname.isEmpty()){
                fatherSurnameTextField.setStyle("-fx-border-color: "+color);
            }else if (fatherOtherName.isEmpty()){
                fatherOtherNameTextField.setStyle("-fx-border-color: "+color);
            }else if (fatherInputPhone.isEmpty()){
                fatherPhoneTextField.setStyle("-fx-border-color: "+color);
            }else if (fatherInputEmail.isEmpty()){
                fatherEmailTextField.setStyle("-fx-border-color: "+color);
            }else {
                proceed = true;
            }
        }else {
            proceed = true;
        }
        return proceed;
    }

    private boolean validateMotherInput(){
        resetStyle();
        String color = "#FA8072";

        boolean proceed = false;
        if (addMother.isSelected()){
            if(motherSurname.isEmpty()){
                motherSurnameTextField.setStyle("-fx-border-color: "+color);
            }else if (motherOtherName.isEmpty()){
                motherOtherNameTextField.setStyle("-fx-border-color: "+color);
            }else if (motherInputPhone.isEmpty()){
                motherPhoneTextField.setStyle("-fx-border-color: "+color);
            }else if (motherInputEmail.isEmpty()){
                motherEmailTextField.setStyle("-fx-border-color: "+color);
            }else {
                proceed = true;
            }
        }else {
            proceed = true;
        }
        return proceed;
    }

    private boolean validateSponsorInput(){
        resetStyle();
        String color = "#FA8072";

        boolean proceed = false;
        if (addSponsor.isSelected()){
            if(sponsorSurname.isEmpty()){
                sponsorSurnameTextField.setStyle("-fx-border-color: "+color);
                scrollContainer.setVvalue( 100);
            }else if (sponsorOtherName.isEmpty()){
                sponsorOtherNameTextField.setStyle("-fx-border-color: "+color);
                scrollContainer.setVvalue( 100);
            }else if (sponsorInputPhone.isEmpty()){
                sponsorPhoneTextField.setStyle("-fx-border-color: "+color);
                scrollContainer.setVvalue( 100);
            }else if (sponsorInputEmail.isEmpty()){
                sponsorEmailTextField.setStyle("-fx-border-color: "+color);
                scrollContainer.setVvalue( 100);
            }else {
                scrollContainer.setVvalue( 0);
                proceed = true;
            }
        }else {
            proceed = true;
        }
        return proceed;
    }

    private void resetStyle(){
        String color = "white";
        stud_surnameTextField.setStyle("-fx-border-color: "+color);
        stud_other_nameTextField.setStyle("-fx-border-color: "+color);
        sexCombo.setStyle("-fx-border-color: "+color);
        dobDatePicker.setStyle("-fx-border-color: "+color);
//        stud_jhsTextField.setStyle("-fx-border-color: "+color);
//        fromDatePicker.setStyle("-fx-border-color: "+color);
//        toDatePicker.setStyle("-fx-border-color: "+color);
//        stud_beceTextField.setStyle("-fx-border-color: "+color);
        studentPhoneTextField.setStyle("-fx-border-color: "+color);
        studentEmailTextField.setStyle("-fx-border-color: "+color);
        adDatePicker.setStyle("-fx-border-color: "+color);
        comDatePicker.setStyle("-fx-border-color: "+color);
        residentialStatus.setStyle("-fx-border-color: "+color);
        programmesCombo.setStyle("-fx-border-color: "+color);
        fatherSurnameTextField.setStyle("-fx-border-color: "+color);
        fatherOtherNameTextField.setStyle("-fx-border-color: "+color);
        fatherPhoneTextField.setStyle("-fx-border-color: "+color);
        fatherEmailTextField.setStyle("-fx-border-color: "+color);
        motherSurnameTextField.setStyle("-fx-border-color: "+color);
        motherOtherNameTextField.setStyle("-fx-border-color: "+color);
        motherPhoneTextField.setStyle("-fx-border-color: "+color);
        motherEmailTextField.setStyle("-fx-border-color: "+color);
        sponsorSurnameTextField.setStyle("-fx-border-color: "+color);
        sponsorOtherNameTextField.setStyle("-fx-border-color: "+color);
        sponsorPhoneTextField.setStyle("-fx-border-color: "+color);
        sponsorEmailTextField.setStyle("-fx-border-color: "+color);
    }

    public void onSaveBtnClicked(){

        if (!validateInputs() || !validateSponsorInput() || !validateMotherInput() || !validateFatherInput()){
            Utils.showSnackBar(snackBarPane, "Some mandatory fields are empty", 2000);
        }else {
            applicationState.setUIState("disable");
            maskerPane.setVisible(true);

            Task<Void> task = new Task<Void>(){

                @Override
                protected Void call() throws IOException {
                    createStudentsInDB();
                    createStudentContactInDB();
                    createGuardianDetailsInDB();
//                    createStudentEducationalBackground();
                    linkStudentToProgramme();

                    Platform.runLater(()-> handleError());
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    public void addFatherListener(){
        if (addFather.isSelected()){
            fatherContainer.setDisable(false);
            scrollContainer.setVvalue( 0);
        }else {
            fatherContainer.setDisable(true);
        }
    }
    public void addMotherListener(){
        if (addMother.isSelected()){
            motherContainer.setDisable(false);
        }else {
            motherContainer.setDisable(true);
        }
    }
    public void addSponsorListener(){
        if (addSponsor.isSelected()){
            sponsorContainer.setDisable(false);
            scrollContainer.setVvalue( 100);
        }else {
            sponsorContainer.setDisable(true);
        }
    }

    private void createStudentsInDB() throws IOException {

        generatedStudentsID = UUID.randomUUID();
        String randomID = generatedStudentsID.toString();

        String sql = "insert into students (`Student_ID`, `Surname`, `Other_Names`, `Sex`, `Date_Of_Birth`, `Place_Of_Birth`," +
                "`Nationality`, `Religion`, `Aspiring_Career`, `Admission_Date`, `Completion_Date`," +
                " `Admission_Type`, `Status`, `Residential_Status`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, randomID);
            preparedStatement.setString(2, studentInputSurname);
            preparedStatement.setString(3, studentInputOther_names);
            preparedStatement.setString(4, studentInputSex);
            preparedStatement.setString(5, studentInputDoB);
            preparedStatement.setString(6, ""); // On the admission form for regular this field is absent
            preparedStatement.setString(7, studentInputNationality);
            preparedStatement.setString(8, studentInputReligion);
            preparedStatement.setString(9, "");
            preparedStatement.setString(10, admissionDate);
            preparedStatement.setString(11, completionDate);
            preparedStatement.setString(12, "remedial");
            preparedStatement.setString(13, "active");
            preparedStatement.setString(14, studentResStatus);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            student_details_error = true;
            Utils.printSQLException(e);
        }

    }

    private void createStudentContactInDB() throws IOException {
        String sql = "insert into student_contacts(`Phone_Number`, `Email`, `House_Number`, `Postal_Address`, `Location`, `Student_ID`)" +
                " values(?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, studentInputPhone);
            preparedStatement.setString(2, studentInputEmail);
            preparedStatement.setString(3, stud_houseNo.getText().trim());
            preparedStatement.setString(4, ""); // this field is absent for regular students
            preparedStatement.setString(5, stud_Location.getText().trim());
            preparedStatement.setString(6, generatedStudentsID.toString());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            student_contact_error = true;
            Utils.printSQLException(e);
        }
    }

    private void createGuardianDetailsInDB() throws IOException {
        if (addFather.isSelected()){
            createFatherDetails();
            createFatherContact();
        }
        if (addMother.isSelected()){
            createMotherDetails();
            createMotherContact();
        }
        if (addSponsor.isSelected()){
            createSponsorDetails();
            createSponsorContact();
        }
    }

    private void createFatherDetails()throws IOException{

        generatedFatherID = UUID.randomUUID();

        String sql = "insert into guardians (`Guardian_ID`, `Relationship`, `Surname`, `Other_Names`, `Occupation`, `Title`, `Student_ID`)" +
                " values (?,?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, generatedFatherID.toString());
            preparedStatement.setString(2, "Father");
            preparedStatement.setString(3, fatherSurname);
            preparedStatement.setString(4, fatherOtherName);
            preparedStatement.setString(5, fatherOccupation);
            preparedStatement.setString(6, fatherTitle);
            preparedStatement.setString(7, generatedStudentsID.toString());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            father_details_error = true;
            Utils.printSQLException(e);
        }
    }

    private void createFatherContact()throws IOException{
        String sql = "insert into guardian_contacts (`Phone_Number`, `Email`, `House_Number`, `Postal_Address`, `Location`, `Guardian_ID`) " +
                "values (?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fatherInputPhone);
            preparedStatement.setString(2, fatherInputEmail);
            preparedStatement.setString(3, "");
            preparedStatement.setString(4, father_P_Address);
            preparedStatement.setString(5, father_R_Address);
            preparedStatement.setString(6, generatedFatherID.toString());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            father_contact_error = true;
            Utils.printSQLException(e);
        }
    }

    private void createMotherDetails()throws IOException{

        generatedMotherID = UUID.randomUUID();

        String sql = "insert into guardians (`Guardian_ID`, `Relationship`, `Surname`, `Other_Names`, `Occupation`, `Title`, `Student_ID`)" +
                " values (?,?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, generatedMotherID.toString());
            preparedStatement.setString(2, "Mother");
            preparedStatement.setString(3, motherSurname);
            preparedStatement.setString(4, motherOtherName);
            preparedStatement.setString(5, motherOccupation);
            preparedStatement.setString(6, motherTitle);
            preparedStatement.setString(7, generatedStudentsID.toString());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            mother_details_error = true;
            Utils.printSQLException(e);
        }
    }

    private void createMotherContact()throws IOException{
        String sql = "insert into guardian_contacts (`Phone_Number`, `Email`, `House_Number`, `Postal_Address`, `Location`, `Guardian_ID`) " +
                "values (?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, motherInputPhone);
            preparedStatement.setString(2, motherInputEmail);
            preparedStatement.setString(3, "");
            preparedStatement.setString(4, mother_P_Address);
            preparedStatement.setString(5, mother_R_Address);
            preparedStatement.setString(6, generatedMotherID.toString());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            mother_contact_error = true;
            Utils.printSQLException(e);
        }
    }

    private void createSponsorDetails()throws IOException{

        generatedSponsorID = UUID.randomUUID();

        String sql = "insert into guardians (`Guardian_ID`, `Relationship`, `Surname`, `Other_Names`, `Occupation`, `Title`, `Student_ID`)" +
                " values (?,?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, generatedSponsorID.toString());
            preparedStatement.setString(2, "Mother");
            preparedStatement.setString(3, sponsorSurname);
            preparedStatement.setString(4, sponsorOtherName);
            preparedStatement.setString(5, sponsorOccupation);
            preparedStatement.setString(6, sponsorTitle);
            preparedStatement.setString(7, generatedStudentsID.toString());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void createSponsorContact()throws IOException{
        String sql = "insert into guardian_contacts (`Phone_Number`, `Email`, `House_Number`, `Postal_Address`, `Location`, `Guardian_ID`) " +
                "values (?,?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sponsorInputPhone);
            preparedStatement.setString(2, sponsorInputEmail);
            preparedStatement.setString(3, "");
            preparedStatement.setString(4, sponsor_P_Address);
            preparedStatement.setString(5, sponsor_R_Address);
            preparedStatement.setString(6, generatedSponsorID.toString());

            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

//    private void createStudentEducationalBackground(){
//        String sql = "insert into regular_student_educational_background (`JHS_Attended`, `From`, `To`, `BECE_Aggregate`, `Student_ID`) " +
//                "values (?,?,?,?,?)";
//
//        try (Connection connection = Utils.connectToDatabase();
//
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setString(1, studentInputJHS);
//            preparedStatement.setString(2, studentInputFromDate);
//            preparedStatement.setString(3, studentInputToDate);
//            preparedStatement.setString(4, studentInputBECE);
//            preparedStatement.setString(5, generatedStudentsID.toString());
//
//            System.out.println(preparedStatement);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            student_edu_error = true;
//            Utils.printSQLException(e);
//        }
//
//    }

    private void linkStudentToProgramme()throws IOException{
        String sql = "insert into student_programme (`Programme_ID`, `Student_ID`, `Status`) values (?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentProgramme.getProgID());
            preparedStatement.setString(2, generatedStudentsID.toString());
            preparedStatement.setString(3, "active");

            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            student_programme_error = true;
            Utils.printSQLException(e);
        }
    }

    private void handleError(){

        maskerPane.setVisible(false);
        applicationState.setUIState("enable");

        if (student_details_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createStudentsInDB();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            student_details_error = false;
        }
        if (student_contact_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createStudentContactInDB();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            student_contact_error = false;
        }
        if (father_details_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createFatherDetails();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            father_details_error = false;
        }
        if (father_contact_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createFatherContact();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            father_contact_error = false;
        }
        if (mother_details_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createMotherDetails();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            mother_details_error = false;
        }
        if (mother_contact_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createMotherContact();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            mother_contact_error = false;
        }
        if (sponsor_details_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createSponsorDetails();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            sponsor_details_error = false;
        }
        if (sponsor_contact_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        createSponsorContact();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            sponsor_contact_error = false;
        }
//        if (student_edu_error){
//            int res = Utils.generateErrorDialog();
//
//            if (res == Utils.YES){
//
//                maskerPane.setVisible(true);
//                applicationState.setUIState("disable");
//
//                Task<Void> task = new Task<Void>(){
//
//                    @Override
//                    protected Void call() {
//
//                        createStudentEducationalBackground();
//
//                        Platform.runLater(()-> handleError());
//                        return null;
//                    }
//                };
//                new Thread(task).start();
//            }
//            student_edu_error = false;
//        }
        if (student_programme_error){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {

                        linkStudentToProgramme();

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }
//            student_programme_error = false;
        }
        if(!student_details_error && !student_contact_error && !father_details_error && !father_contact_error &&
                !mother_details_error && !mother_contact_error && !sponsor_details_error && !sponsor_contact_error
                && !student_programme_error){

            Utils.showSnackBar(snackBarPane, "Saved Successfully", 2000);
            refreshPage();

            // create a remedial students without a class
            Task<Void> task = new Task<Void>(){

                @Override
                protected Void call() throws IOException {
                    createFreeRangeStudent();
                    createCourseLessStudent();
                    return null;
                }
            };
            new Thread(task).start();

            int res = Utils.confirmationDialog("Confirm",
                    "Would you want to add educational background?",
                    "Add educational background?" );
            if (res == Utils.YES){
                applicationState.setUIState("remedial_educational_background");
            }else {
                int response = Utils.confirmationDialog("Confirm",
                        "Would you want to assign this student a class?",
                        "Add student to class?" );
                if (response == Utils.YES){
                    applicationState.isRegular = false;
                    applicationState.setUIState("assign_regular_classroom");
                }
            }
        }

    }


    private void createFreeRangeStudent()throws IOException{
        String sql= "insert into remedial_students_without_class (`Student_ID`) values (?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, generatedStudentsID.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void createCourseLessStudent()throws IOException{
        String sql= "insert into remedial_student_without_course (`Student_ID`) values (?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, generatedStudentsID.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void refreshPage(){
        sexCombo.getSelectionModel().clearSelection();
        nationalityCombo.setValue("Ghana");
        titleCombo1.getSelectionModel().clearSelection();
        titleCombo2.getSelectionModel().clearSelection();
        titleCombo3.getSelectionModel().clearSelection();
        countryCodeCombo1.setValue(new Phone.CountryCodes("+233", "GH +233"));
        countryCodeCombo2.setValue(new Phone.CountryCodes("+233", "GH +233"));;
        countryCodeCombo3.setValue(new Phone.CountryCodes("+233", "GH +233"));;
        countryCodeCombo4.setValue(new Phone.CountryCodes("+233", "GH +233"));;
        dobDatePicker.getEditor().setText("");
//        fromDatePicker.getEditor().setText("");
//        toDatePicker.getEditor().setText("");
        adDatePicker.getEditor().setText("");
        comDatePicker.getEditor().setText("");
        studentEmailTextField.setText("");
        fatherEmailTextField.setText("");
        motherEmailTextField.setText("");
        sponsorEmailTextField.setText("");
        studentPhoneTextField.setText("");
        fatherPhoneTextField.setText("");
        motherPhoneTextField.setText("");
        sponsorPhoneTextField.setText("");
        stud_surnameTextField.setText("");
        stud_other_nameTextField.setText("");
        stud_religionTextField.setText("");
//        stud_futureTextField.setText("");
//        stud_jhsTextField.setText("");
//        stud_beceTextField.setText("");
        stud_houseNo.setText("");
        stud_Location.setText("");
        fatherSurnameTextField.setText("");
        fatherOtherNameTextField.setText("");
        motherSurnameTextField.setText("");
        motherOtherNameTextField.setText("");
        sponsorSurnameTextField.setText("");
        sponsorOtherNameTextField.setText("");
        fatherOccupationTextField.setText("");
        father_P_AddressTextField.setText("");
        father_R_AddressTextField.setText("");
        motherOccupationTextField.setText("");
        mother_P_AddressTextField.setText("");
        mother_R_AddressTextField.setText("");
        sponsorOccupationTextField.setText("");
        sponsor_P_AddressTextField.setText("");
        sponsor_R_AddressTextField.setText("");
        residentialStatus.getSelectionModel().clearSelection();
        programmesCombo.getSelectionModel().clearSelection();
        addFather.setSelected(false);
        addMother.setSelected(false);
        addSponsor.setSelected(false);
        fatherContainer.setDisable(true);
        motherContainer.setDisable(true);
        sponsorContainer.setDisable(true);
        scrollContainer.setVvalue(0);




        studentInputEmail = "";
        fatherInputEmail = "";
        motherInputEmail = "";
        sponsorInputEmail = "";

        studentInputPhone = "";
        fatherInputPhone = "";
        motherInputPhone = "";
        sponsorInputPhone = "";

        studentInputSurname = "";
        studentInputOther_names = "";
        studentInputDoB= "";
        studentInputNationality = "";
        studentInputReligion = "";
//        studentInputFuture = "";
//        studentInputFromDate = "";
//        studentInputToDate = "";
//        studentInputBECE = "";

//        code1 = "";
//        code2 = "";
//        code3 = "";
//        code4 = "";

        fatherTitle = "";
        motherTitle = "";
        sponsorTitle = "";

        fatherSurname = "";
        fatherOtherName = "";
        fatherOccupation = "";
        father_P_Address = "";
        father_R_Address = "";

        motherSurname = "";
        motherOtherName = "";
        motherOccupation = "";
        mother_P_Address = "";
        mother_R_Address = "";

        sponsorSurname = "";
        sponsorOtherName = "";
        sponsorOccupation = "";
        sponsor_P_Address = "";
        sponsor_R_Address = "";

        admissionDate = "";
        completionDate = "";

        studentResStatus = "";
        studentProgramme = null;

//        generatedStudentsID = null;
        generatedFatherID = null;
        generatedMotherID = null;
        generatedSponsorID = null;
    }

    public void onEDUBackgroundBtnClicked(){
        applicationState.setUIState("remedial_educational_background");
    }

    static class Programmes{
        private IntegerProperty progID = new SimpleIntegerProperty();
        private StringProperty progName = new SimpleStringProperty();

        Programmes(int id, String name){
            this.progID.set(id);
            this.progName.set(name);
        }

        public int getProgID(){
            return this.progID.get();
        }

        public String getProgName(){
            return this.progName.get();
        }
    }
}
