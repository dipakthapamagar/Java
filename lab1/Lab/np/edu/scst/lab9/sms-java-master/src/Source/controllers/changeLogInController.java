package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.sharedData;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class changeLogInController implements Initializable {
    @FXML
    private Pane snackBarPane;
    @FXML
    private JFXTextField surnameTextField;
    @FXML
    private JFXTextField other_nameTextField;
    @FXML
    private JFXTextField emailTextField;
    @FXML
    private JFXPasswordField passwordTextField;
    @FXML
    private JFXPasswordField conPassTextField;

    private String surname = "";
    private String other_name = "";
    private String email = "";
    private String password = "";
    private String conPass = "";
    private String hash = "";

    private ArrayList<String> allTakenEmails = new ArrayList<>();
    private boolean network_error = false;

    public changeLogInController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchAllEmailsInDB();
                return null;
            }
        };
        new Thread(task).start();

        surname = sharedData.userInfo.getSurname();
        other_name = sharedData.userInfo.getOther_names();
        email = sharedData.userInfo.getEmail();
        surnameTextField.setText(surname);
        other_nameTextField.setText(other_name);
        emailTextField.setText(email);


    }

    public void validateEmailWhileTyping(){
        boolean valid = isEmailValid(emailTextField.getText().trim());
        if (valid){
            email = emailTextField.getText().trim();
            emailTextField.setStyle("-fx-border-color: white");
            for (String mail : allTakenEmails){
                if (mail.equals(email)){
                    emailTextField.setStyle("-fx-border-color: red");
                    Utils.showSnackBar(snackBarPane, "Sorry this Email has been taken", 2000);
                    email = "";
                    break;
                }
            }
        }else {
            email = "";
            emailTextField.setStyle("-fx-border-color: red");
        }
    }

    private boolean isEmailValid(String email){
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void fetchAllEmailsInDB()throws IOException {
        String sql = "select Email from users ";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                allTakenEmails.add(rs.getString("Email"));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void getPassword1(){
        passwordTextField.setStyle("-fx-border-color: white");

        password = passwordTextField.getText().trim();

        // remove whitespace
        passwordTextField.setText(password);
        passwordTextField.endOfNextWord();


        if (password.length() < 8){
            passwordTextField.setStyle("-fx-border-color: red");
        }

    }

    public void getPassword2(){
        conPassTextField.setStyle("-fx-border-color: white");

        conPass = conPassTextField.getText().trim();

        // remove whitespace
        conPassTextField.setText(conPass);
        conPassTextField.endOfNextWord();


        if (conPass.length() < 8){
            conPassTextField.setStyle("-fx-border-color: red");
        }

    }

    public void getSurname(){
        surname = surnameTextField.getText().trim();
    }

    public void getOtherName(){
        other_name = other_nameTextField.getText().trim();
    }

    public void onSave() throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (surname.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Provide Username", 2000);
            return;
        }
        if (other_name.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Provide Other name", 2000);
            return;
        }
        if (email.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Provide valid email", 2000);
            return;
        }
        if (!conPass.equals(password)){
            Utils.showSnackBar(snackBarPane, "Passwords do not match", 2000);
            return;
        }
        if (password.isEmpty()){
            Utils.showSnackBar(snackBarPane, "Password is required", 2000);
            return;
        }

        hash = Utils.generateStrongPasswordHash(password);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateUser();
                Platform.runLater(()->{
                    if (network_error){
                        Utils.showSnackBar(snackBarPane, "Unable to save", 2000);
                    }else Utils.showSnackBar(snackBarPane, "Saved", 2000);
                });
                return null;
            }
        };
        new Thread(task).start();
    }


    private void updateUser()throws IOException{
        String QUERY = "update users set Surname = ?, Other_Names = ?, Email = ?, Password = ? where User_ID = ?";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {
            preparedStatement.setString(1, surname);
            preparedStatement.setString(2, other_name);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, hash);
            preparedStatement.setString(5, sharedData.userInfo.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            network_error = true;
            Utils.printSQLException(e);
        }
    }
}
