package Source.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class settingsController {
    public void onSMSSettings(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Message Settings");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/smsSettings.fxml"));
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onNewUser(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("New User");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/newUser.fxml"));

        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onChangeInfo(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Change Log In Info");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/changeLogIn.fxml"));
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }
}
