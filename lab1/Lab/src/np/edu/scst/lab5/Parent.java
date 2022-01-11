package np.edu.scst.lab5;
public class Parent {
   public Parent(){}
   public void print(){
       System.out.println("Hello from Parent");
   }
    public static void main(String[] args) {
        Parent p1 = new Parent();
        p1.print();
    }
}
