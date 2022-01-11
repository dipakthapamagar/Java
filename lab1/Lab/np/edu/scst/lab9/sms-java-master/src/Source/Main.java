package Source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml")); //dataEntryClub
        root.getStylesheets().add(getClass().getResource("styles/loginStyle.css").toExternalForm());// printOutStyle
        primaryStage.setTitle("Log In");
        primaryStage.getIcons().add( new Image("Source/image/app_icon.png"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
