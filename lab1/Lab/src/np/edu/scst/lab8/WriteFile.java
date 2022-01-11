package np.edu.scst.lab8;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
public class WriteFile {
     public static void main(String[] args) throws FileNotFoundException, IOException {
         try (FileWriter writing = new FileWriter("D:/lab 8/lab-write.txt")) {
             String hello = "Hello world; this is written using character stream";
             char[] c = hello.toCharArray();
             for (int j = 0; j < c.length; j++) {
                 writing.write(c[j]);
             }
             System.out.println("lab-Write.txt is changed");
         }
         catch(FileNotFoundException e){
             System.out.println("File not found");
         }
    }
}
