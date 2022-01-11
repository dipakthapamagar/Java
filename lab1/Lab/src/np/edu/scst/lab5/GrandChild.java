package np.edu.scst.lab5;
public class GrandChild extends Child{
    public void myDay(){
        System.out.println("I am third generation.");
    }
    public static void main(String[] args) {
        GrandChild g1 = new GrandChild();
        g1.print();
        g1.ok();
        g1.myDay();
    }
}
