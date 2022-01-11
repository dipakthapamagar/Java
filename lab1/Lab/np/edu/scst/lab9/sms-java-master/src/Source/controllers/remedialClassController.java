package Source.controllers;

import Source.sharedUtils.Utils;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;

public class remedialClassController {

    @FXML
    private JFXTextField classNameTextField;
    @FXML
    private Label saveLabel;

    private int class_id = 0;

    public void onSave(){
        classNameTextField.setStyle("fx-border-color: white");
        if (classNameTextField.getText().isEmpty()){
            classNameTextField.setStyle("fx-border-color: #FA8072");
            return;
        }

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws IOException {
                createNewClass();
                getCreatedClassIdFromDB();
                createCoreSubjects();
                Platform.runLater(()-> {
                    saveLabel.setVisible(true);
                });
                return null;
            }
        };
        new Thread(task).start();

    }

    public void onClose(ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    private void createNewClass()throws IOException {
        String sql = "insert into remedial_classrooms (`Class_Name`, `Status`) value(?, ?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, classNameTextField.getText().trim());
            preparedStatement.setString(2, "active");

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void getCreatedClassIdFromDB()throws IOException{
        String sql = "select Class_ID from remedial_classrooms where Class_Name = ? and Status = 'active'; ";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, classNameTextField.getText().trim() );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                class_id = rs.getInt("Class_ID");
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void createCoreSubjects()throws IOException{
        String query = "{ call createCoreSubjects(?) }";

        try (Connection conn = Utils.connectToDatabase();
             CallableStatement stmt = conn.prepareCall(query)) {

            stmt.setInt(1, class_id);

            stmt.executeQuery();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void whileTyping(){
        saveLabel.setVisible(false);
    }
}
