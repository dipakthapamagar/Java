package np.edu.scst.lab5;
public class Child extends Parent {
    public Child(){}
    @Override
    public void print(){
        super.print();
        System.out.println("Hello from Child");
    }
    public void ok(){
        System.out.println("Child says I didn't inherit it from you grand parent.");
    }
    public static void main(String[] args) {
        Child c1 = new Child();
        c1.print();
        c1.ok();
    }
}
