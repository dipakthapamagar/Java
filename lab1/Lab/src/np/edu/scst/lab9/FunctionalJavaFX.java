package np.edu.scst.lab9;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class FunctionalJavaFX extends Application {
    @Override
    public void start(Stage arg0) throws Exception{
        MenuBar menubar = new MenuBar();
        Menu file = new Menu("_FILE");
        Menu edit = new Menu("_EDIT");
        Menu format = new Menu("_FORMAT");
        Menu view = new Menu("_VIEW");
        Menu help = new Menu("_HELP");
        MenuItem New = new MenuItem("New");
        MenuItem Open = new MenuItem("Open");
        MenuItem Save = new MenuItem("Save");
        MenuItem Save_as = new MenuItem("Save as");
        file.getItems().addAll(New,Open,Save,Save_as);
        menubar.getMenus().addAll(file,edit,format,view,help);
        TextArea txtarea = new TextArea();
        txtarea.setMaxHeight(600);
        txtarea.setMaxWidth(600);
        Save.setOnAction(ActionEvent->{
            FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("txt files", "*.txt");
        File savefile = fc.showSaveDialog(null);
        try{
            FileWriter fw = new FileWriter(savefile);
            fw.write(txtarea.getText());
            fw.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        });
        GridPane gp = new GridPane();
        gp.add(menubar, 0, 0);
        gp.add(txtarea, 0, 1);
        Group gr = new Group();
        gr.getChildren().add(gp);
        Scene sc = new Scene(gr);
        Stage st = new Stage();
        st.setTitle("Dipak's Tutorial Notepad clone");
        st.setScene(sc);
        st.setMaxWidth(600);
        st.setMaxHeight(600);
        st.show();
    }
    public static void main(String[] args) {
    Application.launch(args);
  }
}
