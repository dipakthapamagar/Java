package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import Source.sharedUtils.sharedData;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class rootController implements Initializable {

    @FXML
    private VBox globalContainer;
    @FXML
    private JFXButton studBtn;
    @FXML
    private JFXButton finBtn;
    @FXML
    private JFXButton qualBtn;
    @FXML
    private JFXButton reportBtn;
    @FXML
    private JFXButton attendBtn;
    @FXML
    private JFXButton aboutBtn;
    @FXML
    private JFXButton settingsBtn;
    @FXML
    private JFXButton logOutBtn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applicationState.currentUIState.addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("disable")){
                disableNavBtn();
            }

            if (newValue.equals("enable")){
                enableNavBtn();
            }

           if (newValue.equals("student")){

               Parent studentManagement = null;
               try {
                   studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/studentManagement.fxml"));
               } catch (IOException e) {
                   e.printStackTrace();
               }
//               studentManagement.getStylesheets().add(getClass().getResource("../styles/studentManagementStyle.css").toExternalForm());
               globalContainer.getChildren().clear();

               Utils.setFadeInTransition(studentManagement);

               globalContainer.getChildren().add(studentManagement);
           }

            if (newValue.equals("regular_student_registration")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/regularStudentRegistration.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                studentManagement.getStylesheets().add(getClass().getResource("../styles/regularStudentRegistrationStyle.css").toExternalForm());
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("remedial_student_registration")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/remedialRegistrationOne.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                studentManagement.getStylesheets().add(getClass().getResource("../styles/remedialRegistrationOneStyle.css").toExternalForm());
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("remedial_educational_background")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/remedialBackground.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                studentManagement.getStylesheets().add(getClass().getResource("../styles/remedialBackgroundStyle.css").toExternalForm());
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("classroom_management")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/classroomManagement.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                studentManagement.getStylesheets().add(getClass().getResource("../styles/remedialBackgroundStyle.css").toExternalForm());
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("assign_regular_classroom")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/regularAssignClass.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("assign_remedial_subject")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/assignRemedialCourse.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("regular_promotion")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/regularPromotion.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("club_management")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/clubManagement.fxml"));
                    studentManagement.getStylesheets().add(getClass().getResource("/Source/styles/clubTabStyle.css").toExternalForm());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }

            if (newValue.equals("attendance")){

                Parent studentManagement = null;
                try {
                    studentManagement = FXMLLoader.load(getClass().getResource("/Source/views/attendance.fxml"));
//                    studentManagement.getStylesheets().add(getClass().getResource("../styles/clubTabStyle.css").toExternalForm());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                globalContainer.getChildren().clear();

                Utils.setFadeInTransition(studentManagement);

                globalContainer.getChildren().add(studentManagement);
            }
        });

        applicationState.setUIState("student");
    }

    public void onLogOut(ActionEvent event) throws IOException {
        int res =  Utils.confirmationDialog("Confirm Action",
                "Log Out?",
                "This will take you out of the application");

        if (res == Utils.YES){
            Stage rootWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage loginWindow = new Stage();
            Parent login = FXMLLoader.load(getClass().getResource("/Source/views/login.fxml"));
            Scene loginScene = new Scene(login);
            loginScene.getStylesheets().add(getClass().getResource("/Source/styles/loginStyle.css").toExternalForm());
            loginScene.setFill(Color.TRANSPARENT);

            loginWindow.initStyle(StageStyle.TRANSPARENT);
            loginWindow.setScene(loginScene);
            loginWindow.getIcons().add( new Image("/Source/image/app_icon.png"));
            loginWindow.show();

            applicationState.setUIState("log_out");

            rootWindow.close();
        }
    }

    public void onStudentClicked(){
        applicationState.setUIState("student");
    }

    public void onAttendanceClicked(){
        applicationState.setUIState("attendance");
    }

    public void onSettingsClicked(ActionEvent event) throws IOException {
        String role = sharedData.userInfo.getRole();
        if (role.equals("admin") || role.equals("developer")){
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle("Settings");

            Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/settings.fxml"));
            Scene regularScene = new Scene(regular);

            stage.setScene(regularScene);

            stage.initOwner(window);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.getIcons().add( new Image("/Source/image/app_icon.png"));
            stage.showAndWait();
        }else {
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle("Settings");

            Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/userSettings.fxml"));
            Scene regularScene = new Scene(regular);

            stage.setScene(regularScene);

            stage.initOwner(window);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.getIcons().add( new Image("/Source/image/app_icon.png"));
            stage.showAndWait();
        }
    }

    public void onHelpClicked(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Help");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/help.fxml"));
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/pi.png"));
        stage.showAndWait();
    }

    private void disableNavBtn(){
       studBtn.setDisable(true);
       finBtn.setDisable(true);
       qualBtn.setDisable(true);
       reportBtn.setDisable(true);
       attendBtn.setDisable(true);

        aboutBtn.setDisable(true);
        settingsBtn.setDisable(true);
        logOutBtn.setDisable(true);
    }

    private void enableNavBtn(){
        studBtn.setDisable(false);
        finBtn.setDisable(false);
        qualBtn.setDisable(false);
        reportBtn.setDisable(false);
        attendBtn.setDisable(false);

        aboutBtn.setDisable(false);
        settingsBtn.setDisable(false);
        logOutBtn.setDisable(false);
    }
}
