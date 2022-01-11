package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class studentManagementController implements Initializable {

    @FXML
    private Label regularNumberLabel;
    @FXML
    private Label remedialNumberLabel;
    @FXML
    private Label clubNumberLabel;


    private String number_of_regular;
    private String number_of_remedial;
    private String number_of_club;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {
                countNumberOfRegularStudents();
                countNumberOfRemedialStudents();
                countNumberOfStudentsInClub();
                Platform.runLater(()-> {
                    regularNumberLabel.setText(number_of_regular);
                    remedialNumberLabel.setText(number_of_remedial);
                    clubNumberLabel.setText(number_of_club);
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    public void onRegularEnrollment(){
        applicationState.setUIState("regular_student_registration");
    }

    public void onRemedialEnrollment(){
        applicationState.setUIState("remedial_student_registration");
    }

    public void onClassroomManagement(){applicationState.setUIState("classroom_management");}

    public void onClubManagement(){
        applicationState.setUIState("club_management");
    }

    public void onAttendance(){
        applicationState.setUIState("attendance");
    }

    private void countNumberOfRegularStudents() throws IOException, SQLException {
        String sql = "select count(*) from students where Admission_Type = 'regular' and Status = 'active';";
        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                number_of_regular = rs.getString("count(*)");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void countNumberOfRemedialStudents()throws IOException{
        String sql = "select count(*) from students where Admission_Type = 'remedial' and Status = 'active';";
        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                number_of_remedial = rs.getString("count(*)");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void countNumberOfStudentsInClub()throws IOException{
        String sql = "select count(*) from students_clubs where Status = 'active';";
        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                number_of_club = rs.getString("count(*)");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }
}
