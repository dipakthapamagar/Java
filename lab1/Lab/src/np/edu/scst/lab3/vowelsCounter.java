package np.edu.scst.lab3;

public class vowelsCounter {
    public static int countVowels(String s) {
        int counter = 0;
        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if ( s.charAt(i)=='a' || s.charAt(i)=='e'|| s.charAt(i)=='i'|| s.charAt(i)=='o'|| s.charAt(i)=='u') {
                counter+=1;
            }
        }
        return  counter;
    }
    public static void main(String[] args) {
        String phrase = "My name is Dipak Thapa Magar";
        String phrase1 = phrase.toLowerCase();
        System.out.println("The number of Vowels in phrase, " + phrase + ", is " + countVowels(phrase1));
    }
    
}
