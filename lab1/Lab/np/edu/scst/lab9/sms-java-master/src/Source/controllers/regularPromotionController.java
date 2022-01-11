package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class regularPromotionController implements Initializable {

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
    private JFXComboBox<Classrooms> classroomsJFXComboBox;
    @FXML
    private JFXRadioButton moveBtn;
    @FXML
    private JFXRadioButton graduateBtn;


    private MaskerPane maskerPane = new MaskerPane();

    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList2 = FXCollections.observableArrayList();
    private ObservableList<Classrooms> classroomsList = FXCollections.observableArrayList();

    private ObservableList<Students> tempList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maskerPane.setVisible(false);
        stackPane.getChildren().add(maskerPane);

        sourceListView.setItems(studentsList);
        targetListView.setItems(studentsList2);

        classroomsJFXListView.setItems(classroomsList);
        setStudentsListSelectionView();
        setClassroomsJFXListView();
        setClassroomsJFXComboBoxFactory();

        allBackwardBtn.setDisable(true);
        oneBackwardBtn.setDisable(true);

        classroomsJFXListView.setDisable(true);

        onSourceDoubleClicked();
        onTargetDoubleClicked();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {

                getRegularClassrooms();

                Platform.runLater(()-> {

                });
                return null;
            }
        };
        new Thread(task).start();
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

    public void onGoBack(){
        applicationState.setUIState("classroom_management");
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

        studentsList2.clear();
        studentsList2.addAll(studentsList);
        studentsList.clear();

        oneForwardBtn.setDisable(true);
        allForwardBtn.setDisable(true);
    }

    public void onAllBackwardClicked(){
        resetStyles();
        enableAllBtn();

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

    private void resetStyles(){
        String color = "lightgray";
        sourceListView.setStyle("-fx-border-color: "+color);
        targetListView.setStyle("-fx-border-color: "+color);
        classroomsJFXListView.setStyle("-fx-border-color: "+color);
    }

    private void refreshPage(){
        studentsList2.clear();
        classroomsJFXListView.getSelectionModel().clearSelection();
        allBackwardBtn.setDisable(true);
        oneBackwardBtn.setDisable(true);
    }

    private void setClassroomsJFXComboBoxFactory(){
        Callback<ListView<Classrooms>, ListCell<Classrooms>> cellFactory =
                new Callback<ListView<Classrooms>, ListCell<Classrooms>>() {

                    @Override
                    public ListCell<Classrooms> call(ListView<Classrooms> l) {
                        return new ListCell<Classrooms>() {

                            @Override
                            protected void updateItem(Classrooms item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getClassroomName());
                                }
                            }
                        } ;
                    }
                };

        classroomsJFXComboBox.setConverter(new StringConverter<Classrooms>() {
            @Override
            public String toString(Classrooms class_) {
                if (class_ == null){
                    return null;
                } else {
                    return class_.getClassroomName();
                }
            }

            @Override
            public Classrooms fromString(String _prog_) {
                return null;
            }
        });

        classroomsJFXComboBox.setCellFactory(cellFactory);
        classroomsJFXComboBox.setItems(classroomsList);
    }

    private void getRegularClassrooms()throws IOException {
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

    public void onComboBoxClassSelected()throws IOException{
        Classrooms classroom = classroomsJFXComboBox.getValue();
        if (classroom != null){

            studentsList.clear();

            Task<Void> task = new Task<Void>(){

                @Override
                protected Void call() throws SQLException, IOException {
                    getStudentsFromSelectedClass(classroom.getClassroomID());

                    Platform.runLater(()-> {
                        if (tempList.size() == 0){
                            Utils.showSnackBar(snackBarPane, "No student found in this class", 2000);
                        }else {
                            studentsList.clear();
                            studentsList.addAll(tempList);
                        }
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }


    }

    private void getStudentsFromSelectedClass(int id)throws IOException{
        String sql = "select students.Student_ID, Surname, Other_Names from student_classroom " +
                "join students " +
                "on students.Student_ID = student_classroom.Student_ID " +
                "where Classroom_ID = ? and student_classroom.Status = 'active' and students.Status = 'active' order by Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,  id);
            ResultSet rs = preparedStatement.executeQuery();

            tempList.clear();
            while (rs.next()) {
                String s_id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");

                tempList.add(new Students(s_id, s_name + " " + o_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onMoveAction(){
        if (moveBtn.isSelected()){
            classroomsJFXListView.setDisable(false);
        }else {
            classroomsJFXListView.setDisable(true);
            classroomsJFXListView.getSelectionModel().clearSelection();
        }
    }

    public void onGraduateAction(){
        if (graduateBtn.isSelected()){
            classroomsJFXListView.setDisable(true);
            classroomsJFXListView.getSelectionModel().clearSelection();
        }else classroomsJFXListView.setDisable(false);
    }

    public void onSave(){
        if (graduateBtn.isSelected()){
            if (studentsList2.size() == 0){
                String color = "#FA8072";
                sourceListView.setStyle("-fx-border-color: "+color);
                targetListView.setStyle("-fx-border-color: "+color);
                Utils.showSnackBar(snackBarPane, "Select at least a student",2000);
            }else {
                    maskerPane.setVisible(true);
                    applicationState.setUIState("disable");
                    saveBtn.setDisable(true);

                    Task<Void> task = new Task<Void>(){

                        @Override
                        protected Void call() throws SQLException, IOException {

                            for (Students student : studentsList2) {
                                graduateStudents(student.getStudentID());
                            }

                            Platform.runLater(()-> {
                                refreshPage();
                                maskerPane.setVisible(false);
                                saveBtn.setDisable(false);
                                applicationState.setUIState("enable");
                                Utils.showSnackBar(snackBarPane, "Saved Successfully", 2000);
                            });
                            return null;
                        }
                    };
                    new Thread(task).start();

            }

        }else if (moveBtn.isSelected()){
            Classrooms classroom = classroomsJFXListView.getSelectionModel().getSelectedItem();
            Classrooms _class_ = classroomsJFXComboBox.getValue();

            if (classroom == null){
                String color = "#FA8072";
                classroomsJFXListView.setStyle("-fx-border-color: "+color);
                Utils.showSnackBar(snackBarPane, "Select a classroom to move students",2000);
            }else if (studentsList2.size() == 0){
                String color = "#FA8072";
                sourceListView.setStyle("-fx-border-color: "+color);
                targetListView.setStyle("-fx-border-color: "+color);
                Utils.showSnackBar(snackBarPane, "Select at least a student",2000);
            }else if (classroom.getClassroomName().equals(_class_.getClassroomName())){
                Utils.showSnackBar(snackBarPane, "The students are already in this class",2000);
            }
            else {
                maskerPane.setVisible(true);
                applicationState.setUIState("disable");
                saveBtn.setDisable(true);

                Task<Void> task = new Task<Void>(){

                    @Override
                    protected Void call() throws SQLException, IOException {

                        for (Students student : studentsList2) {
                            moveStudentsToAnotherClassroom(classroom.getClassroomID(), student.getStudentID());
                        }

                        Platform.runLater(()-> {
                            refreshPage();
                            maskerPane.setVisible(false);
                            applicationState.setUIState("enable");
                            saveBtn.setDisable(false);
                            Utils.showSnackBar(snackBarPane, "Saved Successfully", 2000);
                        });
                        return null;
                    }
                };
                new Thread(task).start();
            }
        }
    }

    private void graduateStudents(String id)throws IOException{
        String sql = "update student_classroom set Status = 'graduated' where Student_ID = ? ";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void moveStudentsToAnotherClassroom(int c_id, String s_id)throws IOException{
        String sql = "update student_classroom set Classroom_ID = ? where Student_ID = ? ";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, c_id);
            preparedStatement.setString(2, s_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
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
