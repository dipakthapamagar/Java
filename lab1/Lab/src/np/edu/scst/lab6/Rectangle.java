package np.edu.scst.lab6;
public class Rectangle extends Shape{
    @Override
    public void method(){
        System.out.println("Overriding shape for rectangle");
    }
    public static void main(String[] args) {
        Rectangle r = new Rectangle();
        r.print();
        r.method();
    }
}
