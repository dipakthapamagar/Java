package Source.controllers;

import Source.sharedUtils.Utils;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.tableview2.TableColumn2;
import org.controlsfx.control.tableview2.TableView2;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class printRemedialAttendanceSheetController implements Initializable {
    @FXML
    private StackPane tableContainer;
    @FXML
    private JFXComboBox<Courses> coursesJFXComboBox;
    @FXML
    private JFXComboBox<Classrooms> classroomsJFXComboBox;
    @FXML
    private Label number_of_students;
    @FXML
    private AnchorPane printablePage;
    @FXML
    private Label printLabel;


    private TableView2<Students> attendanceTable = new TableView2<>();



    private ObservableList<Classrooms> classroomList = FXCollections.observableArrayList();
    private ObservableList<Courses> coursesList = FXCollections.observableArrayList();
    private ObservableList<Courses> tempCoursesList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Students> tempStudentsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCoursesJFXComboBoxFactory();
        setClassroomsJFXComboBoxFactory();

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
        String sql = "select * from remedial_classrooms where Status = 'active'";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Class_ID");
                String name = rs.getString("Class_Name");

                classroomList.add(new Classrooms(id, name));
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

    private void setCoursesList(){
        coursesList.clear();
        coursesList.addAll(tempCoursesList);
    }

    public void onCourseSelected()throws IOException{
        Courses course = coursesJFXComboBox.getValue();

        if (course != null){
            fetchStudentsForSelectedCourse(course.getCourseType().get(), course.getCourseID().get());
            setStudentsList();
            drawTable();
            number_of_students.setText(String.valueOf( studentsList.size()));
        }
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
    }

    private void drawTable(){

        attendanceTable.getItems().clear();
        attendanceTable.getColumns().clear();
        tableContainer.getChildren().clear();

        TableColumn2<Students, String> nameColumn = new TableColumn2<>("Student Name");
        TableColumn2<Students, String> mondayColumn = new TableColumn2<>("Monday");
        TableColumn2<Students, String> tuesdayColumn = new TableColumn2<>("Tuesday");
        TableColumn2<Students, String> wednesdayColumn = new TableColumn2<>("Wednesday");
        TableColumn2<Students, String> thursdayColumn = new TableColumn2<>("Thursday");
        TableColumn2<Students, String> fridayColumn = new TableColumn2<>("Friday");
        TableColumn2<Students, String> saturdayColumn = new TableColumn2<>("Saturday");
        TableColumn2<Students, String> sundayColumn = new TableColumn2<>("Sunday");
        TableColumn2<Students, String> totalAbsent = new TableColumn2<>("Total Absent");

        TableColumn2<Students, String> dateCol = new TableColumn2<>();
        dateCol.getColumns().add(mondayColumn);

        TableColumn2<Students, String> dateCol2 = new TableColumn2<>();
        dateCol2.getColumns().add(tuesdayColumn);

        TableColumn2<Students, String> dateCol3 = new TableColumn2<>();
        dateCol3.getColumns().add(wednesdayColumn);

        TableColumn2<Students, String> dateCol4 = new TableColumn2<>();
        dateCol4.getColumns().add(thursdayColumn);

        TableColumn2<Students, String> dateCol5 = new TableColumn2<>();
        dateCol5.getColumns().add(fridayColumn);

        TableColumn2<Students, String> dateCol6 = new TableColumn2<>();
        dateCol6.getColumns().add(saturdayColumn);

        TableColumn2<Students, String> dateCol7 = new TableColumn2<>();
        dateCol7.getColumns().add(sundayColumn);

        nameColumn.setCellValueFactory(param -> param.getValue().getStudentName());
        nameColumn.setSortable(false);
        nameColumn.setPrefWidth(200);

        double size = 80.0;
        mondayColumn.setPrefWidth(size);
        tuesdayColumn.setPrefWidth(size);
        wednesdayColumn.setPrefWidth(size);
        thursdayColumn.setPrefWidth(size);
        fridayColumn.setPrefWidth(size);
        saturdayColumn.setPrefWidth(size);
        sundayColumn.setPrefWidth(size);
        totalAbsent.setPrefWidth(size);
        attendanceTable.setMaxWidth(840.0);




        attendanceTable.getColumns().addAll(nameColumn, dateCol, dateCol2,
                dateCol3, dateCol4, dateCol5, dateCol6, dateCol7, totalAbsent);
        attendanceTable.getItems().addAll(studentsList);
//        attendanceTable.setScaleX(1.2);
        attendanceTable.setScaleY(0.85);
//        attendanceTable.setTranslateX(0);
        attendanceTable.setTranslateY(-55);
//        attendanceTable.setMaxHeight(400);

        tableContainer.getChildren().add(attendanceTable);
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
        classroomsJFXComboBox.setItems(classroomList);
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
    }

    public void onPrintTable(ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout =
                printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, 5, 5, 1, 5);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        job.getJobSettings().setPageLayout(pageLayout);

        if (job != null) {
            boolean showDialog = job.showPageSetupDialog(window);


            if (showDialog) {

                int page_number = 1;

                    ObservableList<Students> printList = FXCollections.observableArrayList();

                    printList.addAll(studentsList);

                    while (printList.size() > 0){


                        ObservableList<Students> partList = FXCollections.observableArrayList();

                        int count = 0;
                        for (Students student : printList){
                            partList.add(student);
                            ++count;
                            if (page_number == 1){
                                if (count > 20) break;
                            }else {
                                if (count > 24) break;
                            }

                        }

                        if (page_number > 1){

                            createAnotherTable(partList, job);
                        }else {
                            attendanceTable.getItems().clear();
                            attendanceTable.getItems().addAll(partList);

                            showPrintIndicator("Printing...");
                            boolean success = job.printPage(printablePage);

                            if (!success){
                                job.endJob();
                            }
                        }

                        printList.removeAll(partList);
                        ++page_number;
                    }

                job.endJob();
                showPrintIndicator("Done");

                clearPrintLabel(); // executed after 5s

                    attendanceTable.getItems().clear();
                    attendanceTable.getItems().addAll(studentsList);
            }
        }
    }

    private void createAnotherTable(ObservableList<Students> list, PrinterJob job){
        TableView2<Students> newTable = new TableView2<>();

        TableColumn2<Students, String> nameColumn = new TableColumn2<>("Student Name");
        TableColumn2<Students, String> mondayColumn = new TableColumn2<>("Monday");
        TableColumn2<Students, String> tuesdayColumn = new TableColumn2<>("Tuesday");
        TableColumn2<Students, String> wednesdayColumn = new TableColumn2<>("Wednesday");
        TableColumn2<Students, String> thursdayColumn = new TableColumn2<>("Thursday");
        TableColumn2<Students, String> fridayColumn = new TableColumn2<>("Friday");
        TableColumn2<Students, String> saturdayColumn = new TableColumn2<>("Saturday");
        TableColumn2<Students, String> sundayColumn = new TableColumn2<>("Sunday");
        TableColumn2<Students, String> totalAbsent = new TableColumn2<>("Total Absent");

        TableColumn2<Students, String> dateCol = new TableColumn2<>();
        dateCol.getColumns().add(mondayColumn);

        TableColumn2<Students, String> dateCol2 = new TableColumn2<>();
        dateCol2.getColumns().add(tuesdayColumn);

        TableColumn2<Students, String> dateCol3 = new TableColumn2<>();
        dateCol3.getColumns().add(wednesdayColumn);

        TableColumn2<Students, String> dateCol4 = new TableColumn2<>();
        dateCol4.getColumns().add(thursdayColumn);

        TableColumn2<Students, String> dateCol5 = new TableColumn2<>();
        dateCol5.getColumns().add(fridayColumn);

        TableColumn2<Students, String> dateCol6 = new TableColumn2<>();
        dateCol6.getColumns().add(saturdayColumn);

        TableColumn2<Students, String> dateCol7 = new TableColumn2<>();
        dateCol7.getColumns().add(sundayColumn);

        nameColumn.setCellValueFactory(param -> param.getValue().getStudentName());
        nameColumn.setSortable(false);
        nameColumn.setPrefWidth(200);

        double size = 80.0;
        mondayColumn.setPrefWidth(size);
        tuesdayColumn.setPrefWidth(size);
        wednesdayColumn.setPrefWidth(size);
        thursdayColumn.setPrefWidth(size);
        fridayColumn.setPrefWidth(size);
        saturdayColumn.setPrefWidth(size);
        sundayColumn.setPrefWidth(size);
        totalAbsent.setPrefWidth(size);
        newTable.setMaxWidth(740.0);
        newTable.setMinHeight(650);

        newTable.setScaleY(0.85);
        newTable.setTranslateY(-30);

        newTable.getColumns().addAll(nameColumn, dateCol, dateCol2,
                dateCol3, dateCol4, dateCol5, dateCol6, dateCol7, totalAbsent);
        newTable.getItems().addAll(list);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: white");

        AnchorPane.setLeftAnchor(newTable, 48.0);
        AnchorPane.setRightAnchor(newTable, 500.0);

        newTable.getStylesheets().add("Source/styles/printOutStyle.css");


        anchorPane.getChildren().add(newTable);

        showPrintIndicator("Printing...");
        boolean success = job.printPage(anchorPane);


        if (!success){
            job.endJob();
        }
    }

    private void showPrintIndicator(String text){
        printLabel.setText(text);
        printLabel.setVisible(true);
    }

    private void hidePrintIndicator(){
        printLabel.setVisible(false);
    }

    private void clearPrintLabel(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                hidePrintIndicator();
            }
        }, 5000);
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

    private static class Classrooms{
        IntegerProperty classID = new SimpleIntegerProperty();
        StringProperty className = new SimpleStringProperty();

        Classrooms(int id, String name){
            this.classID.set(id);
            this.className.set(name);
        }

        IntegerProperty getClassID(){
            return this.classID;
        }

        StringProperty getClassName(){
            return this.className;
        }
    }

    private static class Students{
        StringProperty studentID = new SimpleStringProperty();
        StringProperty studentName = new SimpleStringProperty();

        Students(String id, String name){
            this.studentID.set(id);
            this.studentName.set(name);
        }

        public StringProperty getStudentID(){
            return this.studentID;
        }

        public StringProperty getStudentName(){
            return this.studentName;
        }
    }
}
