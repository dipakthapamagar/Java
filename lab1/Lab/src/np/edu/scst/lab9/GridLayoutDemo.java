package np.edu.scst.lab9;
import javax.swing.*;
import java.awt.*;
public class GridLayoutDemo extends JFrame{
    GridLayoutDemo(){
        JLabel jLabel1 = new JLabel("I am first Jlabel");
        JLabel jLabel2 = new JLabel("I am second Jlabel");
        JLabel jLabel3 = new JLabel("I am third Jlabel");
        JLabel jLabel4 = new JLabel("I am forth Jlabel");
        JLabel jLabel5 = new JLabel("I am fifth Jlabel");
        setLayout(new GridLayout(2,3));
        add(jLabel1);
        add(jLabel2);
        add(jLabel3);
        add(jLabel4);
        add(jLabel5);
        setVisible(true);
        setTitle("Flow Layout");
        setSize(500,500);
        setDefaultCloseOperation(2);
    }
    public static void main(String[] args) {
        new GridLayoutDemo();
    }
}
