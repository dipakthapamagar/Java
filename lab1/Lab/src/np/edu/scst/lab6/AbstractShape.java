package np.edu.scst.lab6;
abstract class AS{
    public abstract void run();
}
class Rectangle1 extends AS{
    @Override
    public void run(){
        System.out.println("Rectangle");
    }
}
class Triangle1 extends AS{
    @Override
    public void run(){
        System.out.println("Triangle");
    }
}
public class AbstractShape {
    public static void main(String[] args) {
        Rectangle1 r = new Rectangle1();
        Triangle1 t = new Triangle1();
        r.run();
        t.run();
    }
}
