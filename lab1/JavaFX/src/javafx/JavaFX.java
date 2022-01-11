package javafx;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JavaFX extends Application {

    Stage window;
    BorderPane layout;
    Button button,button1;

    public static void main(String[] args) {
        launch(args);
    }
    
    private static void configureFileChooser(
        final FileChooser fileChooser2) {
        fileChooser2.setTitle("Filter");
        fileChooser2.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser2.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Dipak's Tutorial");

        FileChooser fileChooser = new FileChooser();
        FileChooser fileChooser1 = new FileChooser();
        FileChooser fileChooser2 = new FileChooser();
        fileChooser.setTitle("Open Project");
        fileChooser1.setTitle("Save as");

        Menu fileMenu = new Menu("_File");
        Menu editMenu = new Menu("_Edit");
        Menu helpMenu = new Menu("_Help");
        Menu levelMenu = new Menu("_Level");

        //fileMenu menuitem
        MenuItem newProject = new MenuItem("New Project...");
        newProject.setOnAction(e -> System.out.println("Creating new project..."));
        fileMenu.getItems().add(newProject);

        MenuItem newFile = new MenuItem("New File...");
        newFile.setOnAction(e -> System.out.println("Creating new file..."));
        fileMenu.getItems().add(newFile);

        fileMenu.getItems().add(new SeparatorMenuItem());
        
        MenuItem save = new MenuItem("Save");
        fileMenu.getItems().add(save);
        
        TextArea txtarea = new TextArea();
        save.setOnAction(ActionEvent->{
            FileChooser fc = new FileChooser();
//        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("txt files", "*.txt");
save.setOnAction(e -> fileChooser1.showSaveDialog(window));
        File savefile = fc.showSaveDialog(null);
        try{
            FileWriter fw = new FileWriter(savefile);
            fw.write(txtarea.getText());
            fw.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        });
        
        fileMenu.getItems().add(new SeparatorMenuItem());

        MenuItem openProject = new MenuItem("Open Project...");
        openProject.setOnAction(e -> fileChooser.showOpenDialog(window));
        fileMenu.getItems().add(openProject);

        Menu openRecent = new Menu("Open Recent File");
        fileMenu.getItems().add(openRecent);

        MenuItem file1 = new MenuItem("file1");
        MenuItem file2 = new MenuItem("file2");
        MenuItem file3 = new MenuItem("file3");
        MenuItem file4 = new MenuItem("file4");

        openRecent.getItems().addAll(file1, file2, file3, file4);

        fileMenu.getItems().add(new SeparatorMenuItem());

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exit);

        //editMenu  menuitem
        editMenu.getItems().add(new MenuItem("Copy"));
        editMenu.getItems().add(new MenuItem("Cut"));

        MenuItem paste = new MenuItem("Paste");
        paste.setOnAction(e -> System.out.println("Pasting..."));
        paste.setDisable(true);
        editMenu.getItems().add(paste);

        //helpMenu checkmenuitem
        CheckMenuItem showLine = new CheckMenuItem("Show Line Number");
        showLine.setOnAction(e -> {
            if (showLine.isSelected()) {
                System.out.println("Line is showing");
            } else {
                System.out.println("Line is hiding");
            }
        });

        CheckMenuItem autoSave = new CheckMenuItem("Auto Save");
        autoSave.setSelected(true);
        helpMenu.getItems().addAll(autoSave, showLine);

        //levelMenu radiomenuitem
        ToggleGroup levelToggle = new ToggleGroup();

        RadioMenuItem easy = new RadioMenuItem("Easy");
        RadioMenuItem medium = new RadioMenuItem("Medium");
        RadioMenuItem hard = new RadioMenuItem("Hard");

        easy.setToggleGroup(levelToggle);
        medium.setToggleGroup(levelToggle);
        hard.setToggleGroup(levelToggle);

        levelMenu.getItems().addAll(easy, medium, hard);

        //menubar 
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu, levelMenu);

        layout = new BorderPane();
        layout.setTop(menuBar);
        button = new Button("Click me");
        
        configureFileChooser(fileChooser2);
        button1 = new Button("Open Picture with filter");
        button1.setOnAction(e -> fileChooser2.showOpenDialog(window));
        
 
        
        
        Tooltip tooltip = new Tooltip();
        tooltip.setText(
                "\nEven you click nothing will happen ;P\n"
        );
//        button.setTooltip(tooltip);
//        VBox layout1 = new VBox();
//        layout1.setSpacing(50);
//        layout1.getChildren().addAll(layout, button, button1);
//        Scene scene = new Scene(layout1, 800, 400);
//        window.setScene(scene);
//        window.show();
        GridPane gp = new GridPane();
        gp.add(menuBar, 0, 0);
        gp.add(txtarea, 0, 1);
        Group gr = new Group();
        gr.getChildren().add(gp);
        Scene scene = new Scene(gr);
        Stage st = new Stage();
        st.setScene(scene);
        st.show();
    }

}
