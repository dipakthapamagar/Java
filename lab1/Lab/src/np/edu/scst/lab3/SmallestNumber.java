package np.edu.scst.lab3;

public class SmallestNumber {
    public static int smallNumber(int a, int b, int c) {
        if (a<b & a<c) {
            return a;
        } 
        else if ( b<a & b<c) {
            return b;
        }
        else {
            return c;
        }
    }
    public static void main(String[] args) {
        int a=4, b=1, c=3;
        System.out.println("The smallest number is "+ smallNumber(a,b,c));
    }
}
