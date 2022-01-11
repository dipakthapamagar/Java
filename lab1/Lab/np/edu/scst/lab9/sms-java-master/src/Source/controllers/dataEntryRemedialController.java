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

public class dataEntryRemedialController implements Initializable {

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
    private JFXComboBox<Classrooms> classroomsJFXComboBox;
    @FXML
    private JFXComboBox<Courses> coursesJFXComboBox;
    @FXML
    private JFXDatePicker attendanceDate;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<Classrooms> classroomsJFXComboBox2;
    @FXML
    private JFXComboBox<Courses> coursesJFXComboBox2;
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
    private ObservableList<Classrooms> classroomsList = FXCollections.observableArrayList();
    private ObservableList<Courses> coursesList = FXCollections.observableArrayList();
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
        setCoursesJFXComboBoxFactory();
        setClassroomsJFXComboBoxFactory();
        setStudentsJFXListViewFactory();

        sourceListView.setItems(studentsList);
        targetListView.setItems(studentsList2);

        Utils.convertDate(attendanceDate);
        Utils.convertDate(pickDate);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                getRemedialClassrooms();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void getRemedialClassrooms()throws IOException {
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

    private void fetchCoreCoursesForThisClassroom(int c_id)throws IOException{
        String sql = "select Remedial_Course_ID, Course_Name from remedial_core_courses where Class_ID = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, c_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Remedial_Course_ID");
                String name = rs.getString("Course_Name");

                tempCoursesList.add(new Courses(id, name, "core"));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void fetchElectiveCourses()throws IOException{
        String sql = "select Elective_Course_ID, Course_Name from remedial_elective_courses; ";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Elective_Course_ID");
                String name = rs.getString("Course_Name");

                tempCoursesList.add(new Courses(id, name, "elective"));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onClassSelected(){
        Classrooms classroom = classroomsJFXComboBox.getValue();

        if (classroom != null){
            tempCoursesList.clear();


            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    fetchCoreCoursesForThisClassroom(classroom.getClassID().get());
                    fetchElectiveCourses();

                    Platform.runLater(()->{
                        setCoursesList();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    public void onClassSelected2(){
        Classrooms classroom = classroomsJFXComboBox2.getValue();

        if (classroom != null){
            tempCoursesList.clear();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    fetchCoreCoursesForThisClassroom(classroom.getClassID().get());
                    fetchElectiveCourses();

                    Platform.runLater(()->{
                        setCoursesList();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void setCoursesList(){
        coursesList.clear();
        coursesList.addAll(tempCoursesList);
    }

    private void fetchStudentsForSelectedCourse(String type, int id)throws IOException{
        String sql = "select students.Surname, students.Other_Names, students.Student_ID from remedial_student_courses " +
                "join students " +
                "on students.Student_ID = remedial_student_courses.Student_ID " +
                "where students.Status = 'active' and remedial_student_courses.Type = ? " +
                "and remedial_student_courses.Course_ID = ? order by students.Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, type );
            preparedStatement.setInt(2,  id);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");
                String s_id = rs.getString("Student_ID");

                tempStudentsList.add(new Students(s_id, s_name + " " + o_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setStudentsList(){
        studentsList.clear();
        studentsList.addAll(tempStudentsList);
        tempStudentsList.clear();
        stackPane.setDisable(false);
    }

    public void onCourseSelected(){
        Courses course = coursesJFXComboBox.getValue();

        if (course != null){


            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    fetchStudentsForSelectedCourse(course.getCourseType().get(), course.getCourseID().get());

                    Platform.runLater(()->setStudentsList());
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    public void onCourseSelected2(){
        Courses course = coursesJFXComboBox2.getValue();

        pickDate.getEditor().clear();
        pickDate.setDisable(true);
        summaryBox.setVisible(false);

        if (course != null){


            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    fetchStudentsForSelectedCourse(course.getCourseType().get(), course.getCourseID().get());

                    Platform.runLater(()->setStudentsList2());
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void setStudentsList2(){
        studentsList_update.clear();
        studentsList_update.addAll(tempStudentsList);
        tempStudentsList.clear();
        box.setDisable(false);
    }

    private void markAttendance(String s_id, int c_id, String type, String date, String attend)throws IOException{
        String sql = "insert into remedial_attendance (`Student_ID`, `Course_ID`, `Type`, `Date`, `Attend`)" +
                " values(?,?,?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id);
            preparedStatement.setInt(2, c_id);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, date);
            preparedStatement.setString(5, attend);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            network_error = true;
            Utils.printSQLException(e);
        }
    }

    public void onSave(){
        if (validateDate() && validateSelectedList()){
            Courses course = coursesJFXComboBox.getValue();
            String type = course.getCourseType().get();
            int course_id = course.getCourseID().get();
            String[] d = attendanceDate.getEditor().getText().split("/");
            String date = d[2] + "-" + d[1] + "-" + d[0];

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // mark absent
                    for (Students student : studentsList2){
                        markAttendance(student.getStudentID(), course_id, type, date, "Absent");
                    }

                    if (!network_error){
                        // mark present
                        for (Students student : studentsList){
                            markAttendance(student.getStudentID(), course_id, type, date, "Present");
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
                                    setText(item.getClassName().get());
                                }
                            }
                        };
                    }
                };

        classroomsJFXComboBox.setConverter(new StringConverter<Classrooms>()

        {
            @Override
            public String toString (Classrooms _class){
                if (_class == null) {
                    return null;
                } else {
                    return _class.getClassName().get();
                }
            }

            @Override
            public Classrooms fromString (String _class_){
                return null;
            }
        });
        classroomsJFXComboBox.setCellFactory(cellFactory);
        classroomsJFXComboBox.setItems(classroomsList);

        //////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        Callback<ListView<Classrooms>, ListCell<Classrooms>> cellFactory2 =
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
                                    setText(item.getClassName().get());
                                }
                            }
                        };
                    }
                };

        classroomsJFXComboBox2.setConverter(new StringConverter<Classrooms>()

        {
            @Override
            public String toString (Classrooms _class){
                if (_class == null) {
                    return null;
                } else {
                    return _class.getClassName().get();
                }
            }

            @Override
            public Classrooms fromString (String _class_){
                return null;
            }
        });
        classroomsJFXComboBox2.setCellFactory(cellFactory2);
        classroomsJFXComboBox2.setItems(classroomsList);
    }

    private void setCoursesJFXComboBoxFactory(){
        Callback<ListView<Courses>, ListCell<Courses>> cellFactory =
                new Callback<ListView<Courses>, ListCell<Courses>>() {

                    @Override
                    public ListCell<Courses> call(ListView<Courses> l) {
                        return new ListCell<Courses>() {

                            @Override
                            protected void updateItem(Courses item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getCourseName().get());
                                }
                            }
                        };
                    }
                };

        coursesJFXComboBox.setConverter(new StringConverter<Courses>()

        {
            @Override
            public String toString (Courses course){
                if (course == null) {
                    return null;
                } else {
                    return course.getCourseName().get();
                }
            }

            @Override
            public Courses fromString (String _class_){
                return null;
            }
        });
        coursesJFXComboBox.setCellFactory(cellFactory);
        coursesJFXComboBox.setItems(coursesList);



        //////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////
        Callback<ListView<Courses>, ListCell<Courses>> cellFactory2 =
                new Callback<ListView<Courses>, ListCell<Courses>>() {

                    @Override
                    public ListCell<Courses> call(ListView<Courses> l) {
                        return new ListCell<Courses>() {

                            @Override
                            protected void updateItem(Courses item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getCourseName().get());
                                }
                            }
                        };
                    }
                };

        coursesJFXComboBox2.setConverter(new StringConverter<Courses>()

        {
            @Override
            public String toString (Courses course){
                if (course == null) {
                    return null;
                } else {
                    return course.getCourseName().get();
                }
            }

            @Override
            public Courses fromString (String _class_){
                return null;
            }
        });
        coursesJFXComboBox2.setCellFactory(cellFactory2);
        coursesJFXComboBox2.setItems(coursesList);


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
        String sql = "select Remedial_Attendance_ID, Attend from remedial_attendance where Student_ID = ? and Date = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id );
            preparedStatement.setString(2, date );
            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                int _id = rs.getInt("Remedial_Attendance_ID");
                String name = rs.getString("Attend");

                attendance = new Attendance(_id, name);
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onSelectionChange()throws IOException{
        if(attendance != null){
            updateAttendance();
            attendLabel.setText(attendanceJFXComboBox.getValue());
            Utils.showSnackBar(snackBarPane2, "Saved", 2000);
        }

    }

    private void updateAttendance()throws IOException{
        String sql = "update remedial_attendance set Attend = ? where Remedial_Attendance_ID = ?";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, attendanceJFXComboBox.getValue());
            preparedStatement.setInt(2, attendance.getId());

            preparedStatement.executeUpdate();
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

    private static class Classrooms{
        IntegerProperty classID = new SimpleIntegerProperty();
        StringProperty className = new SimpleStringProperty();

        Classrooms(int id, String name){
            this.classID.set(id);
            this.className.set(name);
        }

        public IntegerProperty getClassID(){
            return this.classID;
        }

        StringProperty getClassName(){
            return this.className;
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
}
