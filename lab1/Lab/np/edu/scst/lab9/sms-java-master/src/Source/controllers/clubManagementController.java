package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
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
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class clubManagementController implements Initializable {

    @FXML
    private JFXTextField clubNameTextField;
    @FXML
    private JFXCheckBox mondayCheck;
    @FXML
    private JFXCheckBox tuesdayCheck;
    @FXML
    private JFXCheckBox wednesdayCheck;
    @FXML
    private JFXCheckBox thursdayCheck;
    @FXML
    private JFXCheckBox fridayCheck;
    @FXML
    private JFXCheckBox saturdayCheck;
    @FXML
    private JFXCheckBox sundayCheck;
    @FXML
    private JFXTimePicker monStart;
    @FXML
    private JFXTimePicker monEnd;
    @FXML
    private JFXTimePicker tuesStart;
    @FXML
    private JFXTimePicker tuesEnd;
    @FXML
    private JFXTimePicker wedStart;
    @FXML
    private JFXTimePicker wedEnd;
    @FXML
    private JFXTimePicker thursStart;
    @FXML
    private JFXTimePicker thursEnd;
    @FXML
    private JFXTimePicker friStart;
    @FXML
    private JFXTimePicker friEnd;
    @FXML
    private JFXTimePicker satStart;
    @FXML
    private JFXTimePicker satEnd;
    @FXML
    private JFXTimePicker sunStart;
    @FXML
    private JFXTimePicker sunEnd;
    @FXML
    private Pane newClubSnackBarPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<Clubs> clubsJFXComboBox;
    @FXML
    private VBox settingsContainer;
    @FXML
    private Pane updateClubSnackBarPane;
    @FXML
    private JFXListView<Students> studentsJFXListView;
    @FXML
    private CheckListView<Clubs> clubsCheckListView;
    @FXML
    private JFXComboBox<Classrooms> classroomsJFXComboBox;
    @FXML
    private Pane snackBarPane;

    private MaskerPane maskerPane = new MaskerPane();
    private JFXTextField updateClubName = new JFXTextField();


    private int number_of_selected_days = 0;
    private boolean create_error = false;
    private boolean update_error = false;
    private boolean insert_error = false;

    private int counter = 1;

    private ArrayList<HBox> settingToUpdate = new ArrayList<>();



    private ObservableList<Clubs> clubsList = FXCollections.observableArrayList();
    private ObservableList<ClubSettings> clubsSettingsList = FXCollections.observableArrayList();
    private ObservableList<String> daysOfWeek = FXCollections.observableArrayList(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<Classrooms> classroomList = FXCollections.observableArrayList();
    private ObservableList<Clubs> studentClubList = FXCollections.observableArrayList();

    private ObservableList<Students> tempStudentsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        maskerPane.setVisible(false);
        stackPane.getChildren().add(maskerPane);
        settingsContainer.setSpacing(30);
        settingsContainer.setFillWidth(false);


        updateClubName.setLabelFloat(true);
        updateClubName.setPrefWidth(400.0);
        updateClubName.setPromptText("Club Name");

        convertClockTo24Hour();
        checkBoxEventListeners();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {
                getClubList();
                getAllClassrooms();
                getAllStudents();

                return null;
            }
        };
        new Thread(task).start();


        setClubsJFXComboBoxFactory();
        setStudentsJFXListViewFactory();
        setClassroomsJFXComboBoxFactory();
        setClubsCheckListViewFactory();
        studentsJFXListView.setItems(studentsList);
    }


    private void convertClockTo24Hour(){
        monStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        monStart.set24HourView(true);

        monEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        monEnd.set24HourView(true);

        tuesStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        tuesStart.set24HourView(true);

        tuesEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        tuesEnd.set24HourView(true);

        wedStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        wedStart.set24HourView(true);

        wedEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        wedEnd.set24HourView(true);

        thursStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        thursStart.set24HourView(true);

        thursEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        thursEnd.set24HourView(true);

        friStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        friStart.set24HourView(true);

        friEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        friEnd.set24HourView(true);

        satStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        satStart.set24HourView(true);

        satEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        satEnd.set24HourView(true);

        sunStart.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        sunStart.set24HourView(true);

        sunEnd.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        sunEnd.set24HourView(true);
    }

    private void checkBoxEventListeners(){
        mondayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                monEnd.setDisable(false);
                monStart.setDisable(false);
                ++number_of_selected_days;

            }else {
                monEnd.setDisable(true);
                monStart.setDisable(true);

                monEnd.getEditor().clear();
                monStart.getEditor().clear();
                --number_of_selected_days;

            }

        });

        tuesdayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                tuesEnd.setDisable(false);
                tuesStart.setDisable(false);

                ++number_of_selected_days;

            }else {
                tuesEnd.setDisable(true);
                tuesStart.setDisable(true);

                tuesEnd.getEditor().clear();
                tuesStart.getEditor().clear();

                --number_of_selected_days;

            }

        });

        wednesdayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                wedEnd.setDisable(false);
                wedStart.setDisable(false);

                ++number_of_selected_days;


            }else {
                wedEnd.setDisable(true);
                wedStart.setDisable(true);

                wedEnd.getEditor().clear();
                wedStart.getEditor().clear();

                --number_of_selected_days;
            }

        });

        thursdayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                thursEnd.setDisable(false);
                thursStart.setDisable(false);

                ++number_of_selected_days;


            }else {
                thursEnd.setDisable(true);
                thursStart.setDisable(true);

                thursEnd.getEditor().clear();
                thursStart.getEditor().clear();

                --number_of_selected_days;

            }

        });

        fridayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                friEnd.setDisable(false);
                friStart.setDisable(false);

                ++number_of_selected_days;

            }else {
                friEnd.setDisable(true);
                friStart.setDisable(true);

                friEnd.getEditor().clear();
                friStart.getEditor().clear();

                --number_of_selected_days;

            }

        });


        saturdayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                satEnd.setDisable(false);
                satStart.setDisable(false);

                ++number_of_selected_days;
                System.out.println(number_of_selected_days);
            }else {
                satEnd.setDisable(true);
                satStart.setDisable(true);

                satEnd.getEditor().clear();
                satStart.getEditor().clear();

                --number_of_selected_days;

            }

        });

        sundayCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                sunEnd.setDisable(false);
                sunStart.setDisable(false);

                ++number_of_selected_days;
                System.out.println(number_of_selected_days);
            }else {
                sunEnd.setDisable(true);
                sunStart.setDisable(true);

                sunEnd.getEditor().clear();
                sunStart.getEditor().clear();

                --number_of_selected_days;
                System.out.println(number_of_selected_days);
            }

        });
    }

    public void onSaveNewClub(){
       String club_name = clubNameTextField.getText();
       if (club_name.isEmpty()){
           Utils.showSnackBar(newClubSnackBarPane, "Provide a club name", 2000);
           return;
       }

       // validation
        if (mondayCheck.isSelected()){
            String start =  monStart.getEditor().getText();
            String end =  monEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (tuesdayCheck.isSelected()){
            String start =  tuesStart.getEditor().getText();
            String end =  tuesEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (wednesdayCheck.isSelected()){
            String start =  wedStart.getEditor().getText();
            String end =  wedEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (thursdayCheck.isSelected()){
            String start =  thursStart.getEditor().getText();
            String end =  thursEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (fridayCheck.isSelected()){
            String start =  friStart.getEditor().getText();
            String end =  friEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (saturdayCheck.isSelected()){
            String start =  satStart.getEditor().getText();
            String end =  satEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (sundayCheck.isSelected()){
            String start =  sunStart.getEditor().getText();
            String end =  sunEnd.getEditor().getText();

            if (start.isEmpty() || end.isEmpty()){
                Utils.showSnackBar(newClubSnackBarPane, "Please provide club's meeting time", 2000);
                return;
            }
        }

        if (number_of_selected_days < 1){
            Utils.showSnackBar(newClubSnackBarPane, "Please select club's meeting days", 2000);
            return;
        }



      UUID clubID = UUID.randomUUID();
        String _clubID_ = clubID.toString();

        maskerPane.setVisible(true);
        applicationState.setUIState("disable");

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {

                insertData(_clubID_);

                Platform.runLater(()->{
                    applicationState.setUIState("enable");
                    maskerPane.setVisible(false);
                    if (create_error){
                        Utils.showSnackBar(newClubSnackBarPane, "A network Error occurred", 5000);
                        create_error = false;
                    }else {
                        Utils.showSnackBar(newClubSnackBarPane, "New club created", 2000);
                    }
                });
                return null;
            }
        };
        new Thread(task).start();


    }

    private void createNewClub(String id, String name)throws IOException {
        String sql = "insert into clubs (`Club_ID`, `Club_Name`) values(?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            create_error = true;
            Utils.printSQLException(e);
        }

    }

    private void createClubSettings(String id, String day, String s_time, String e_time)throws IOException{
        String sql = "insert into club_settings (`Club_ID`, `Day`, `Start_Time`, `End_Time`) values (?, ?, ?, ?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, day);
            preparedStatement.setString(3, s_time);
            preparedStatement.setString(4, e_time);


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            create_error = true;
            Utils.printSQLException(e);
        }

    }

    private void insertData(String _clubID_)throws IOException{
        createNewClub(_clubID_, clubNameTextField.getText());

        if (mondayCheck.isSelected()){
            String start =  monStart.getEditor().getText();
            String end =  monEnd.getEditor().getText();

            createClubSettings(_clubID_, "Monday", start, end);
        }

        if (tuesdayCheck.isSelected()){
            String start =  tuesStart.getEditor().getText();
            String end =  tuesEnd.getEditor().getText();

            createClubSettings(_clubID_, "Tuesday", start, end);

        }

        if (wednesdayCheck.isSelected()){
            String start =  wedStart.getEditor().getText();
            String end =  wedEnd.getEditor().getText();

            createClubSettings(_clubID_, "Wednesday", start, end);

        }

        if (thursdayCheck.isSelected()){
            String start =  thursStart.getEditor().getText();
            String end =  thursEnd.getEditor().getText();

            createClubSettings(_clubID_, "Thursday", start, end);

        }

        if (fridayCheck.isSelected()){
            String start =  friStart.getEditor().getText();
            String end =  friEnd.getEditor().getText();

            createClubSettings(_clubID_, "Friday", start, end);

        }

        if (saturdayCheck.isSelected()){
            String start =  satStart.getEditor().getText();
            String end =  satEnd.getEditor().getText();

            createClubSettings(_clubID_, "Saturday", start, end);
        }

        if (sundayCheck.isSelected()){
            String start =  sunStart.getEditor().getText();
            String end =  sunEnd.getEditor().getText();

            createClubSettings(_clubID_, "Sunday", start, end);

        }
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
                        } ;
                    }
                };

        clubsJFXComboBox.setConverter(new StringConverter<Clubs>() {
            @Override
            public String toString(Clubs club) {
                if (club == null){
                    return null;
                } else {
                    return club.getClubName();
                }
            }

            @Override
            public Clubs fromString(String _club_) {
                return null;
            }
        });
        clubsJFXComboBox.setCellFactory(cellFactory);
        clubsJFXComboBox.setItems(clubsList);
    }

    public void onClubSelected(){
        Clubs club = clubsJFXComboBox.getValue();
        if (club == null){
            return;
        }

        clubsSettingsList.clear();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {

                getClubSettings(club.getClubID());

                Platform.runLater(()->{
                    setUpSetting();
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void getClubList()throws IOException{
        String sql  = "select * from clubs";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String name = rs.getString("Club_Name");

                clubsList.add(new Clubs(id, name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getClubSettings(String club_id)throws IOException{
        String sql = "select * from club_settings where Club_ID = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club_id );

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String name = rs.getString("Day");
                String[] d = rs.getString("Start_Time").split(":");
                String[] q = rs.getString("End_Time").split(":");

                clubsSettingsList.add(new ClubSettings(id, name,d[0] + ":" + d[1], q[0] + ":" + q[1] ));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setUpSetting(){

        counter = 1;
        settingsContainer.getChildren().clear();

        updateClubName.setText(clubsJFXComboBox.getValue().getClubName());
        settingsContainer.getChildren().add(updateClubName);

        for (ClubSettings club : clubsSettingsList){
            HBox hBox = new HBox();
            hBox.setSpacing(20);

            JFXComboBox<String> day = new JFXComboBox<>();
            day.setLabelFloat(true);
            day.setPromptText("Week Day");
            day.setItems(daysOfWeek);
            day.setValue(club.getDay().get());


            JFXTimePicker start = new JFXTimePicker();
            start.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
            start.set24HourView(true);
            start.setPromptText("Start time");
            start.setDefaultColor(Paint.valueOf("ROYALBLUE"));
            start.getEditor().setText(club.getStartTime().get());


            JFXTimePicker end = new JFXTimePicker();
            end.setPromptText("End time");
            end.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
            end.set24HourView(true);
            end.setDefaultColor(Paint.valueOf("ROYALBLUE"));
            end.getEditor().setText(club.getEndTime().get());


            JFXButton delBtn = new JFXButton("Del");
            delBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE);
            icon.setFill(Color.color(1.0,0,0));
            icon.setSize("15.0");
            delBtn.setGraphic(icon);

            delBtn.setOnAction(event -> {
                Node thisDelBtn = (Node) event.getSource();
                Node theHBox = thisDelBtn.getParent();

                settingsContainer.getChildren().remove(theHBox);
                --counter;

                settingToUpdate.clear();

                ObservableList<Node> someNodes = settingsContainer.getChildren();

                int count = 0;
                for(Node node : someNodes){
                    if (count == 0){ // don't add the first member which is a text field
                        ++count;
                        continue;
                    }
                    settingToUpdate.add((HBox) node);
                    ++count;
                }
            });


            hBox.getChildren().addAll(day, start, end, delBtn);

            settingsContainer.getChildren().add(hBox);

            ++counter;

            settingToUpdate.add(hBox);
        }

    }

    public void onAddNewDay(){
        if (counter > 7){
            return;
        }

        HBox hBox = new HBox();
        hBox.setSpacing(20);

        JFXComboBox<String> day = new JFXComboBox<>();
        day.setLabelFloat(true);
        day.setPromptText("Week Day");
        day.setItems(daysOfWeek);
        day.setValue("Monday");



        JFXTimePicker start = new JFXTimePicker();
        start.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        start.set24HourView(true);
        start.setPromptText("Start time");
        start.setDefaultColor(Paint.valueOf("ROYALBLUE"));
        start.getEditor().setText("00:00");



        JFXTimePicker end = new JFXTimePicker();
        end.setPromptText("End time");
        end.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK));
        end.set24HourView(true);
        end.setDefaultColor(Paint.valueOf("ROYALBLUE"));
        end.getEditor().setText("00:00");



        JFXButton delBtn = new JFXButton("Del");
        delBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE);
        icon.setFill(Color.color(1.0,0,0));
        icon.setSize("15.0");
        delBtn.setGraphic(icon);

        delBtn.setOnAction(event -> {
            Node thisDelBtn = (Node) event.getSource();
            Node theHBox = thisDelBtn.getParent();

            settingsContainer.getChildren().remove(theHBox);
            --counter;

            settingToUpdate.clear();

            ObservableList<Node> someNodes = settingsContainer.getChildren();

            for(Node node : someNodes){
                settingToUpdate.add((HBox) node);
            }
        });


        hBox.getChildren().addAll(day, start, end, delBtn);

        settingsContainer.getChildren().add(hBox);

        ++counter;

        settingToUpdate.add(hBox);


    }

    public void onSaveChanges() throws ClassNotFoundException{
        if (updateClubName.getText().isEmpty()){
            Utils.showSnackBar(updateClubSnackBarPane, "Club name can not be empty", 2000);
            return;
        }

        maskerPane.setVisible(true);
        applicationState.setUIState("disable");


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Clubs club = clubsJFXComboBox.getValue();

                deletePreviousSettings(club.getClubID());
                deleteClub(club.getClubID());

                String club_id = UUID.randomUUID().toString();
                createNewClub(club_id, updateClubName.getText().trim());
                for (HBox box : settingToUpdate){
                    createClubSettings(club_id, ((JFXComboBox<String>)box.getChildren().get(0)).getValue(),
                            ((JFXTimePicker)box.getChildren().get(1)).getEditor().getText(),
                            ((JFXTimePicker)box.getChildren().get(2)).getEditor().getText());
                }

                Platform.runLater(() -> {
                    maskerPane.setVisible(false);
                    applicationState.setUIState("enable");

                    if (create_error || update_error){
                        Utils.showSnackBar(newClubSnackBarPane, "A network Error occurred", 5000);
                        create_error = false;
                        update_error = false;
                    }else {
                        Utils.showSnackBar(newClubSnackBarPane, "Club Updated Successfully", 5000);
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void deletePreviousSettings(String id)throws IOException{
        String sql = "delete from club_settings where Club_ID = '" + id + "'; ";

        try (Connection connection = Utils.connectToDatabase();

             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);


        } catch (SQLException e) {
            update_error = true;
            Utils.printSQLException(e);
        }
    }

    private void deleteClub(String id)throws IOException{
        String sql = "delete from clubs where Club_ID = '" + id + "'; ";

        try (Connection connection = Utils.connectToDatabase();

             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);


        } catch (SQLException e) {
            update_error = true;
            Utils.printSQLException(e);
        }

    }

    private void getAllStudents()throws IOException{
        String sql = "select Student_ID, Surname, Other_Names from students where Admission_Type = 'regular' and Status = 'active' " +
                " order by Surname; ";

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

    private void getStudentsFromClassroom(int c_id)throws IOException{
        String sql = "select students.Student_ID, Surname, Other_Names from student_classroom " +
                "join students " +
                "on students.Student_ID = student_classroom.Student_ID " +
                "where Classroom_ID = ? and student_classroom.Status = 'active' and students.Status = 'active' order by Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, c_id );

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String o_name = rs.getString("Surname");
                String s_name = rs.getString("Other_Names");

                tempStudentsList.add(new Students(id, (s_name + " " + o_name)));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getAllClassrooms()throws IOException{
        String sql = "select Classroom_ID, Classroom_Name from classrooms";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Classroom_ID");
                String name = rs.getString("Classroom_Name");


                classroomList.add(new Classrooms(id, name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
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
            public String toString(Classrooms _class) {
                if (_class == null){
                    return null;
                } else {
                    return _class.getClassroomName();
                }
            }

            @Override
            public Classrooms fromString(String _class_) {
                return null;
            }
        });
        classroomsJFXComboBox.setCellFactory(cellFactory);
        classroomsJFXComboBox.setItems(classroomList);
    }

    private void setClubsCheckListViewFactory(){
        clubsCheckListView.setCellFactory(lv -> new CheckBoxListCell<Clubs>(clubsCheckListView::getItemBooleanProperty) {
            @Override
            public void updateItem(Clubs core, boolean empty) {
                super.updateItem(core, empty);
                setText(core == null ? "" :  core.getClubName());
            }
        });

        clubsCheckListView.getCheckModel().getCheckedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Integer> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (int i : c.getAddedSubList()) {
                            System.out.println(clubsCheckListView.getItems().get(i).getClubName() + " selected");
                        }
                    }
                    if (c.wasRemoved()) {
                        for (int i : c.getRemoved()) {
                            System.out.println(clubsCheckListView.getItems().get(i).getClubName()+ " deselected");
                        }
                    }
                }
            }
        });

        clubsCheckListView.setItems(clubsList);
    }

    public void onClassSelected(){
        Classrooms classroom = classroomsJFXComboBox.getValue();
        clubsCheckListView.getCheckModel().clearChecks();

        if (classroom != null){

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    getStudentsFromClassroom(classroom.getClassroomID());

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setStudentsList();
                        }
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
    }

    public void onStudentSelected(){
        Students student = studentsJFXListView.getSelectionModel().getSelectedItem();

        if (student != null){
            studentClubList.clear();

            clubsCheckListView.setDisable(false);

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    fetchStudentsClub(student.getStudentID());

                    Platform.runLater(() -> {
                        clubsCheckListView.getCheckModel().clearChecks();

                        for (Clubs club : studentClubList){
                            int index = 0;
                            for (Clubs mainClubs : clubsList){
                                if (mainClubs.getClubID().equals(club.getClubID())){
                                    clubsCheckListView.getCheckModel().check(index);
                                }
                                ++index;
                            }

                        }
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void fetchStudentsClub(String s_id)throws IOException{
        String sql = "select clubs.Club_Name, clubs.Club_ID from students_clubs " +
                "join clubs " +
                "on clubs.Club_ID = students_clubs.Club_ID " +
                "where students_clubs.Student_ID = ?  and Status = 'active';";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String name = rs.getString("Club_Name");

                studentClubList.add(new Clubs(id , name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void deleteStudentsClubs(String s_id)throws IOException{
        String sql = "delete from students_clubs where Student_ID = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id );
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            insert_error = true;
            Utils.printSQLException(e);
        }
    }

    private void addStudentToClub(String s_id, String c_id)throws IOException{
        String sql = "insert into students_clubs (`Student_ID`, `Club_ID`, `Status`) values(?,?,?);";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id );
            preparedStatement.setString(2, c_id );
            preparedStatement.setString(3, "active" );

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            insert_error = true;
            Utils.printSQLException(e);
        }
    }

    public void onSaveBtnClicked(){
        Students selectedStudent = studentsJFXListView.getSelectionModel().getSelectedItem();

        if (selectedStudent == null){
            Utils.showSnackBar(snackBarPane, "Select a student",2000);
            return;
        }

        ObservableList<Clubs> selectedClubs = clubsCheckListView.getCheckModel().getCheckedItems();

        if (selectedClubs.size() == 0){
            Utils.showSnackBar(snackBarPane, "Select a a club",2000);
            return;
        }

        applicationState.setUIState("disable");
        maskerPane.setVisible(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                deleteStudentsClubs(selectedStudent.getStudentID());

                for (Clubs club : selectedClubs){
                    addStudentToClub(selectedStudent.getStudentID(), club.getClubID());
                }

                Platform.runLater(() -> {
                    applicationState.setUIState("enable");
                    maskerPane.setVisible(false);

                  if (insert_error){
                      Utils.showSnackBar(snackBarPane, "A network error occurred", 5000);
                      insert_error = false;
                  }else {
                      Utils.showSnackBar(snackBarPane, "Student has been added to the clubs", 2000);
                      refreshPage();
                  }
                });
                return null;
            }
        };

        new Thread(task).start();
    }

    private void refreshPage(){
        studentsJFXListView.getSelectionModel().clearSelection();
        clubsCheckListView.getCheckModel().clearChecks();
        clubsCheckListView.getSelectionModel().clearSelection();
        clubsCheckListView.setDisable(true);
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
        StringProperty clubID = new SimpleStringProperty();
        StringProperty startTime = new SimpleStringProperty();
        StringProperty endTime = new SimpleStringProperty();
        StringProperty day = new SimpleStringProperty();

        ClubSettings(String id, String day, String st, String et){
            this.clubID.set(id);
            this.day.set(day);
            this.endTime.set(et);
            this.startTime.set(st);
        }

        public StringProperty getClubID(){
            return this.clubID;
        }

        StringProperty getStartTime(){
            return this.startTime;
        }

        StringProperty getEndTime(){
            return this.endTime;
        }

        StringProperty getDay(){
            return this.day;
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
