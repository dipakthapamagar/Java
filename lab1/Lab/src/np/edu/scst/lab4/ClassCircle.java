package np.edu.scst.lab4;
public class ClassCircle {
    int x, y;
    double rad;
    
    public ClassCircle(){}
    public ClassCircle(int x, int y, double r){
        this.x = x;
        this.y = y;
        this.rad = r;
    }
    
    public static void main(String[] args) {
        ClassCircle c2 = new ClassCircle(4,5,5.6d);
        System.out.println("Center is ("+c2.x+","+c2.y+")");
        System.out.println("Radius is "+c2.rad);
    }
}
