package Source.controllers;

import Source.sharedUtils.Utils;
import Source.sharedUtils.applicationState;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.MaskerPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class assignRemedialCourseController implements Initializable {
    @FXML
    private JFXListView<Students> studentsJFXListView;
    @FXML
    private CheckListView<CoreSubjects> coreSubjectsCheckListView;
    @FXML
    private CheckListView<ElectiveSubjects> electiveSubjectsCheckListView;
    @FXML
    private Pane snackBarPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton saveBtn;

    private MaskerPane maskerPane = new MaskerPane();

    private String previous_selected_student = "";

    private ObservableList<Students> studentsList = FXCollections.observableArrayList();
    private ObservableList<CoreSubjects> coreSubjectsList = FXCollections.observableArrayList();
    private ObservableList<ElectiveSubjects> electiveSubjectsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        stackPane.getChildren().add(maskerPane);
        maskerPane.setVisible(false);

        setStudentListViewFactories();
        setElectiveSubjectsCheckListViewFactory();
        setCoreSubjectsCheckListViewFactory();

        studentsJFXListView.setItems(studentsList);
        electiveSubjectsCheckListView.setItems(electiveSubjectsList);
        coreSubjectsCheckListView.setItems(coreSubjectsList);

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() throws SQLException, IOException {
                getStudents();
                getElectiveSubjects();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void getStudents()throws IOException {
        String sql = "select students.Student_ID, Surname, Other_Names from remedial_student_without_course " +
                "join students " +
                "on remedial_student_without_course.Student_ID = students.Student_ID " +
                "where students.Status = 'active' order by Surname; ";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String id = rs.getString("Student_ID");
                String s_name = rs.getString("Surname");
                String o_name = rs.getString("Other_Names");

                studentsList.add(new Students(id, s_name + " " + o_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void getElectiveSubjects()throws IOException{
        String sql = "select * from remedial_elective_courses";

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Elective_Course_ID");
                String name = rs.getString("Course_Name");

                electiveSubjectsList.add(new ElectiveSubjects(id, name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    private void setStudentListViewFactories(){
        studentsJFXListView.setCellFactory(new Callback<ListView<Students>, ListCell<Students>>() {
            @Override
            public ListCell<Students> call(ListView<Students> param) {
                ListCell<Students> cell = new ListCell<Students>(){
                    @Override
                    protected void updateItem(Students item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getStudentName());
                        }else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void setElectiveSubjectsCheckListViewFactory(){
        electiveSubjectsCheckListView.setCellFactory(lv -> new CheckBoxListCell<ElectiveSubjects>(electiveSubjectsCheckListView::getItemBooleanProperty) {
            @Override
            public void updateItem(ElectiveSubjects elective, boolean empty) {
                super.updateItem(elective, empty);
                setText(elective == null ? "" :  elective.getElectiveName());
            }
        });

        electiveSubjectsCheckListView.getCheckModel().getCheckedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Integer> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (int i : c.getAddedSubList()) {
                            System.out.println(electiveSubjectsCheckListView.getItems().get(i).getElectiveName()+ " selected");
                        }
                    }
                    if (c.wasRemoved()) {
                        for (int i : c.getRemoved()) {
                            System.out.println(electiveSubjectsCheckListView.getItems().get(i).getElectiveName() + " deselected");
                        }
                    }
                }
            }
        });
    }

    private void setCoreSubjectsCheckListViewFactory(){
        coreSubjectsCheckListView.setCellFactory(lv -> new CheckBoxListCell<CoreSubjects>(coreSubjectsCheckListView::getItemBooleanProperty) {
            @Override
            public void updateItem(CoreSubjects core, boolean empty) {
                super.updateItem(core, empty);
                setText(core == null ? "" :  core.getCoreName());
            }
        });

        coreSubjectsCheckListView.getCheckModel().getCheckedIndices().addListener((ListChangeListener<Integer>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (int i : c.getAddedSubList()) {
                        System.out.println(coreSubjectsCheckListView.getItems().get(i).getCoreName()+ " selected");
                    }
                }
                if (c.wasRemoved()) {
                    for (int i : c.getRemoved()) {
                        System.out.println(coreSubjectsCheckListView.getItems().get(i).getCoreName() + " deselected");
                    }
                }
            }
        });
    }

    public void clearElectiveSubjectSelections(){
        electiveSubjectsCheckListView.getCheckModel().clearChecks();
        electiveSubjectsCheckListView.getSelectionModel().clearSelection();
    }

    public void clearCoreSubjectSelections(){
        coreSubjectsCheckListView.getCheckModel().clearChecks();
        coreSubjectsCheckListView.getSelectionModel().clearSelection();
    }

    public void onStudentSelected()throws IOException{
        Students selectedStudent = studentsJFXListView.getSelectionModel().getSelectedItem();

        if (selectedStudent != null){

            if (!previous_selected_student.equals(selectedStudent.getStudentName())){
                coreSubjectsList.clear();

                getCoreSubjectForSelectedStudent(selectedStudent.getStudentID());

                if (coreSubjectsList.size() == 0){
                    Utils.showSnackBar(snackBarPane, "Assign this students a class first", 3000);
                }
                previous_selected_student = selectedStudent.getStudentName();
            }
        }

    }

    private void getCoreSubjectForSelectedStudent(String s_id)throws IOException{
        String sql = "select Remedial_Course_ID, Course_Name from remedial_core_courses where Class_ID in " +
                " (select Class_ID from remedial_students_classrooms where Student_ID = ?) ;"; // and Status = 'active'

        try (Connection connection =  Utils.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int r_id = rs.getInt("Remedial_Course_ID");
                String c_name = rs.getString("Course_Name");

                coreSubjectsList.add(new CoreSubjects(r_id, c_name));
            }

        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
    }

    public void onSave(){
        String studentID = studentsJFXListView.getSelectionModel().getSelectedItem().getStudentID();

        boolean core_was_empty = true;
        boolean elective_was_empty = true;
        // Core subjects
        ObservableList<CoreSubjects> selectedCore = coreSubjectsCheckListView.getCheckModel().getCheckedItems();
        ArrayList<Integer> coreIDs = new ArrayList<>();

        if (selectedCore.size() != 0){
            for (CoreSubjects coreSubject : selectedCore){
                coreIDs.add(coreSubject.getCoreID());
            }
            core_was_empty = false;
        }

        ObservableList<ElectiveSubjects> selectedElective = electiveSubjectsCheckListView.getCheckModel().getCheckedItems();
        ArrayList<Integer> electiveIDs = new ArrayList<>();

        if (selectedElective.size() != 0){
            for (ElectiveSubjects electiveSubject : selectedElective){
                electiveIDs.add(electiveSubject.getElectiveID());
            }
            elective_was_empty = false;
        }

        if (elective_was_empty && core_was_empty){
            Utils.showSnackBar(snackBarPane, "Select at least one subject", 3000);
        }else {
            // For the core subjects
            maskerPane.setVisible(true);
            saveBtn.setDisable(true);
            applicationState.setUIState("disable");


            Task<Void> task = new Task<Void>(){

                @Override
                protected Void call() throws SQLException, IOException {
                    for (int c_id : coreIDs){
                        insertSubjects(studentID, c_id, "core");
                    }

                    for (int c_id : electiveIDs){
                        insertSubjects(studentID, c_id, "elective");
                    }

                    removeStudentsFromFreeRange(studentID);

                    Platform.runLater(()->{
                        clearSelection();
                        Utils.showSnackBar(snackBarPane, "Saved Successfully", 2000);
                        saveBtn.setDisable(false);
                        maskerPane.setVisible(false);
                        applicationState.setUIState("enable");
                    });
                    return null;
                }
            };
            new Thread(task).start();
        }

    }

    private void insertSubjects(String s_id, int c_id, String type)throws IOException{
        String sql = "insert into remedial_student_courses (`Student_ID`, `Course_ID`, `Type`) values (?,?,?)";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, s_id);
            preparedStatement.setInt(2, c_id);
            preparedStatement.setString(3, type);


            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }

    private void removeStudentsFromFreeRange(String id)throws IOException{
        String sql = "delete from remedial_student_without_course where Student_ID = ?";

        try (Connection connection = Utils.connectToDatabase();

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);



            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            Utils.printSQLException(e);
        }
    }


    private void clearSelection(){
        clearElectiveSubjectSelections();
        clearCoreSubjectSelections();

        Students student = studentsJFXListView.getSelectionModel().getSelectedItem();
        studentsList.remove(student);

        studentsJFXListView.getSelectionModel().clearSelection();

        coreSubjectsList.clear();
    }

    public void onGoBack(){
        applicationState.setUIState("classroom_management");
    }

    private static class Students{
        private StringProperty studentID = new SimpleStringProperty();
        private StringProperty studentName = new SimpleStringProperty();

        Students(String id, String name){
            this.studentID.set(id);
            this.studentName.set(name);
        }

        String getStudentID(){
            return this.studentID.get();
        }

        String getStudentName(){
            return this.studentName.get();
        }
    }

    private static class CoreSubjects{
        private IntegerProperty coreID = new SimpleIntegerProperty();
        private StringProperty coreName = new SimpleStringProperty();

        CoreSubjects(int id, String name){
            this.coreID.set(id);
            this.coreName.set(name);
        }

        int getCoreID(){
            return this.coreID.get();
        }

        String getCoreName(){
            return this.coreName.get();
        }
    }

    private static class ElectiveSubjects{
        private IntegerProperty electiveID = new SimpleIntegerProperty();
        private StringProperty electiveName = new SimpleStringProperty();

        ElectiveSubjects(int id, String name){
            this.electiveID.set(id);
            this.electiveName.set(name);
        }

        int getElectiveID(){
            return this.electiveID.get();
        }

        String getElectiveName(){
            return this.electiveName.get();
        }
    }
}
