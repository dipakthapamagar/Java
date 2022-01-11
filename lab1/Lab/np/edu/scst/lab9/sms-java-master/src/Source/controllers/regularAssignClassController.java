package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
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
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class regularAssignClassController implements Initializable {


    @FXML
    private JFXListView<Classrooms> classroomsJFXListView;
    @FXML
    private JFXListView<Students> sourceListView;
    @FXML
    private JFXListView<Students> targetListView;
    @FXML
    private Button oneForwardBtn;
    @FXML
    private Button allForwardBtn;
    @FXML
    private Button oneBackwardBtn;
    @FXML
    private Button allBackwardBtn;
    @FXML
    private Pane snackBarPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private Label label;


    private MaskerPane maskerPane = new MaskerPane();



    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList2 = FXCollections.observableArrayList();
    private ObservableList<Classrooms> classroomsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        maskerPane.setVisible(false);
        stackPane.getChildren().add(maskerPane);

        setClassroomsJFXListView();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {

                if (applicationState.isRegular){
                    label.setText("Regular /");
                    getRegularStudents();
                    getRegularClassrooms();
                }else {
                    label.setText("Remedial /");
                    getRemedialStudents();
                    getRemedialClassrooms();
                }

                Platform.runLater(()-> {

                });
                return null;
            }
        };
        new Thread(task).start();

        sourceListView.setItems(studentsList);
        targetListView.setItems(studentsList2);

        classroomsJFXListView.setItems(classroomsList);
        setStudentsListSelectionView();


        allBackwardBtn.setDisable(true);
        oneBackwardBtn.setDisable(true);

        onSourceDoubleClicked();
        onTargetDoubleClicked();
    }

    private void setStudentsListSelectionView(){
        sourceListView.setCellFactory(new Callback<ListView<Students>, ListCell<Students>>() {
            @Override
            public ListCell<Students> call(ListView<Students> param) {
                ListCell<Students> cell = new ListCell<Students>(){
                    @Override
                    protected void updateItem(Students item, boolean empty){
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

        targetListView.setCellFactory(new Callback<ListView<Students>, ListCell<Students>>() {
            @Override
            public ListCell<Students> call(ListView<Students> param) {
                ListCell<Students> cell = new ListCell<Students>(){
                    @Override
                    protected void updateItem(Students item, boolean empty){
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

        sourceListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setClassroomsJFXListView(){
        classroomsJFXListView.setCellFactory(new Callback<ListView<Classrooms>, ListCell<Classrooms>>() {
            @Override
            public ListCell<Classrooms> call(ListView<Classrooms> param) {
                ListCell<Classrooms> cell = new ListCell<Classrooms>(){
                    @Override
                    protected void updateItem(Classrooms item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getClassroomName());
                        }else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void getRegularStudents()throws IOException {
        String sql = "select  students.Student_ID, students.Surname, students.Other_Names from regular_students_without_class " +
                "join students " +
                "on regular_students_without_class.Student_ID = students.Student_ID where students.Status = 'active' " +
                " order by students.Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");
                studentsList.add(new Students(id, (s_name + " " + o_name)));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getRemedialStudents()throws IOException{
        String sql = "select  students.Student_ID, students.Surname, students.Other_Names from remedial_students_without_class " +
                "join students " +
                "on remedial_students_without_class.Student_ID = students.Student_ID where students.Status = 'active'" +
                " order by students.Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");
                studentsList.add(new Students(id, (s_name + " " + o_name)));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getRegularClassrooms()throws IOException{
        String sql = "select Classroom_ID, Classroom_Name from classrooms";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Classroom_ID");
                String c_name = rs.getString("Classroom_Name");

                classroomsList.add(new Classrooms(id, c_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getRemedialClassrooms()throws IOException{
        String sql = "select Class_ID, Class_Name from remedial_classrooms where Status = 'active'";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Class_ID");
                String c_name = rs.getString("Class_Name");

                classroomsList.add(new Classrooms(id, c_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onGoBack(){
        applicationState.setUIState("classroom_management");
    }

    public void onSave(){
        String color = "#FA8072";
        resetStyles();

        // validate first
        if (studentsList2.size() == 0){
            Utils.showSnackBar(snackBarPane, "Please select a student", 2000);
            sourceListView.setStyle("-fx-border-color: "+color);
            targetListView.setStyle("-fx-border-color: "+color);
            return;
        }
        Classrooms selectedClass = classroomsJFXListView.getSelectionModel().getSelectedItem();
        if (selectedClass == null){
            Utils.showSnackBar(snackBarPane, "Please select a class", 2000);
            classroomsJFXListView.setStyle("-fx-border-color: "+color);
            return;
        }

        maskerPane.setVisible(true);
        saveBtn.setDisable(true);
        applicationState.setUIState("disable");

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws IOException {

                for(Students student : studentsList2){
                    String student_id = student.getStudentID();
                    int class_id = selectedClass.getClassroomID();

                    removeStudentFromFreeRange(student_id);
                    assignStudentsClassroom(student_id, class_id);
                }

                Platform.runLater(()-> {
                    maskerPane.setVisible(false);
                    Utils.showSnackBar(snackBarPane, "Saved Successfully", 2000);
                    refreshPage();
                    saveBtn.setDisable(false);
                    applicationState.setUIState("enable");
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void assignStudentsClassroom(String s_id, int c_id)throws IOException{
        String sql;
        if (applicationState.isRegular){
             sql = "insert into student_classroom (`Student_ID`, `Classroom_ID`, `Status`) values (?,?,?)";
        }else {
            sql = "insert into remedial_students_classrooms (`Student_ID`, `Class_ID`, `Status`) values (?,?,?)";
        }


        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id);
            preparedStatement.setInt(2, c_id);
            preparedStatement.setString(3, "active");


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void removeStudentFromFreeRange(String id)throws IOException{
        String sql;
        if (applicationState.isRegular){
            sql = "delete from regular_students_without_class where Student_ID = ?";
        }else {
            sql = "delete from remedial_students_without_class where Student_ID = ?";
        }

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void onSourceDoubleClicked(){
        sourceListView.setOnMouseClicked(click -> {
            resetStyles();
            if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {

                enableAllBtn();

                Students selectedItem =  sourceListView.getSelectionModel().getSelectedItem();

                if (selectedItem != null){
                    studentsList2.addAll(selectedItem);
                    studentsList.removeAll(selectedItem);
                }


                if (studentsList.size() == 0){
                    oneForwardBtn.setDisable(true);
                    allForwardBtn.setDisable(true);
                }else {
                    oneForwardBtn.setDisable(false);
                    allForwardBtn.setDisable(false);
                }

            }
        });
    }

    private void onTargetDoubleClicked(){

        targetListView.setOnMouseClicked(click -> {
            resetStyles();
            if (click.getButton() == MouseButton.PRIMARY && click.getClickCount() == 2) {

                enableAllBtn();

                Students selectedItem =  targetListView.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    studentsList.addAll(selectedItem);
                    studentsList2.removeAll(selectedItem);
                }


                if (studentsList2.size() == 0){
                    oneBackwardBtn.setDisable(true);
                    allBackwardBtn.setDisable(true);
                }else {
                    oneBackwardBtn.setDisable(false);
                    allBackwardBtn.setDisable(false);
                }
            }
        });
    }

    public void onOneForwardClicked(){
        resetStyles();
        ObservableList<Students> selectedItems =  sourceListView.getSelectionModel().getSelectedItems();

        if (selectedItems != null){
            if (selectedItems.size() > 0){
                enableAllBtn();

                studentsList2.addAll(selectedItems);
                studentsList.removeAll(selectedItems);


                if (studentsList.size() == 0){
                    oneForwardBtn.setDisable(true);
                    allForwardBtn.setDisable(true);
                }else {
                    oneForwardBtn.setDisable(false);
                    allForwardBtn.setDisable(false);
                }
            }
        }

    }

    public void onOneBackwardClicked(){
        resetStyles();
        ObservableList<Students> selectedItems =  targetListView.getSelectionModel().getSelectedItems();

        if (selectedItems != null) {
            if (selectedItems.size() > 0) {
                enableAllBtn();

                studentsList.addAll(selectedItems);
                studentsList2.removeAll(selectedItems);


                if (studentsList2.size() == 0){
                    oneBackwardBtn.setDisable(true);
                    allBackwardBtn.setDisable(true);
                }else {
                    oneBackwardBtn.setDisable(false);
                    allBackwardBtn.setDisable(false);
                }
            }
        }
    }

    public void onAllForwardClicked(){
        resetStyles();
        enableAllBtn();

//        studentsList2.clear();
        studentsList2.addAll(studentsList);
        studentsList.clear();

        oneForwardBtn.setDisable(true);
        allForwardBtn.setDisable(true);
    }

    public void onAllBackwardClicked(){
        resetStyles();
        enableAllBtn();

//        studentsList.clear();
        studentsList.addAll(studentsList2);
        studentsList2.clear();


            oneBackwardBtn.setDisable(true);
            allBackwardBtn.setDisable(true);

    }

    private void enableAllBtn(){
        oneForwardBtn.setDisable(false);
        allForwardBtn.setDisable(false);
        oneBackwardBtn.setDisable(false);
        allBackwardBtn.setDisable(false);
    }

    private void refreshPage(){
        studentsList2.clear();
        classroomsJFXListView.getSelectionModel().clearSelection();
        allBackwardBtn.setDisable(true);
        oneBackwardBtn.setDisable(true);
    }

    private void resetStyles(){
        String color = "lightgray";
        sourceListView.setStyle("-fx-border-color: "+color);
        targetListView.setStyle("-fx-border-color: "+color);
        classroomsJFXListView.setStyle("-fx-border-color: "+color);
    }

   private static class Students{
        private StringProperty studentID = new SimpleStringProperty();
        private StringProperty studentName = new SimpleStringProperty();

        Students(String id, String name){
            this.studentID.set(id);
            this.studentName.set(name);
        }

        String getStudentID(){
            return this.studentID.get();
        }

        String getStudentName(){
            return this.studentName.get();
        }
    }

    private static class Classrooms{
        private IntegerProperty classroomID = new SimpleIntegerProperty();
        private StringProperty classroomName = new SimpleStringProperty();

        Classrooms(int id, String name){
            this.classroomID.set(id);
            this.classroomName.set(name);
        }

        int getClassroomID(){
            return this.classroomID.get();
        }

        String getClassroomName(){
            return this.classroomName.get();
        }
    }
}
