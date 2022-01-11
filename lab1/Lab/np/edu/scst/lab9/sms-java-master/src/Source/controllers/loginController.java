package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.sharedData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;


public class loginController implements Initializable {
    @FXML
   private AnchorPane parent;
    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXTextField emailTextField;
    @FXML
    private JFXPasswordField passwordTextField;
    @FXML
    private JFXCheckBox rememberMe;
    @FXML
    private Label errorLabel;
    @FXML
    private ProgressIndicator progressIndicator;


    private double x, y = 0.0;
    private Stage stage;

    private String inputEmail;
    private String inputPassword;


    private String surnameFromDB;
    private String emailFromDB;
    private String other_nameFromDB;
    private String passwordFromDB;
    private String roleFromDB;
    private String userIDFromDB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        emailTextField.setText("techlead@piuniversaltech.com");
        passwordTextField.setText("natural");


    }




    public void onCloseBtnClicked(ActionEvent event){
       Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
       stage.close();
    }



    private void makeDraggable(){
        parent.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        parent.setOnMouseDragged(event -> {
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setX(event.getSceneX() - x);
            stage.setY(event.getSceneY() - y);
        });
    }


    public void validateEmailWileTyping(){
        hideErrorText();

        inputEmail = emailTextField.getText();
        boolean valid = Utils.isEmailValid(inputEmail);
        if (!valid){
            emailTextField.setStyle("-fx-border-color: #FA8072");
        }else {
            emailTextField.setStyle("-fx-border-color: white");
        }

    }

    public void whileTypingPassword(){
        hideErrorText();
    }

    private void setErrorText(String error){
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    private void hideErrorText(){
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    private boolean validateInputs(){
        inputEmail = emailTextField.getText().trim();
        inputPassword = passwordTextField.getText().trim();
        if (passwordTextField.getText().isEmpty() || emailTextField.getText().isEmpty()){
            setErrorText("Provide both Email and Password");
            return false;
        }
        return true;
    }

    public void onLoginBtnClicked(ActionEvent event){
        if (validateInputs()){
            showProgress();
            hideErrorText();
            processLogin(event);
        }
    }

    private void getUserWithEmail() throws IOException {
        String sql = "select * from users where Email = ?";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, inputEmail );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                userIDFromDB = rs.getString("User_ID");
               surnameFromDB = rs.getString("Surname");
               other_nameFromDB = rs.getString("Other_Names");
               emailFromDB = rs.getString("Email");
               passwordFromDB = rs.getString("Password");
               roleFromDB = rs.getString("Role");

                sharedData.userInfo =
                        new sharedData.currentUserInfo(userIDFromDB, surnameFromDB, other_nameFromDB, emailFromDB, roleFromDB);

            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void processLogin(ActionEvent event){
        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws IOException {
                getUserWithEmail();

                Platform.runLater(()-> {
                    try {
                        authenticateLogin(event);
                    } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
                        e.printStackTrace();
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void authenticateLogin(ActionEvent event) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        if (passwordFromDB != null){
            boolean matched = Utils.validatePassword(inputPassword, passwordFromDB);

            if (!matched){
                setErrorText("Invalid Log in details");
            }else {

                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                Stage rootWindow = new Stage();

                Parent root = FXMLLoader.load(getClass().getResource("/Source/views/root.fxml"));
                Scene rootScene = new Scene(root);
                rootScene.getStylesheets().add(getClass().getResource("/Source/styles/rootStyle.css").toExternalForm());

                rootWindow.initStyle(StageStyle.DECORATED);
                rootWindow.setScene(rootScene);
                rootWindow.getIcons().add( new Image("/Source/image/app_icon.png"));
                rootWindow.setTitle("Preset Admin System");

                rootWindow.show();
                window.close();
            }
        }else {
            setErrorText("Invalid Log in details");
        }

        hideProgress();

    }

    private void showProgress(){
        progressIndicator.setVisible(true);
    }

    private void hideProgress(){
        progressIndicator.setVisible(false);
    }
}
