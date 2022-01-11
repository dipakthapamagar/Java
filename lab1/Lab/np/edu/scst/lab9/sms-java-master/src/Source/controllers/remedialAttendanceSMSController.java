package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.sharedData;
import com.hubtel.*;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class remedialAttendanceSMSController implements Initializable {

    @FXML
    private JFXComboBox<Classrooms> classroomsJFXComboBox;
    @FXML
    private CheckListView<Students> studentsCheckListView;
    @FXML
    private JFXComboBox<String> academicYearComboBox;
    @FXML
    private JFXCheckBox selectAllCheck;
    @FXML
    private TextArea postTextArea;
    @FXML
    private JFXDatePicker fromDatePicker;
    @FXML
    private JFXDatePicker toDatePicker;
    @FXML
    private Pane snackBarPane;
    @FXML
    private JFXCheckBox sendToStudentsCheckBox;
    @FXML
    private JFXRadioButton sendToAllParentsRadioButton;
    @FXML
    private JFXRadioButton sendToOneParentsRadioButton;


    private ObservableList<Classrooms> classroomsList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Students> tempStudentList = FXCollections.observableArrayList();


    private ArrayList<StudentsCourses> studentsCoursesList = new ArrayList<>();
    private ArrayList<String> attendanceList = new ArrayList<>();
    private ArrayList<AttendanceData> attendanceDataList = new ArrayList<>();


    private ArrayList<String> parentsContactList = new ArrayList<>();
    private String oneParentContact = "";
    private String studentContact = "";


    private String postText = "";
    private int class_ID = 0;
    private String course_name = "";

    private String username = "";
    private String password = "";
    private String billing_info = "";
    private String from = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setClassroomsJFXComboBox();
        setStudentsCheckListView();
        setSelectAllCheckListener();
        academicYearComboBox.setItems(sharedData.academicYears);

        Utils.convertDate(fromDatePicker);
        Utils.convertDate(toDatePicker);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                getClassrooms();
                getHubtelAccount();

                return null;
            }
        };
        new Thread(task).start();
    }

    private void setClassroomsJFXComboBox(){
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
    }

    private void setStudentsCheckListView(){
        studentsCheckListView.setCellFactory(lv -> new CheckBoxListCell<Students>(studentsCheckListView::getItemBooleanProperty) {
            @Override
            public void updateItem(Students core, boolean empty) {
                super.updateItem(core, empty);
                setText(core == null ? "" :  core.getStudentName());
            }
        });

        studentsCheckListView.getCheckModel().getCheckedIndices().addListener((ListChangeListener<Integer>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (int i : c.getAddedSubList()) {
                        System.out.println(studentsCheckListView.getItems().get(i).getStudentName()+ " selected");
                    }
                }
                if (c.wasRemoved()) {
                    for (int i : c.getRemoved()) {
                        System.out.println(studentsCheckListView.getItems().get(i).getStudentName() + " deselected");
                    }
                }
            }
        });

        studentsCheckListView.setItems(studentsList);
    }

    private void getClassrooms()throws IOException {
        String sql = "select * from remedial_classrooms where Status = 'active'";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Class_ID");
                String name = rs.getString("Class_Name");

                classroomsList.add(new Classrooms(id, name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getStudentsForSelectedClass(int class_id)throws IOException{
        String sql = "select Surname, Other_Names, students.Student_ID " +
                "from remedial_students_classrooms " +
                "join students " +
                "on remedial_students_classrooms.Student_ID = students.Student_ID " +
                "where students.Status = 'active' and remedial_students_classrooms.Status = " +
                " 'active' and Admission_Type = 'remedial' and Class_ID = ? order by Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, class_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");

                tempStudentList.add(new Students(id, s_name + " " + o_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setStudentsList(){
        studentsList.clear();
        studentsList.addAll(tempStudentList);
        tempStudentList.clear();
    }

    public void onClassroomSelected(){
        Classrooms classroom = classroomsJFXComboBox.getValue();
        selectAllCheck.setSelected(false);

        if (classroom != null){
            class_ID = classroom.getClassID().get();

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    getStudentsForSelectedClass(classroom.getClassID().get());

                    Platform.runLater(()-> setStudentsList());

                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void setSelectAllCheckListener(){
        selectAllCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                studentsCheckListView.getCheckModel().checkAll();
            }else  studentsCheckListView.getCheckModel().clearChecks();
        });
    }

    public void getPostText(){
        postText = postTextArea.getText().trim();
    }

    public void onSend() throws HttpRequestException, IOException {
        ObservableList<Students> selectedStudents = studentsCheckListView.getCheckModel().getCheckedItems();
        if (selectedStudents.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Select at least one student", 2000);
            return;
        }
        String academic_year = academicYearComboBox.getValue();
        if (academic_year == null || academic_year.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Select academic year", 2000);
            return;
        }

        String st = fromDatePicker.getEditor().getText();
        String en = toDatePicker.getEditor().getText();

        if (st.isEmpty() || en.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Pick a date", 2000);
            return;
        }

        String[] d = st.split("/");
        String[] q = en.split("/");

        String startDate = d[2] + "-" + d[1] + "-" + d[0];
        String endDate = q[2] + "-" + q[1] + "-" + q[0];


        for (Students students : selectedStudents){
            attendanceDataList.clear();
            studentsCoursesList.clear();

            getStudentCourses(students.getStudentID());

            int grand_attendance_total = 0;
            int grand_present_total = 0;
            for (StudentsCourses course : studentsCoursesList){
                attendanceList.clear();
                parentsContactList.clear();

                if (sendToStudentsCheckBox.isSelected()){
                    getStudentContact(students.getStudentID());
                }

                if (sendToAllParentsRadioButton.isSelected()){
                    getParentsContactList(students.getStudentID());
                }else if (sendToOneParentsRadioButton.isSelected()){
                    getOneParentsContact(students.getStudentID());
                }

                getAttendance(students.getStudentID(), course.getCourseID(), course.getCourseType(), startDate, endDate);

                if (course.getCourseType().equals("core")){
                    getCoreCourseName(class_ID, course.getCourseID());
                }else if (course.getCourseType().equals("elective")){
                    getElectiveCourseName(course.getCourseID());
                }

                int present = 0;
                int total = attendanceList.size();

                for (String attend : attendanceList){
                    if (attend.equals("Present")){
                        ++present;
                    }
                }
                if (total > 0)
                attendanceDataList.add(new AttendanceData(present, total, course_name));
            }

            for (AttendanceData data : attendanceDataList){

                grand_attendance_total += data.getTotal();
                grand_present_total += data.getPresent();
            }

            // set message body

            String message = "Weekly School Attendance \n" +
                    "Name: " + students.getStudentName() + " \n" +
                    "Year: " + academic_year + " \n" +
                    "Date From: " + st + " \n" +
                    "Date To: " + en + " \n";

            for (AttendanceData data : attendanceDataList){
                message += data.getCourseName() + ": " + data.getPresent() + " out of " + data.getTotal() + " Periods \n";
            }

            message += "Attendance Summary: " + grand_present_total + " out of " + grand_attendance_total + " Periods. \n";

            if (!postText.isEmpty()){
                message += "Post Text: " + postText;
            }

            if (sendToStudentsCheckBox.isSelected()){
                sendMessage(studentContact, message);
            }

            if (sendToAllParentsRadioButton.isSelected()){
                for (String contact : parentsContactList){
                    sendMessage(contact, message);
                }
            }else if (sendToOneParentsRadioButton.isSelected()){
                sendMessage(oneParentContact, message);
            }

        }
    }

    private void getStudentCourses(String student_id)throws IOException{
        String sql = "select Course_ID, Type from remedial_student_courses where Student_ID = ?;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String type = rs.getString("Type");
                int id = rs.getInt("Course_ID");

                studentsCoursesList.add(new StudentsCourses(id, type));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getAttendance(String student_id, int course_id, String type, String st_date, String en_date)throws IOException{
        String sql = "select Attend from remedial_attendance where Student_ID = ? and  " +
                "Course_ID = ? and Type = ? and Date between ? and ?;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student_id);
            preparedStatement.setInt(2, course_id);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, st_date);
            preparedStatement.setString(5, en_date);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String attend = rs.getString("Attend");

                attendanceList.add(attend);
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getCoreCourseName(int class_id, int course_id)throws IOException{
        String sql = "select Course_Name from remedial_core_courses where Remedial_Course_ID = ? and Class_ID = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, course_id );
            preparedStatement.setInt(2, class_id );

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                 course_name = rs.getString("Course_Name");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getElectiveCourseName(int course_id)throws IOException{
        String sql = "select Course_Name from remedial_elective_courses where Elective_Course_ID = ? ";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, course_id );


            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                course_name = rs.getString("Course_Name");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getStudentContact(String student_id)throws IOException{
        String sql = "select student_contacts.Phone_Number from students " +
                "join student_contacts " +
                "on student_contacts.Student_ID = students.Student_ID " +
                "where students.Student_ID = ?;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                studentContact = rs.getString("Phone_Number");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getParentsContactList(String student_id)throws IOException{
        String sql = " select guardian_contacts.Phone_Number " +
                " from guardians " +
                " join guardian_contacts " +
                " on guardian_contacts.Guardian_ID = guardians.Guardian_ID " +
                " where guardians.Student_ID = ?;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String phone_number = rs.getString("Phone_Number");

                parentsContactList.add(phone_number);
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getOneParentsContact(String student_id)throws IOException{
        String sql = "select guardian_contacts.Phone_Number " +
                " from guardians " +
                " join guardian_contacts " +
                " on guardian_contacts.Guardian_ID = guardians.Guardian_ID " +
                " where guardians.Student_ID = ? limit 1;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, student_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                oneParentContact = rs.getString("Phone_Number");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void sendMessage(String contact, String message) throws HttpRequestException {
        BasicAuth auth = new BasicAuth(username, password);
        ApiHost host = new ApiHost(auth);

        MessagingApi messagingApi = new MessagingApi(host);

        MessageResponse response = messagingApi.sendQuickMessage(from, contact, message, billing_info);

        System.out.println("Server Response Status " + response.getStatus());
    }

    private void getHubtelAccount()throws IOException{
        String sql = "select Username, Password, Billing_Info, Fromm from hubtel_account";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                username = rs.getString("Username");
                password = rs.getString("Password");
                billing_info = rs.getString("Billing_Info");
                from = rs.getString("Fromm");
            }

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

    private static class StudentsCourses{
        int courseID;
        String courseType;

        StudentsCourses(int id, String type){
            courseID = id;
            courseType = type;
        }

        private int getCourseID(){
            return courseID;
        }

        private String getCourseType(){
            return courseType;
        }
    }

    private static class AttendanceData{
        int present;
        int total;
        String courseName;

        AttendanceData(int pres, int tot, String cou){
            present = pres;
            total = tot;
            courseName =  cou;
        }

        private int getPresent(){
            return present;
        }

        private int getTotal(){
            return total;
        }

        private String getCourseName(){
            return courseName;
        }
    }
}
