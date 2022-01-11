package np.edu.scst.lab8;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
public class DeserializeObject {
    public static void main(String[] args) {
        Mobile m;
      try {
         FileInputStream fileIn = new FileInputStream("D:/lab 8/Serialize.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         m = (Mobile) in.readObject();
         in.close();
         fileIn.close();
      } catch (IOException i) {
         i.printStackTrace();
         return;
      } catch (ClassNotFoundException c) {
         System.out.println("Mobile class not found");
         c.printStackTrace();
         return;
      }
      System.out.println("Deserialized Mobile...");
      System.out.println("Name: " + m.name);
      System.out.println("Model: " + m.model);
      System.out.println("SSN: " + m.SSN);
    }
}
