package np.edu.scst.lab8;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
class Mobile implements Serializable{
    String name, model;
    int SSN;
}
public class SerializeObject {
    public static void main(String[] args) {
        Mobile m = new Mobile();
        m.name = "Samsung";
        m.model = "S-series";
        m.SSN = 9;
        try {
            FileOutputStream fileOut
                    = new FileOutputStream("D:/lab 8/Serialize.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(m);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in D:/lab 8/Serialize.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
