package Source.controllers;

import Source.sharedUtils.AutoCompleteTextField;
import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import Source.sharedUtils.sharedData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class remedialBackgroundController implements Initializable {

    @FXML
    private HBox autoCompleteContainer;
    @FXML
    private JFXListView<Student> studentJFXListView;
    @FXML
    private JFXTextField nameOfSchoolTextField;
    @FXML
    private JFXTextField addressTextField;
    @FXML
    private JFXComboBox<String> completedYearCombo;
    @FXML
    private VBox firstAttemptGroup;
    @FXML
    private VBox secondAttemptGroup;
    @FXML
    private VBox allContainer;
    @FXML
    private Pane snackBarPane;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXTextField school1TextField;
    @FXML
    private JFXTextField school2TextField;
    @FXML
    private CheckBox firstCheck;
    @FXML
    private CheckBox secondCheck;
    @FXML
    private JFXButton addBtn1;
    @FXML
    private JFXButton addBtn2;
    @FXML
    private StackPane masterContainer;

    private MaskerPane maskerPane = new MaskerPane();
    private AutoCompleteTextField programmedOffered = new AutoCompleteTextField();


    private String schoolName = "";
    private String schoolAddress = "";
    private String yearCompleted = "";
    private String programme = "";
    private String studentID = "";
    private String schoolOne = "";
    private String schoolTwo = "";

    private ArrayList<JFXTextField> firstAttemptSubjects = new ArrayList<>();
    private ArrayList<JFXComboBox<String>> firstAttemptGrades = new ArrayList<>();

    private ArrayList<JFXTextField> secondAttemptSubjects = new ArrayList<>();
    private ArrayList<JFXComboBox<String>> secondAttemptGrades = new ArrayList<>();


    private ObservableList<String> proGList = FXCollections.observableArrayList(
            "General Science",
            "General Arts",
            "Visual Arts",
            "Business - Secretarial",
            "Business - Accounting",
            "Home Economics",
            "Technical"
    );

    private ObservableList<String> grades = FXCollections.observableArrayList(
            "A", "A1", "B", "B1", "B2", "B3", "C", "C4", "C5", "C6", "D", "D7", "E", "E8", "F", "F9");

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    private boolean edu_error = false;
    private boolean history_error1 = false;
    private boolean history_error2 = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       setUpAutoComplete();
       completedYearCombo.setItems(sharedData.yearsList);
       allContainer.setDisable(true);
        saveBtn.setDisable(true);
        masterContainer.getChildren().add(maskerPane);
        maskerPane.setVisible(false);

        setStudentJFXListView();
        studentJFXListView.setItems(studentList);


        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws IOException {

                getStudentList();

                return null;
            }
        };
        new Thread(task).start();

        getProgrammeOffered();
    }
    private void setUpAutoComplete(){
        HBox.setHgrow(programmedOffered, Priority.ALWAYS);
        programmedOffered.setPrefHeight(25);
        programmedOffered.setPromptText("Programme Offered *");
        programmedOffered.setLabelFloat(true);
        programmedOffered.getEntries().addAll(proGList);
        autoCompleteContainer.getChildren().add(programmedOffered);
    }


    private void getStudentList() throws IOException {
        String sql = "select Student_ID, Surname, Other_Names from students where Admission_Type = 'remedial' and Status = 'active' order by Surname;";
        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");

                studentList.add(new Student(id, (s_name + " "+ o_name)));

            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setStudentJFXListView(){
        studentJFXListView.setCellFactory(new Callback<ListView<Student>, ListCell<Student>>() {
            @Override
            public ListCell<Student> call(ListView<Student> param) {
                ListCell<Student> cell = new ListCell<Student>(){
                    @Override
                    protected void updateItem(Student item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getStudentName());
                        }else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
    }

    public void onAddFirstAttempt(){
        HBox newHBox = new HBox();
        newHBox.setSpacing(20);

        JFXTextField subject = new JFXTextField();
        HBox.setHgrow(subject, Priority.ALWAYS);
        subject.setPrefHeight(25);
        subject.setPromptText("Subject");
        subject.setLabelFloat(true);

        JFXComboBox<String> gradeComboBox = new JFXComboBox<>();
        gradeComboBox.setPrefHeight(25);
        gradeComboBox.setPromptText("Grade");
        gradeComboBox.setLabelFloat(true);
        gradeComboBox.setItems(grades);

        JFXButton delBtn = new JFXButton("Del");
        delBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE);
        icon.setFill(Color.color(1.0,0,0));
        icon.setSize("15.0");
        delBtn.setGraphic(icon);

        delBtn.setOnAction(event -> {
           Node thisDelBtn = (Node) event.getSource();
           Node theHBox = thisDelBtn.getParent();

            firstAttemptGroup.getChildren().remove(theHBox);
          ObservableList<Node> listOfHBox =  firstAttemptGroup.getChildren();

            firstAttemptSubjects.clear();
            firstAttemptGrades.clear();

            for (Node oneNode : listOfHBox){
                HBox oneHBox = (HBox) oneNode;
                firstAttemptSubjects.add((JFXTextField) oneHBox.getChildren().get(0));
                firstAttemptGrades.add((JFXComboBox<String>) oneHBox.getChildren().get(1));
            }

        });


        firstAttemptSubjects.add(subject);
        firstAttemptGrades.add(gradeComboBox);

        newHBox.getChildren().addAll(subject, gradeComboBox, delBtn);

        firstAttemptGroup.getChildren().add(newHBox);
    }

    public void onAddSecondAttempt(){
        HBox newHBox = new HBox();
        newHBox.setSpacing(20);

        JFXTextField subject = new JFXTextField();
        HBox.setHgrow(subject, Priority.ALWAYS);
        subject.setPrefHeight(25);
        subject.setPromptText("Subject");
        subject.setLabelFloat(true);

        JFXComboBox<String> gradeComboBox = new JFXComboBox<>();
        gradeComboBox.setPrefHeight(25);
        gradeComboBox.setPromptText("Grade");
        gradeComboBox.setLabelFloat(true);
        gradeComboBox.setItems(grades);

        JFXButton delBtn = new JFXButton("Del");
        delBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE);
        icon.setFill(Color.color(1.0,0,0));
        icon.setSize("15.0");
        delBtn.setGraphic(icon);

        delBtn.setOnAction(event -> {
            Node thisDelBtn = (Node) event.getSource();
            Node theHBox = thisDelBtn.getParent();

            secondAttemptGroup.getChildren().remove(theHBox);

            ObservableList<Node> listOfHBox =  firstAttemptGroup.getChildren();

            firstAttemptSubjects.clear();
            firstAttemptGrades.clear();

            for (Node oneNode : listOfHBox){
                HBox oneHBox = (HBox) oneNode;
                firstAttemptSubjects.add((JFXTextField) oneHBox.getChildren().get(0));
                firstAttemptGrades.add((JFXComboBox<String>) oneHBox.getChildren().get(1));
            }

        });


        secondAttemptSubjects.add(subject);
        secondAttemptGrades.add(gradeComboBox);

        newHBox.getChildren().addAll(subject, gradeComboBox, delBtn);

        secondAttemptGroup.getChildren().add(newHBox);
    }

    public void onStudentSelected(){
        Student selectedPerson =  studentJFXListView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null){
            enableInterface();
            studentID = selectedPerson.getStudentID();
        }
    }

    private void enableInterface(){
        allContainer.setDisable(false);
        saveBtn.setDisable(false);
    }

    private boolean validateEducationalBackgroundInputs(){
        resetStyles();
        String color = "#FA8072";
        if (schoolName.isEmpty()){
            nameOfSchoolTextField.setStyle("-fx-border-color: " +color);
            return false;
        }else if (yearCompleted.isEmpty()){
            completedYearCombo.setStyle("-fx-border-color: " +color);
            return false;
        }else if (programme.isEmpty()){
            programmedOffered.setStyle("-fx-border-color: " +color);
            return false;
        }else return true;
    }

    private void resetStyles(){
        String color = "white";
        nameOfSchoolTextField.setStyle("-fx-border-color: " +color);
        completedYearCombo.setStyle("-fx-border-color: " +color);
        programmedOffered.setStyle("-fx-border-color: " +color);
    }

    public void getSchoolName(){
        schoolName = nameOfSchoolTextField.getText().trim();
    }

    public void getAddress(){
        schoolAddress = addressTextField.getText().trim();
    }

    public void getCompletedYear(){
        yearCompleted = completedYearCombo.getValue();
    }

    private void getProgrammeOffered(){
        programmedOffered.setOnKeyReleased(event -> programme = programmedOffered.getText().trim());

        programmedOffered.setOnAction(event -> programme = programmedOffered.getText().trim());
    }

    public void onSaveBtnClicked(){
        if (!validateEducationalBackgroundInputs() || !validateGrades() ||
                !validateSubjects() || !validateSchoolName() || !validateSubjects2() || !validateGrades2()){
            Utils.showSnackBar(snackBarPane, "Some mandatory fields are empty", 2000);
        }else {
            maskerPane.setVisible(true);
            applicationState.setUIState("disable");

            Task<Void> task = new Task<Void>(){

                @Override
                protected Void call() throws IOException {
                    createEduBackgroundInDB();

                   if (firstCheck.isSelected()){
                       int index = 0;
                       for (JFXTextField subject: firstAttemptSubjects){
                           createExamHistoryOneInDB(subject.getText().trim(), firstAttemptGrades.get(index).getValue());
                           ++index;
                       }
                   }

                   if (secondCheck.isSelected()){
                       int index = 0;
                       for (JFXTextField subject: secondAttemptSubjects){
                           createExamHistoryTwoInDB(subject.getText().trim(), secondAttemptGrades.get(index).getValue());
                           ++index;
                       }
                   }

                    Platform.runLater(()->{
                        handleError();
                    });

                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private boolean validateSubjects(){
        boolean proceed = true;
        if (firstCheck.isSelected()){
            for (JFXTextField d: firstAttemptSubjects){ // reset styles
                d.setStyle("-fx-border-color: white");
            }

            for (JFXTextField d: firstAttemptSubjects){
                if (d.getText().isEmpty()){
                    d.setStyle("-fx-border-color: #FA8072");
                    proceed = false;
                    break;
                }
            }
        }

       return proceed;
    }

    private boolean validateSubjects2(){
        boolean proceed = true;
        if (secondCheck.isSelected()){
            for (JFXTextField d: secondAttemptSubjects){ // reset styles
                d.setStyle("-fx-border-color: white");
            }

            for (JFXTextField d: secondAttemptSubjects){
                if (d.getText().isEmpty()){
                    d.setStyle("-fx-border-color: #FA8072");
                    proceed = false;
                    break;
                }
            }
        }

        return proceed;
    }

    private boolean validateGrades(){
        boolean proceed = true;
        if (firstCheck.isSelected()) {
            for (JFXComboBox<String> d : firstAttemptGrades) { // reset styles
                d.setStyle("-fx-border-color: white");
            }

            for (JFXComboBox<String> d : firstAttemptGrades) {
                String value = d.getValue();
                if (value == null || value.isEmpty()) {
                    d.setStyle("-fx-border-color: #FA8072");
                    proceed = false;
                    break;
                }
            }
        }
        return proceed;
    }

    private boolean validateGrades2(){
        boolean proceed = true;
        if (secondCheck.isSelected()) {
            for (JFXComboBox<String> d : secondAttemptGrades) { // reset styles
                d.setStyle("-fx-border-color: white");
            }

            for (JFXComboBox<String> d : secondAttemptGrades) {
                String value = d.getValue();
                if (value == null || value.isEmpty()) {
                    d.setStyle("-fx-border-color: #FA8072");
                    proceed = false;
                    break;
                }
            }
        }
        return proceed;
    }

    private void createEduBackgroundInDB()throws IOException{
        String sql = "insert into remedial_student_educational_background " +
                "(`Name_Of_School`, `School_Address`, `Completion_Year`, `Programme`, `Student_ID`) values (?,?,?,?,?)";
        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, schoolName);
            preparedStatement.setString(2, schoolAddress);
            preparedStatement.setString(3, yearCompleted);
            preparedStatement.setString(4, programme);
            preparedStatement.setString(5, studentID);


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            edu_error = true;
            Utils.printSQLException(e);
        }
    }

    private void createExamHistoryOneInDB(String subject, String grade)throws IOException{
        String sql = "insert into remedial_student_previous_exams" +
                " (`School_Name`, `Attempt_Number`, `Subject`, `Grade`, `Student_ID`) values (?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, schoolOne);
            preparedStatement.setInt(2, 1);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, grade);
            preparedStatement.setString(5, studentID);


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            history_error1 = true;
            Utils.printSQLException(e);
        }
    }

    private void createExamHistoryTwoInDB(String subject, String grade)throws IOException{
        String sql = "insert into remedial_student_previous_exams" +
                " (`School_Name`, `Attempt_Number`, `Subject`, `Grade`, `Student_ID`) values (?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, schoolTwo);
            preparedStatement.setInt(2, 2);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, grade);
            preparedStatement.setString(5, studentID);


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            history_error2 = true;
            Utils.printSQLException(e);
        }
    }

    public void onAddOneCheck(){
        if (firstCheck.isSelected()){
            school1TextField.setDisable(false);
            addBtn1.setDisable(false);
            firstAttemptGroup.setDisable(false);
        }else {
            school1TextField.setDisable(true);
            addBtn1.setDisable(true);
            firstAttemptGroup.setDisable(true);
        }
    }

    public void onAddTwoCheck(){
        if (secondCheck.isSelected()){
            school2TextField.setDisable(false);
            addBtn2.setDisable(false);
            secondAttemptGroup.setDisable(false);
        }else {
            school2TextField.setDisable(true);
            addBtn2.setDisable(true);
            secondAttemptGroup.setDisable(true);
        }
    }

    public void getSchoolOne(){
        schoolOne = school1TextField.getText().trim();
    }

    public void getSchoolTwo(){
        schoolTwo = school2TextField.getText().trim();
    }

    private boolean validateSchoolName(){
        String color = "white";
        school2TextField.setStyle("-fx-border-color: " +color);
        school1TextField.setStyle("-fx-border-color: " +color);
        color = "#FA8072";
        if (firstCheck.isSelected() && schoolOne.isEmpty()){
            school1TextField.setStyle("-fx-border-color: " +color);
            return false;
        }else if (secondCheck.isSelected() && schoolTwo.isEmpty()){
            school2TextField.setStyle("-fx-border-color: " +color);
            return false;
        }else return true;
    }

    private void handleError(){
        maskerPane.setVisible(false);
        applicationState.setUIState("enable");

        if (edu_error){
                int res = Utils.generateErrorDialog();

                if (res == Utils.YES){

                    maskerPane.setVisible(true);
                    applicationState.setUIState("disable");

                    Task<Void> task = new Task<Void>(){

                        @Override
                        protected Void call() throws IOException {
                            createEduBackgroundInDB();

                            Platform.runLater(()-> handleError());
                            return null;
                        }
                    };
                    new Thread(task).start();
                }


        }
        if (history_error1){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {
                        int index = 0;
                        for (JFXTextField subject: firstAttemptSubjects){
                            createExamHistoryOneInDB(subject.getText().trim(), firstAttemptGrades.get(index).getValue());
                            ++index;
                        }

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }

        }

        if (history_error2){
            int res = Utils.generateErrorDialog();

            if (res == Utils.YES){

                maskerPane.setVisible(true);
                applicationState.setUIState("disable");

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws IOException {
                        int index = 0;
                        for (JFXTextField subject: secondAttemptSubjects){
                            createExamHistoryTwoInDB(subject.getText().trim(), secondAttemptGrades.get(index).getValue());
                            ++index;
                        }

                        Platform.runLater(()-> handleError());
                        return null;
                    }
                };
                new Thread(task).start();
            }

        }

        if (!edu_error && !history_error2 && !history_error1){
            Utils.showSnackBar(snackBarPane, "Saved Successfully", 2000);
        }
    }


    private static class Student{
        StringProperty studentID = new SimpleStringProperty();
        StringProperty studentName = new SimpleStringProperty();

        Student(String id, String name){
            this.studentID.set(id);
            this.studentName.set(name);
        }

        public String getStudentID(){
           return this.studentID.get();
        }

        public String getStudentName(){
            return this.studentName.get();
        }

    }
}
