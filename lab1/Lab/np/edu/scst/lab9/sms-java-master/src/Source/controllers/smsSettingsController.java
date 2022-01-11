package Source.controllers;

import Source.sharedUtils.Utils;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class smsSettingsController implements Initializable {
    @FXML
    private Pane snackBarPane;
    @FXML
    private JFXTextField usernameTextField;
    @FXML
    private JFXTextField passwordTextField;
    @FXML
    private JFXTextField billingTextField;


    private String username = "";
    private String password = "";
    private String billing_info = "";

    private  boolean error = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                getHubtelAccount();
                Platform.runLater(()->{
                    usernameTextField.setText(username);
                    passwordTextField.setText(password);
                    billingTextField.setText(billing_info);
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void getHubtelAccount()throws IOException{
        String sql = "select Username, Password, Billing_Info from hubtel_account";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                username = rs.getString("Username");
                password = rs.getString("Password");
                billing_info = rs.getString("Billing_Info");

            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void setBilling_info(){
        billing_info = billingTextField.getText().trim();
    }

    public void setUsername(){
        username = usernameTextField.getText().trim();
    }

    public void setPassword(){
        password = passwordTextField.getText().trim();
    }

    private void updateAccount()throws IOException {
        String sql = "update hubtel_account set Username = ?, Password = ?, Billing_Info = ? where ID = 1";
        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, billing_info);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            error = true;
            Utils.printSQLException(e);
        }
    }

    public void onSave() throws IOException {
        updateAccount();
        if (error){
            Utils.showSnackBar(snackBarPane, "Unable to save", 2000);
            error = false;
        }else
        Utils.showSnackBar(snackBarPane, "Saved", 2000);
    }

}
