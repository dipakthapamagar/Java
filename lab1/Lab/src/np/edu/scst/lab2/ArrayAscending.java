package np.edu.scst.lab2;

public class ArrayAscending {
    public static void main(String[] args) {
        int ok[]={6,5,7,8,9,4,3,2,1,0};
        int temp = 0;
        System.out.println("Before Sorting:");
        for (int i = 0; i < ok.length; i++) {
            System.out.println(ok[i]);
        }
        for (int i = 0; i < ok.length; i++) {
            for (int j = i+1 ; j < ok.length; j++) {
                if (ok[i]>ok[j]) {
                    temp = ok[i];
                    ok[i]=ok[j];
                    ok[j]=temp;
                }
            }
        }
        System.out.println("Before Sorting:");
        for (int i = 0; i < ok.length; i++) {
            System.out.println(ok[i]);
        }
    }
}

