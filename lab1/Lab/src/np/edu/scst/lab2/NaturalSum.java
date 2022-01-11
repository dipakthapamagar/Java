package np.edu.scst.lab2;

public class NaturalSum {
    public static void main(String[] args) {
        int n=10, sum = 0;
        System.out.println("The first "+ n +" natural numbers are:");
        for (int i = 1; i <=n; i++) {
            System.out.println(i);
            sum+=i;
        }
        System.out.println("The sum of first "+ n +" natural numbers is "+ sum);
    }
}
