package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.sharedData;
import com.hubtel.*;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class clubAttendanceSMSController implements Initializable {

    @FXML
    private JFXComboBox<Clubs> clubsJFXComboBox;
    @FXML
    private CheckListView<Students> studentsCheckListView;
    @FXML
    private JFXComboBox<String> academicYearComboBox;
    @FXML
    private JFXCheckBox selectAllCheck;
    @FXML
    private TextArea postTextArea;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private Pane snackBarPane;
    @FXML
    private JFXCheckBox sendToStudentsCheckBox;
    @FXML
    private JFXRadioButton sendToAllParentsRadioButton;
    @FXML
    private JFXRadioButton sendToOneParentsRadioButton;

    private boolean is_data_present = false;

    private ObservableList<Clubs> clubList = FXCollections.observableArrayList();
    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Students> tempStudentList = FXCollections.observableArrayList();

    private String postText = "";


    private ArrayList<ClubSettings> clubSettings = new ArrayList<>();
    private String studentContact = "";
    private ArrayList<String> parentsContactList = new ArrayList<>();
    private String oneParentContact = "";

    private String username = "";
    private String password = "";
    private String billing_info = "";
    private String from = "";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setClubsJFXComboBox();
        setStudentsCheckListView();
        setSelectAllCheckListener();
        academicYearComboBox.setItems(sharedData.academicYears);

        Utils.convertDate(datePicker);


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                getClubs();
                getHubtelAccount();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void setClubsJFXComboBox(){
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

    private void getClubs()throws IOException {
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

    private void getStudentsForSelectedClub(String club_id)throws IOException{
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

    public void onClubSelected(){
        Clubs clubs = clubsJFXComboBox.getValue();
        selectAllCheck.setSelected(false);

        if (clubs != null){
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    getStudentsForSelectedClub(clubs.getClubID());

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

    public void onDatePicked(){
        String w = datePicker.getEditor().getText();
        if (!w.isEmpty()){

            String[] d = w.split("/");
            String date = d[2] + "-" + d[1] + "-" + d[0];

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    checkIfDataIsPresent(date);

                    Platform.runLater(()->{
                        if (!is_data_present){
                            Utils.showSnackBar(snackBarPane, "No attendance was found for this day", 2000);
                        }
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void checkIfDataIsPresent(String date)throws IOException{
        is_data_present = false;

        String sql = "select * from club_attendance where Date = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                is_data_present = true;
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onSend() throws IOException, HttpRequestException {
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

        String st = datePicker.getEditor().getText();

        if (st.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Pick a date", 2000);
            return;
        }

        String[] d = st.split("/");

        String date = d[2] + "-" + d[1] + "-" + d[0];

        if (is_data_present){
            for (Students students : selectedStudents){
                clubSettings.clear();

                if (sendToStudentsCheckBox.isSelected()){
                    getStudentContact(students.getStudentID());
                }

                if (sendToAllParentsRadioButton.isSelected()){
                    getParentsContactList(students.getStudentID());
                }else if (sendToOneParentsRadioButton.isSelected()){
                    getOneParentsContact(students.getStudentID());
                }

                ClubAttendance attendance =  getClubAttendance(date, students.getStudentID());

                if (attendance != null){
                    getClubSettings(attendance.getClubID());

                    LocalDate localDate = LocalDate.parse(date);
                    String day = localDate.getDayOfWeek().toString();

                    String start_time = "";
                    String end_time = "";
                    String club_day = "";
                    String club_name = "";
                    String attend = attendance.getAttend();
                    String student_name = students.getStudentName();

                    boolean does_club_meet_on_this_day = false;

                    for (ClubSettings settings : clubSettings){
                        if (day.toLowerCase().equals(settings.getClubDay().toLowerCase())){
                            start_time = settings.getStartTime().toString();
                            end_time = settings.getEndTime().toString();
                            club_day = settings.getClubDay();
                            club_name = settings.getClubName();
                            does_club_meet_on_this_day = true;
                            break;
                        }
                    }

                    if (!does_club_meet_on_this_day){
                        Utils.showSnackBar(snackBarPane, clubsJFXComboBox.getValue().getClubName() + " does not meet on " + day.toLowerCase(), 2000);
                        break;
                    }

                    // prepare message body
                    String message = "Club Attendance Report \n" +
                            "Student: " + student_name + " \n" +
                            "Club: " + club_name + " \n" +
                            "Date: " + club_day + "," + st + " \n" +
                            "Time: " + start_time + " to " + end_time + " \n" +
                            "Attendance: " + attend + " .\n";

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

                }else {
                    Utils.showSnackBar(snackBarPane, "No attendance was found for "+ students.getStudentName(), 2000);
                }
            }
        }else {
            Utils.showSnackBar(snackBarPane, "No attendance was found for this day", 2000);
        }
    }

    private ClubAttendance getClubAttendance(String date, String stud_id)throws IOException{
         ClubAttendance clubAttendance  = null;

        String sql = "SELECT Club_ID, Attend FROM club_attendance where Date = ? and Student_ID = ?;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, stud_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String attend = rs.getString("Attend");

                clubAttendance = new ClubAttendance(id, attend);
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
        return clubAttendance;
    }

    private void getClubSettings(String club_id)throws IOException{
        String sql = "SELECT clubs.Club_ID, Day, Start_Time, End_Time, Club_Name " +
                "from clubs " +
                "join club_settings " +
                "on clubs.Club_ID = club_settings.Club_ID " +
                " where clubs.Club_ID = ?;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String day = rs.getString("Day");
                LocalTime start_time = rs.getTime("Start_Time", Calendar.getInstance(TimeZone.getDefault())).toLocalTime();
                LocalTime end_time = rs.getTime("End_Time", Calendar.getInstance(TimeZone.getDefault())).toLocalTime();
                String name = rs.getString("Club_Name");

                clubSettings.add(new ClubSettings(id, day, start_time, end_time, name));
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
        String sql = "select guardian_contacts.Phone_Number " +
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

    private static class ClubSettings{
        String clubID;
        String clubDay;
        LocalTime startTime;
        LocalTime endTime;
        String ClubName;

        ClubSettings(String id, String day, LocalTime st, LocalTime et, String name){
            clubID = id;
            clubDay = day;
            startTime = st;
            endTime = et;
            ClubName = name;
        }

        private String getClubID(){
            return this.clubID;
        }

        private String getClubDay(){
            return this.clubDay;
        }

        private LocalTime getStartTime(){
            return this.startTime;
        }

        private LocalTime getEndTime(){
            return this.endTime;
        }

        private String getClubName(){
            return this.ClubName;
        }
    }

    private static class ClubAttendance{
        String clubID;
        String attend;

        ClubAttendance(String id, String attend){
            this.attend = attend;
            this.clubID = id;
        }

        private String getClubID(){
            return this.clubID;
        }

        private String getAttend(){
            return this.attend;
        }
    }
}
