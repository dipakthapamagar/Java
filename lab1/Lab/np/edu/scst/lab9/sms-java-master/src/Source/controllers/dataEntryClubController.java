package Source.controllers;

import Source.sharedUtils.Utils;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class dataEntryClubController implements Initializable {
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
    private Pane snackBarPane1;
    @FXML
    private Pane snackBarPane2;
    @FXML
    private JFXComboBox<Clubs> clubsJFXComboBox;
    @FXML
    private JFXDatePicker attendanceDate;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<Clubs> clubsJFXComboBox2;
    @FXML
    private JFXListView<Students> studentsJFXListView;
    @FXML
    private JFXDatePicker pickDate;
    @FXML
    private Label nameLabel;
    @FXML
    private Label attendLabel;
    @FXML
    private VBox summaryBox;
    @FXML
    private JFXComboBox<String> attendanceJFXComboBox;
    @FXML
    private HBox box;


    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList2 = FXCollections.observableArrayList();
    private ObservableList<Clubs> clubList = FXCollections.observableArrayList();
    private ObservableList<Courses> tempCoursesList = FXCollections.observableArrayList();
    private ObservableList<Students> tempStudentsList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList_update = FXCollections.observableArrayList();
    private ObservableList<String> attendList = FXCollections.observableArrayList("Absent", "Present");

    private boolean network_error = false;

    private Attendance attendance = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStudentsListSelectionView();
        allBackwardBtn.setDisable(true);
        oneBackwardBtn.setDisable(true);
        stackPane.setDisable(true);
        summaryBox.setVisible(false);
        box.setDisable(true);
        pickDate.setDisable(true);
        attendanceJFXComboBox.setItems(attendList);


        onSourceDoubleClicked();
        onTargetDoubleClicked();

        setClubsJFXComboBoxFactory();
        setStudentsJFXListViewFactory();

        sourceListView.setItems(studentsList);
        targetListView.setItems(studentsList2);

        Utils.convertDate(attendanceDate);
        Utils.convertDate(pickDate);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchClubs();
                return null;
            }
        };
        new Thread(task).start();
    }



    public void onClubSelected(){
        Clubs clubs = clubsJFXComboBox.getValue();

        if (clubs != null){
            tempCoursesList.clear();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    fetchStudentsInThisClub(clubs.getClubID());
                    Platform.runLater(()->{
                        setStudentsList();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    public void onClubSelected2(){
        Clubs clubs = clubsJFXComboBox2.getValue();

        if (clubs != null){
            tempCoursesList.clear();

            pickDate.setDisable(true);
            pickDate.getEditor().clear();

            attendance = null;
            attendanceJFXComboBox.setValue("");
            summaryBox.setVisible(false);


            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    fetchStudentsInThisClub2(clubs.getClubID());
                    Platform.runLater(()->{
                        setStudentsList2();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void setStudentsList(){
        studentsList.clear();
        studentsList.addAll(tempStudentsList);
        tempStudentsList.clear();
        stackPane.setDisable(false);
    }

    private void setStudentsList2(){
        studentsList_update.clear();
        studentsList_update.addAll(tempStudentsList);
        tempStudentsList.clear();
        box.setDisable(false);
    }

    private void markAttendance(String stud_id, String club_id, String date, String attend)throws IOException {
        String sql = "insert into club_attendance (`Student_ID`, `Club_ID`, `Date`, `Attend`)" +
                " values(?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, stud_id);
            preparedStatement.setString(2, club_id);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, attend);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            network_error = true;
            Utils.printSQLException(e);
        }
    }

    public void onSave(){
        if (validateDate() && validateSelectedList()){
            Clubs clubs = clubsJFXComboBox.getValue();

            String clubID = clubs.getClubID();
            String[] d = attendanceDate.getEditor().getText().split("/");
            String date = d[2] + "-" + d[1] + "-" + d[0];

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // mark absent
                    for (Students student : studentsList2){
                        markAttendance(student.getStudentID(), clubID, date, "Absent");
                    }

                    if (!network_error){
                        // mark present
                        for (Students student : studentsList){
                            markAttendance(student.getStudentID(), clubID, date, "Present");
                        }
                    }
                    Platform.runLater(()->{
                        if (network_error){
                            Utils.showSnackBar(snackBarPane1, "A network error occurred, try again", 2000);
                            network_error = false;
                        }else {
                            Utils.showSnackBar(snackBarPane1, "Saved successfully", 2000);
                            refreshTab1();
                        }
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private boolean validateDate(){
        if(attendanceDate.getEditor().getText().isEmpty()){
            Utils.showSnackBar(snackBarPane1, "Please pick a date", 2000);
            return false;
        }
        return true;
    }

    private boolean validateSelectedList(){
        if (studentsList2.size() == 0){
            int res =  Utils.confirmationDialog("Confirm",
                    "Were all students present?",
                    "All Students will be marked present");
            return res == Utils.YES;
        }else return true;
    }

    private void setClubsJFXComboBoxFactory(){
        Callback<ListView<Clubs>, ListCell<Clubs>> cellFactory =
                new Callback<ListView<Clubs>, ListCell<Clubs>>() {

                    @Override
                    public ListCell<Clubs> call(ListView<Clubs> l) {
                        return new ListCell<Clubs>() {

                            @Override
                            protected void updateItem(Clubs item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getClubName());
                                }
                            }
                        };
                    }
                };

        clubsJFXComboBox.setConverter(new StringConverter<Clubs>()

        {
            @Override
            public String toString (Clubs _class){
                if (_class == null) {
                    return null;
                } else {
                    return _class.getClubName();
                }
            }

            @Override
            public Clubs fromString (String _class_){
                return null;
            }
        });
        clubsJFXComboBox.setCellFactory(cellFactory);
        clubsJFXComboBox.setItems(clubList);

        //////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        Callback<ListView<Clubs>, ListCell<Clubs>> cellFactory2 =
                new Callback<ListView<Clubs>, ListCell<Clubs>>() {

                    @Override
                    public ListCell<Clubs> call(ListView<Clubs> l) {
                        return new ListCell<Clubs>() {

                            @Override
                            protected void updateItem(Clubs item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getClubName());
                                }
                            }
                        };
                    }
                };

        clubsJFXComboBox2.setConverter(new StringConverter<Clubs>()

        {
            @Override
            public String toString (Clubs clubs){
                if (clubs == null) {
                    return null;
                } else {
                    return clubs.getClubName();
                }
            }

            @Override
            public Clubs fromString (String _class_){
                return null;
            }
        });
        clubsJFXComboBox2.setCellFactory(cellFactory2);
        clubsJFXComboBox2.setItems(clubList);
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

    private void setStudentsJFXListViewFactory(){
        studentsJFXListView.setCellFactory(new Callback<ListView<Students>, ListCell<Students>>() {
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

        studentsJFXListView.setItems(studentsList_update);
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
    }

    private void refreshTab1(){
        attendanceDate.getEditor().clear();
//        studentsList.addAll(studentsList2);
        studentsList2.clear();
        studentsList.clear();
//        studentsList.sort(Comparator.comparing(Students::getStudentName));
        stackPane.setDisable(true);
    }

    public void onStudentSelected_update(){
        Students student = studentsJFXListView.getSelectionModel().getSelectedItem();

        if (student != null){
            pickDate.setDisable(false);
            pickDate.getEditor().clear();

            attendance = null;
            attendanceJFXComboBox.setValue("");
            summaryBox.setVisible(false);
        }
    }

    public void onDatePicked(){
        Students student = studentsJFXListView.getSelectionModel().getSelectedItem();

        String[] d = pickDate.getEditor().getText().split("/");
        String date = d[2] + "-" + d[1] + "-" + d[0];

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                getAttendance(student.getStudentID(), date);

                Platform.runLater(()-> {
                    if (attendance != null){
                        nameLabel.setText(student.getStudentName());
                        attendLabel.setText(attendance.getAttend());
                        summaryBox.setVisible(true);
                    }else {
                        Utils.showSnackBar(snackBarPane2, "No data found", 200);
                    }

                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void getAttendance(String id, String date)throws IOException{
        String sql = "select Club_Attendance_ID, Attend from club_attendance where Student_ID = ? and Date = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id );
            preparedStatement.setString(2, date );
            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                int _id = rs.getInt("Club_Attendance_ID");
                String name = rs.getString("Attend");

                attendance = new Attendance(_id, name);
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onSelectionChange() throws IOException {
        if(attendance != null){
            updateAttendance();
            attendLabel.setText(attendanceJFXComboBox.getValue());
            Utils.showSnackBar(snackBarPane2, "Saved", 2000);
        }

    }

    private void updateAttendance()throws IOException{
        String sql = "update club_attendance set Attend = ? where Club_Attendance_ID = ?";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, attendanceJFXComboBox.getValue());
            preparedStatement.setInt(2, attendance.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }

    }

    private void fetchClubs()throws IOException{
        String sql = "select * from clubs";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String name = rs.getString("Club_Name");

                clubList.add(new Clubs(id, name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void fetchStudentsInThisClub(String club_id)throws IOException{
        String sql = "select Surname, Other_Names, students.Student_ID " +
                "from students_clubs " +
                "join students " +
                "on students.Student_ID = students_clubs.Student_ID " +
                "where students_clubs.Status = 'active' and students.Status = 'active' " +
                "and Club_ID = ? order by Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");

                tempStudentsList.add(new Students(id, s_name + " " + o_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void fetchStudentsInThisClub2(String club_id)throws IOException{
        String sql = "select Surname, Other_Names, students.Student_ID " +
                "from students_clubs " +
                "join students " +
                "on students.Student_ID = students_clubs.Student_ID " +
                "where students_clubs.Status = 'active' and students.Status = 'active' " +
                "and Club_ID = ? order by Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");

                tempStudentsList.add(new Students(id, s_name + " " + o_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private static class Attendance{
        private int id;
        private String attend;

        Attendance(int id, String attend){
            this.attend = attend;
            this.id = id;
        }

        private int getId(){
            return this.id;
        }

        private String getAttend(){
            return this.attend;
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

    private static class Courses {
        IntegerProperty courseID = new SimpleIntegerProperty();
        StringProperty courseName = new SimpleStringProperty();
        StringProperty courseType = new SimpleStringProperty();

        Courses(int id, String name, String type){
            this.courseID.set(id);
            this.courseName.set(name);
            this.courseType.set(type);
        }

        IntegerProperty getCourseID(){
            return this.courseID;
        }

        StringProperty getCourseName(){
            return this.courseName;
        }

        StringProperty getCourseType(){
            return this.courseType;
        }
    }

    private static class Clubs{
        StringProperty clubID = new SimpleStringProperty();
        StringProperty clubName = new SimpleStringProperty();

        Clubs(String id, String name){
            this.clubID.set(id);
            this.clubName.set(name);
        }

        String getClubID(){
            return this.clubID.get();
        }

        String getClubName(){
            return this.clubName.get();
        }
    }
}
