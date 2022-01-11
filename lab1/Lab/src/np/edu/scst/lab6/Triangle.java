package np.edu.scst.lab6;
public class Triangle extends Shape{
    @Override
    public void method(){
        System.out.println("Overriding shape for triangle");
    }
    public static void main(String[] args) {
        Triangle t = new Triangle();
        t.print();
        t.method();
    }
}
