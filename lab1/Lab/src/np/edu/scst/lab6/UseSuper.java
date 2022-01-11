package np.edu.scst.lab6;
class Bird {
    String color = "Black";
    void fly(){
        System.out.println("Flying");
    }
}
class Pegion extends Bird {
    String color = "Grey";
    void printColor() {
        System.out.println(color);
        System.out.println(super.color);
    }
    @Override
    void fly() {
        super.fly(); 
    }
}
public class UseSuper {
    public static void main(String[] args) {
        Pegion c = new Pegion();
        c.printColor();
        c.fly();
    }
}
