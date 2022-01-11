package np.edu.scst.lab3;

public class middleCharOfString {
    public static void midCharOfStr(String str) {
        int len = str.length();
        int mid=len/2;
        System.out.println(mid);
        System.out.println("The middle character of string "+ str + " is " + str.charAt(mid));
    }
    public static void main(String[] args) {
        String str="Dipaki";
        midCharOfStr(str);
    }
}
