package Source.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.tools.Borders;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class attendanceController implements Initializable {
    @FXML
    private VBox clubMenuBox;
    @FXML
    private VBox remedialMenuBox;
    @FXML
    private HBox menuBox;
    @FXML
    private Separator middleSeparator;
    @FXML
    private Separator leftSeparator;
    @FXML
    private Separator rightSeparator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addBorders();
    }

    private void addBorders(){
        Node wrappedBox1 = Borders.wrap(clubMenuBox)
                .lineBorder()
                .title("Club Attendance Menu")
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
                .title("Remedial Attendance Menu")
                .color(Color.GREEN)
                .thickness(1, 0, 0, 0)
                .thickness(1)
                .radius(0, 5, 5, 0)
                .build()
                .emptyBorder()
                .padding(10)
                .build().build();

        menuBox.getChildren().clear();
        menuBox.getChildren().addAll(leftSeparator,wrappedBox1,middleSeparator, wrappedBox2, rightSeparator);
    }

    public void onPrintAttendanceSheet(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Remedial Attendance Sheet Print Out");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/printRemedialAttendanceSheet.fxml"));
        regular.getStylesheets().add(getClass().getResource("/Source/styles/printOutStyle.css").toExternalForm());
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onRemedialDateEntry(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Remedial Attendance Data Entry");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/dataEntryRemedial.fxml"));
        regular.getStylesheets().add(getClass().getResource("/Source/styles/clubTabStyle.css").toExternalForm());
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onClubDataEntry(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Club Attendance Data Entry");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/dataEntryClub.fxml"));
        regular.getStylesheets().add(getClass().getResource("/Source/styles/clubTabStyle.css").toExternalForm());
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onRemedialSendSMS(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Remedial Attendance SMS");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/remedialAttendanceSMS.fxml"));
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onClubSendSMS(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Club Attendance SMS");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/clubAttendanceSMS.fxml"));
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }

    public void onPrintClubAttendance(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Club Attendance Sheet Print Out");

        Parent regular = FXMLLoader.load(getClass().getResource("/Source/views/printClubAttendanceSheet.fxml"));
        regular.getStylesheets().add(getClass().getResource("/Source/styles/printOutStyle.css").toExternalForm());
        Scene regularScene = new Scene(regular);

        stage.setScene(regularScene);

        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.getIcons().add( new Image("/Source/image/app_icon.png"));
        stage.showAndWait();
    }
}
