package Source.controllers;

import Source.sharedUtils.Utils;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class regularClassController implements Initializable {

    @FXML
    private JFXTextField classNameTextField;
    @FXML
    private JFXComboBox<YearGroup> yearGroupJFXComboBox;
    @FXML
    private Label saveLabel;


    private ObservableList<YearGroup> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws IOException {
                getYears();
                return null;
            }
        };
        new Thread(task).start();

        setUpComboBox();
        yearGroupJFXComboBox.setItems(list);
    }

    public void onSave(){
        if (!classNameTextField.getText().isEmpty() && yearGroupJFXComboBox.getValue() != null){
            Task<Void> task = new Task<Void>(){

                @Override
                protected Void call() throws IOException {
                    createNewClass();
                Platform.runLater(()-> {
                    saveLabel.setVisible(true);
                });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    public void onClose(ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    public void whileInputting(){
        saveLabel.setVisible(false);
    }

    private void createNewClass()throws IOException {
        String sql = "insert into classrooms (`Classroom_Name`, `Year_Group_ID`) values (?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, classNameTextField.getText().trim());
            preparedStatement.setInt(2, yearGroupJFXComboBox.getValue().getGroupID());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {


            Utils.printSQLException(e);
        }
    }

    private void getYears()throws IOException{
        String sql = "select Year_Group_ID, Year_Group_Name from year_group";
        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                list.add(new YearGroup(rs.getInt("Year_Group_ID"), rs.getString("Year_Group_Name")));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setUpComboBox(){
        Callback<ListView<YearGroup>, ListCell<YearGroup>> cellFactory =
                new Callback<ListView<YearGroup>, ListCell<YearGroup>>() {

                    @Override
                    public ListCell<YearGroup> call(ListView<YearGroup> l) {
                        return new ListCell<YearGroup>() {

                            @Override
                            protected void updateItem(YearGroup item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getGroupName());
                                }
                            }
                        } ;
                    }
                };

        yearGroupJFXComboBox.setConverter(new StringConverter<YearGroup>() {
            @Override
            public String toString(YearGroup prog) {
                if (prog == null){
                    return null;
                } else {
                    return prog.getGroupName();
                }
            }

            @Override
            public YearGroup fromString(String _prog_) {
                return null;
            }
        });
        yearGroupJFXComboBox.setCellFactory(cellFactory);

    }

    static class YearGroup{
        int groupID;
        String groupName;

        YearGroup(int id, String name){
            this.groupID = id;
            this.groupName = name;
        }

        int getGroupID(){
            return this.groupID;
        }

        String getGroupName(){
            return this.groupName;
        }
    }

}
