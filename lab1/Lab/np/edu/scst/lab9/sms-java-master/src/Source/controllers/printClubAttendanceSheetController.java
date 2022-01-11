package Source.controllers;

import Source.sharedUtils.Utils;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.tableview2.TableColumn2;
import org.controlsfx.control.tableview2.TableView2;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class printClubAttendanceSheetController implements Initializable {
    @FXML
    private JFXComboBox<Clubs> clubsJFXComboBox;
    @FXML
    private Label headingLabel;
    @FXML
    private StackPane tableContainer;
    @FXML
    private AnchorPane printLayout;

    private TableView2<TableData> tableView = new TableView2<>();


    private ObservableList<Clubs> clubList = FXCollections.observableArrayList();
    private ObservableList<TableData> tableData = FXCollections.observableArrayList();
    private ObservableList<TableData> tempData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchClubs();
                return null;
            }
        };
        new Thread(task).start();

        setClubsJFXComboBox();
    }

    public void onClubSelected(){
        Clubs clubs = clubsJFXComboBox.getValue();
        if (clubs != null){
            String club_name = clubs.getClubName().toUpperCase();
            String header = "ATTENDANCE SHEET";
            headingLabel.setText(club_name + " " + header);

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    fetchStudentsInClub(clubs.getClubID());

                    Platform.runLater(()-> {
                        setTableData();
                        drawTable();
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private void drawTable(){
        tableContainer.getChildren().clear();
        tableView.getItems().clear();
        tableView.getColumns().clear();

        tableView.setColumnResizePolicy(TableView2.CONSTRAINED_RESIZE_POLICY);

        TableColumn2<TableData, String> mainColumn =
                new TableColumn2<>("Day:                                                      Date:");
        TableColumn2<TableData, String> numberColumn = new TableColumn2<>("No.");
        TableColumn2<TableData, String> studentColumn = new TableColumn2<>("Student Name");
        TableColumn2<TableData, String> classColumn = new TableColumn2<>("Class");
        TableColumn2<TableData, String> signInColumn = new TableColumn2<>("Sign In Time");
        TableColumn2<TableData, String> signOutColumn = new TableColumn2<>("Sign Out time");
        TableColumn2<TableData, String> signatureColumn = new TableColumn2<>("Signature");
        mainColumn.getColumns().addAll(numberColumn, studentColumn, classColumn, signInColumn, signOutColumn, signatureColumn);

        numberColumn.setCellValueFactory(p-> p.getValue().getNumber());
        studentColumn.setCellValueFactory(param -> param.getValue().getName());
        classColumn.setCellValueFactory(param -> param.getValue().getClassName());

        tableView.getColumns().add(mainColumn);


        tableView.getItems().addAll(tableData);
        tableContainer.getChildren().add(tableView);
    }

    private void fetchStudentsInClub(String club_id) throws IOException {
        String sql = "select Surname, Other_Names, Classroom_Name " +
                "from students_clubs " +
                "join students " +
                "on students_clubs.Student_ID = students.Student_ID " +
                "join student_classroom " +
                "on students_clubs.Student_ID = student_classroom.Student_ID " +
                "join classrooms " +
                "on student_classroom.Classroom_ID = classrooms.Classroom_ID " +
                "where Club_ID = ? " +
                "and students_clubs.Status = 'active' and students.Status = 'active' order by Surname;";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, club_id );
            ResultSet rs = preparedStatement.executeQuery();

            int count = 1;

            while (rs.next()) {
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");
                String class_name = rs.getString("Classroom_Name");

                tempData.add(new TableData(String.valueOf(count), s_name + " " + o_name, class_name));

                ++count;
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setTableData(){
        tableData.clear();
        tableData.addAll(tempData);
        tempData.clear();
    }

    private void fetchClubs() throws IOException {
        String sql = "select * from clubs";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Club_ID");
                String name = rs.getString("Club_Name");

                clubList.add(new Clubs(id, name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setClubsJFXComboBox(){
        Callback<ListView<Clubs>, ListCell<Clubs>> cellFactory =
                new Callback<ListView<Clubs>, ListCell<Clubs>>() {

                    @Override
                    public ListCell<Clubs> call(ListView<Clubs> l) {
                        return new ListCell<Clubs>() {

                            @Override
                            protected void updateItem(Clubs item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getClubName());
                                }
                            }
                        };
                    }
                };

        clubsJFXComboBox.setConverter(new StringConverter<Clubs>()

        {
            @Override
            public String toString (Clubs _class){
                if (_class == null) {
                    return null;
                } else {
                    return _class.getClubName();
                }
            }

            @Override
            public Clubs fromString (String _class_){
                return null;
            }
        });
        clubsJFXComboBox.setCellFactory(cellFactory);
        clubsJFXComboBox.setItems(clubList);
    }

    public void onPrintBtnClicked(ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        ObservableList<TableData> copy_of_data = FXCollections.observableArrayList();
        copy_of_data.addAll(tableData);

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout =
                printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, 5, 5, 5, 1);
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        job.getJobSettings().setPageLayout(pageLayout);

        if (job != null) {
            boolean showDialog = job.showPageSetupDialog(window);
            if (showDialog) {

                clubsJFXComboBox.setVisible(false);
//                printLayout.setScaleX(0.8);
                printLayout.setScaleY(0.9);
                printLayout.setTranslateX(-30);
                printLayout.setTranslateY(-80);


                ObservableList<TableData> tempList = FXCollections.observableArrayList();

                while (copy_of_data.size() > 0) {
                    int count = 0;
                    tempList.clear();

                    for (TableData sheetData : copy_of_data) {
                        if (count >= 20) break;
                        tempList.add(sheetData);
                        ++count;
                    }
                    tableView.getItems().clear();
                    copy_of_data.removeAll(tempList);
                    tableView.getItems().addAll(tempList);

                    boolean success = job.printPage(printLayout);
                    if (!success) {
                        job.endJob();
                        tableView.getItems().clear();
                        tableView.getItems().addAll(tableData);
                        printLayout.setScaleY(1.0);
                        printLayout.setTranslateX(0);
                        printLayout.setTranslateY(0);
                        clubsJFXComboBox.setVisible(true);
                    }
                }

                job.endJob();
                tableView.getItems().clear();
                tableView.getItems().addAll(tableData);
                printLayout.setScaleY(1.0);
                printLayout.setTranslateX(0);
                printLayout.setTranslateY(0);
                clubsJFXComboBox.setVisible(true);
            }
        }
    }

    private static class Clubs{
        StringProperty clubID = new SimpleStringProperty();
        StringProperty clubName = new SimpleStringProperty();

        Clubs(String id, String name){
            this.clubID.set(id);
            this.clubName.set(name);
        }

        String getClubID(){
            return this.clubID.get();
        }

        String getClubName(){
            return this.clubName.get();
        }
    }

    private static class TableData{
        StringProperty number = new SimpleStringProperty();
        StringProperty name = new SimpleStringProperty();
        StringProperty className = new SimpleStringProperty();

        TableData(String no, String nm, String cl){
            number.set(no);
            name.set(nm);
            className.set(cl);
        }

        private StringProperty getName(){
            return name;
        }

        private StringProperty getNumber(){
            return number;
        }

        private StringProperty getClassName(){
            return className;
        }
    }

}
