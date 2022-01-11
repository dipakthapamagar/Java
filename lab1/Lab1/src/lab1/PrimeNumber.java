package lab1;

public class PrimeNumber {
    public static void main(String[] args) {
        int i,a=7,b=0,f=0;
        b=a/2;
        if (a==0||a==1) {
            System.out.println(a+" is not prime number.");
        }
        else
        {
            for(i=2;i<=b;i++){
                if(a%i==0){
//                    System.out.println(a+" is not prime number.");
                    f=1;
                    break;
                }
            }
            if (f==0) {
                System.out.println(a+" is prime number.");
            }
            else{
                System.out.println(a+" is not prime number.");
            }
        }
    }
}
