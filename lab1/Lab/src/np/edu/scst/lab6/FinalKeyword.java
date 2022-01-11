package np.edu.scst.lab6;
class Bike {
    final String color = "Red";
    final void run(){
        System.out.println("Running.");
    }
}
final class Honda extends Bike {
    void printColor() {
        System.out.println(color);
    }
}
public class FinalKeyword {
    public static void main(String[] args) {
        Honda h = new Honda();
        h.printColor();
        h.run();
    }
}
