package Source.sharedUtils;

import com.jfoenix.controls.JFXSnackbar;
import javafx.animation.FadeTransition;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

public class Utils {
    private static final int CANCEL = 2;
    private static final int RETRY = 1;


    public static int YES = 1;
    private static int NO = 0;


    public static Connection connectToDatabase() throws SQLException, IOException {

        Connection connection = null;

        try(FileInputStream f = new FileInputStream("3ix57J89Ej.properties")) {
            // load the properties file
            Properties pros = new Properties();
            pros.load(f);

            // assign db parameters
            String url       = pros.getProperty("url");
            String user      = pros.getProperty("user");
            String password  = pros.getProperty("password");
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void printSQLException(SQLException ex) throws IOException {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


    public static boolean isEmailValid(String email){
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);

    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static boolean validatePassword(String incomingPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(incomingPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static int confirmationDialog(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add( new Image("Source/image/app_icon.png"));

        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");


        alert.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == btnYes){
            return YES;
        }else {
            return NO;
        }
    }

    public static void setFadeInTransition(Node node){
        FadeTransition ft = new FadeTransition(Duration.millis(1500), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void convertDate(DatePicker datePicker){
        datePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
//                datePicker.setPromptText(pattern.toLowerCase());
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    public static boolean validateInputAsNumber(String input){
        String regex = "\\d{0,9}([.]\\d{0,4})?";
        return input.matches(regex);
    }

    public static void showSnackBar(Pane snackBarPane, String message, int time){
        JFXSnackbar snackBar = new JFXSnackbar(snackBarPane);
        Duration timeout = new Duration(time);
        PseudoClass pseudo = PseudoClass.getPseudoClass("nothing");
        Label info = new Label(message);
        info.setStyle("-fx-text-fill: WHITE;" +
                "-fx-background-color: black;" +
                "-fx-border-radius: 5px");
        Insets padding = new Insets(10, 20, 10, 20);
        info.setPadding(padding);

        snackBar.enqueue(new JFXSnackbar.SnackbarEvent(info, timeout, pseudo));
    }

    public static int generateErrorDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred!");
        alert.setContentText("Check your network connection and retry");
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add( new Image("Source/image/app_icon.png"));

        ButtonType btnRetry = new ButtonType("Retry");
        ButtonType btnCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(btnRetry, btnCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == btnRetry){
            return RETRY;
        }else {
            return CANCEL;
        }
    }

}
