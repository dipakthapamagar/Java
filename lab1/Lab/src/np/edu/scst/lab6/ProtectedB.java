package np.edu.scst.lab6;

public class ProtectedB extends ProtectedA{

    @Override
    protected void print() {
        System.out.println("I am overriding protectedA print method");
        super.print(); 
    }
    
    public static void main(String[] args) {
        ProtectedB a = new ProtectedB();
        a.print();
    }
    
}
