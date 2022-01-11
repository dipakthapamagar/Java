package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.tools.Borders;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class classroomManagementController implements Initializable {

    @FXML
    private HBox regularMenuBox;
    @FXML
    private HBox remedialMenuBox;
    @FXML
    private VBox menuBox;
    @FXML
    private Label regLabel;
    @FXML
    private Label remLabel;
    @FXML
    private Label clubLabel;
    @FXML
    private HiddenSidesPane settingsPane;
    @FXML
    private StackPane stackPane;

    private MaskerPane maskerPane = new MaskerPane();

    private String number_of_new_regular = "";
    private String number_of_new_remedial = "";
    private String number_of_new_club_membership = "";

    private JFXDatePicker wassceDate;
    private JFXDatePicker novDecDate;

    private String WASSCE = "";
    private String NOVDEC = "";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maskerPane.setVisible(false);
        stackPane.getChildren().add(maskerPane);
        addBorders();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws IOException {
                countNewRegularStudents();
                countNewRemedialStudents();
                getExistingDate();
                countNewClubMemberShip();
                Platform.runLater(()-> {
                    regLabel.setText(number_of_new_regular);
                    remLabel.setText(number_of_new_remedial);
                    clubLabel.setText(number_of_new_club_membership);

                    wassceDate.getEditor().setText(WASSCE);
                    novDecDate.getEditor().setText(NOVDEC);
                });
                return null;
            }
        };
        new Thread(task).start();


        try {
            settingsPane.setRight(setSideBarContents());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addBorders(){
        Node wrappedBox1 = Borders.wrap(regularMenuBox)
                .lineBorder()
                .title("Regular Students")
                .color(Color.GREEN)
                .thickness(1, 0, 0, 0)
                .thickness(1)
                .radius(0, 5, 5, 0)
                .build()
                .emptyBorder()
                .padding(10)
                .build().build();

        Node wrappedBox2 = Borders.wrap(remedialMenuBox)
                .lineBorder()
                .title("Remedial Students")
                .color(Color.GREEN)
                .thickness(1, 0, 0, 0)
                .thickness(1)
                .radius(0, 5, 5, 0)
                .build()
                .emptyBorder()
                .padding(10)
                .build().build();

        menuBox.getChildren().addAll(wrappedBox1, wrappedBox2);
    }

    public void onCreateRegularClass(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        Stage regularClass = new Stage();
        regularClass.setTitle("New Regular Class");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/regularClass.fxml"));
        Scene regularScene = new Scene(regular);

        regularClass.setScene(regularScene);

        regularClass.initOwner(window);
        regularClass.initModality(Modality.APPLICATION_MODAL);
        regularClass.getIcons().add( new Image("/Source/image/app_icon.png"));
        regularClass.showAndWait();
    }

    public void onCreateRemedialClass(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        Stage remedialClass = new Stage();
        remedialClass.setTitle("New Remedial Class");

        Parent remedial = FXMLLoader.load(getClass().getResource("/Source/views/remedialClass.fxml"));
        Scene remedialScene = new Scene(remedial);

        remedialClass.setScene(remedialScene);

        remedialClass.initOwner(window);
        remedialClass.initModality(Modality.APPLICATION_MODAL);
        remedialClass.getIcons().add( new Image("/Source/image/app_icon.png"));
        remedialClass.showAndWait();
    }

    private void countNewRegularStudents()throws IOException{
        LocalDate now = LocalDate.now();
        String todayDate = now.toString();
        String[] d = todayDate.split("-");
        String final_date = d[0] + "-" + d[1] + "-" + "01";


        String sql = "select count(*) from students where Admission_Date > ? and Admission_Type = 'regular'and Status = 'active';";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, final_date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                number_of_new_regular = rs.getString("count(*)");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void countNewRemedialStudents()throws IOException{
        LocalDate now = LocalDate.now();
        String todayDate = now.toString();
        String[] d = todayDate.split("-");
        String final_date = d[0] + "-" + d[1] + "-" + "01";


        String sql = "select count(*) from students where Admission_Date > ? and Admission_Type = 'remedial' and Status = 'active' ;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, final_date);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                number_of_new_remedial = rs.getString("count(*)");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void countNewClubMemberShip()throws IOException{
        LocalDate now = LocalDate.now();
        String todayDate = now.toString();
        String[] d = todayDate.split("-");
        String final_date = d[0] + "-" + d[1] + "-" + "01";


        String sql = "SELECT count(*) FROM club_attendance where createdDate > ? ;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, final_date);
            ResultSet rs = preparedStatement.executeQuery();

            System.out.println(preparedStatement);
            while (rs.next()) {
                number_of_new_club_membership = rs.getString("count(*)");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onAssignRegularStudent(){
        applicationState.isRegular = true;
        applicationState.setUIState("assign_regular_classroom");
    }

    public void onAssignRemedialStudent(){
        applicationState.isRegular = false;
        applicationState.setUIState("assign_regular_classroom");
    }

    public void onAssignSubject(){
        applicationState.setUIState("assign_remedial_subject");
    }

    public void onRegularPromotion(){
        applicationState.setUIState("regular_promotion");
    }

    private VBox setSideBarContents()throws IOException{
        VBox container = new VBox();

        VBox was_box = new VBox();
        Label was_label = new Label("MAY/JUNE WASSCE DATE");
        was_label.setStyle("-fx-text-fill: white;");
        wassceDate = new JFXDatePicker();
        wassceDate.setPromptText("WASSCE date");
        Utils.convertDate(wassceDate);
        wassceDate.setDefaultColor(Paint.valueOf("ROYALBLUE"));
        was_box.getChildren().addAll(was_label, wassceDate);

        wassceDate.setOnAction(event -> {
            String[] date = wassceDate.getEditor().getText().split("/");
            try {
                saveWASSCEDate(date[2] + "-" + date[1] + "-" +date[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            settingsPane.setPinnedSide(null);
        });

        wassceDate.setOnMouseClicked(event -> settingsPane.setPinnedSide(Side.RIGHT));



        VBox nov_box = new VBox();
        Label nov_label = new Label("NOV/DEC WASSCE DATE");
        nov_label.setStyle("-fx-text-fill: white");
        novDecDate = new JFXDatePicker();
        novDecDate.setPromptText("NOV/DEC date");
        Utils.convertDate(novDecDate);
        novDecDate.setDefaultColor(Paint.valueOf("ROYALBLUE"));
        nov_box.getChildren().addAll(nov_label, novDecDate);

        novDecDate.setOnAction(event -> {
          String[] date = novDecDate.getEditor().getText().split("/");
            try {
                saveNOVDECDate(date[2] + "-" + date[1] + "-" +date[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            settingsPane.setPinnedSide(null);
        });

        novDecDate.setOnMouseClicked(event -> settingsPane.setPinnedSide(Side.RIGHT));



        JFXButton closeRemedialBtn = new JFXButton("Close remedial class");
        closeRemedialBtn.setButtonType(JFXButton.ButtonType.RAISED);
        closeRemedialBtn.setStyle("-fx-background-color: #FFA07A; -fx-text-fill: #696969;");
        closeRemedialBtn.setRipplerFill(Paint.valueOf("white"));

        closeRemedialBtn.setOnAction(event -> {
         int res =  Utils.confirmationDialog(
                    "Confirm",
                    "Are you sure you want to close this year's class?",
                    "This will end this year's remedial class");

         if (res == Utils.YES){
             maskerPane.setVisible(true);
             maskerPane.setText("Closing...");
             applicationState.setUIState("disable");

             Task<Void> task = new Task<Void>(){

                 @Override
                 protected Void call() throws IOException {

                     deleteFromRemedialStudentsWithoutCourse();
                     deleteRemedialStudentsWithoutClass();
                     deactivateRemedialStudentsFromClass();
                     deactivateRemedialStudents();
                     deactivateAllRemedialClassrooms();

                     Platform.runLater(()-> {
                         maskerPane.setVisible(false);
                         applicationState.setUIState("enable");
                         remLabel.setText("0");

                     });
                     return null;
                 }
             };
             new Thread(task).start();
         }
        });


        container.setSpacing(40);
        container.setStyle("-fx-background-color: rgba(0,0, 255,.25); -fx-padding: 50 10 10 10");
        container.setFillWidth(true);
        container.getChildren().addAll(was_box, nov_box, closeRemedialBtn);
        return container;
    }

    private void saveNOVDECDate(String date)throws IOException{
        String sql = "update final_exam_reminder set NOV_DEC_date = ?";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void saveWASSCEDate(String date)throws IOException{
        String sql = "update final_exam_reminder set WASSCE_date = ?";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, date);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void getExistingDate()throws IOException{
        String sql = "select * from final_exam_reminder";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String[] q = rs.getString("WASSCE_date").split("-");
                WASSCE = q[2] + "/" + q[1] + "/" + q[0];

                String[] d = rs.getString("NOV_DEC_date").split("-");
                NOVDEC = d[2] + "/" + d[1] + "/" + d[0];
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void deactivateRemedialStudents()throws IOException{
        String sql = "update students set Status = 'graduated' where Admission_Type = 'remedial'; ";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }

    }

    private void deactivateRemedialStudentsFromClass()throws IOException{
        String sql = "update remedial_students_classrooms set Status = 'inactive' ";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void deleteRemedialStudentsWithoutClass()throws IOException{
        String sql = "delete from remedial_students_without_class";

        try (Connection connection = Utils.connectToDatabase();

             Statement statement = connection.createStatement()) {

             statement.executeUpdate(sql);

        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void deleteFromRemedialStudentsWithoutCourse()throws IOException{
        String sql = "delete from remedial_student_without_course";

        try (Connection connection = Utils.connectToDatabase();

             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void deactivateAllRemedialClassrooms()throws IOException{
        String sql = "update remedial_classrooms set Status = 'inactive' where Status = 'active'; ";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }
}
