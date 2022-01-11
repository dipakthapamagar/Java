package np.edu.scst.lab2;

public class SwitchCase {
    public static void main(String[] args) {
        int a = 90;
        if(a>100)
        {
            System.out.println("Percentage cannot be higher than 100");
        }
        else{
            switch (a/10){
                case 10:
                case 9:
                case 8:
                    System.out.println("Distinction");
                    break;
                case 7:
                case 6:
                    System.out.println("First");
                    break;
                case 5:
                    System.out.println("Second");
                    break;
                case 4:
                    System.out.println("Thire");
                    break;
                default:
                    System.out.println("Fail");
            }     
        }
    }
}

