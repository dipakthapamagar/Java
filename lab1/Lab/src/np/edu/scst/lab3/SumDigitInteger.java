package np.edu.scst.lab3;

public class SumDigitInteger {
    public static int sumOfDigit(int x) {
        int sum=0;
        while (x!=0) {
            sum+=x%10;
            x/=10; 
        }
        return sum;
    }
    public static void main(String[] args) {
        int digit=123456789;
        System.out.println("The sum of digits in an integer is " + sumOfDigit(digit));
    }
}
