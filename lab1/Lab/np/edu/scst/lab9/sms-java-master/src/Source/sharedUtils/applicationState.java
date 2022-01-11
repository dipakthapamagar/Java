package Source.sharedUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class applicationState {
    public static StringProperty currentUIState = new SimpleStringProperty("default");

    public static String getUIState() {
        if (currentUIState != null) {
            return currentUIState.get();
        }else {
            return "default";
        }
    }

    public static void setUIState(String state) {
        currentUIState.set(state);
    }

    // assigning remedial and regular student a class using same controller...we nee to know which
    public static boolean isRegular = true;
}
