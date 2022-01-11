package np.edu.scst.lab7;
public class DivisionException {
    public static void main(String[] args) {
        int a=120, b=0;
        try{
            System.out.println(a/b);
        }catch(ArithmeticException e){
            System.out.println(e);
        }finally{
            System.out.println("Thankyou! End Of Program.");
        }
    }
}
