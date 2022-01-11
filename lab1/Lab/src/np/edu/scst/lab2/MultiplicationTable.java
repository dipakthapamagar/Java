package np.edu.scst.lab2;

public class MultiplicationTable {
    public static void multiplicationTable(int a){
        System.out.println("The multiplication table of "+ a + " is:");
        for (int i = 1; i <=10; i++) {
            System.out.println(a +"*"+ i + "="+ a*i);
        }
    }
    public static void main(String[] args) {
        int n=9;
        multiplicationTable(n);
    }
}
