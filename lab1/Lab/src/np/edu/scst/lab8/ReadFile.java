package np.edu.scst.lab8;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class ReadFile {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try (FileInputStream reading = new FileInputStream("D:/lab 8/lab-read.txt")) {
            int i = 0;
            while ((i = reading.read()) != -1) {
                System.out.print((char) i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
}
