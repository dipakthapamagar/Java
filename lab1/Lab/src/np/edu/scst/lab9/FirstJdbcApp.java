package np.edu.scst.lab9;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class FirstJdbcApp {
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/SCST_LAB9","root","");
        Statement stm = con.createStatement();
        String select = "SELECT * FROM avengers WHERE planet='Earth'";
        ResultSet rs = stm.executeQuery(select);
        while(rs.next()){
            System.out.print("ID="+rs.getString("id")+"\t");
            System.out.print("Name="+rs.getString("name")+"\t");
            System.out.print("Super_power="+rs.getString("super_power")+"\t");
            System.out.println("Planet="+rs.getString("planet")+"\t");
        }
        con.close();
    }
}
