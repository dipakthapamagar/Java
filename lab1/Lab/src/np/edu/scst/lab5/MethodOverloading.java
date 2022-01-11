package np.edu.scst.lab5;
public class MethodOverloading {
    int l, b;
    public void Area(int l, int b){
        System.out.println("Area of rectange is "+l*b);
    }
    public void Area(int l){
        System.out.println("Area of square is "+l*l);
    }
    public static void main(String[] args) {
        MethodOverloading a= new MethodOverloading();
        a.Area(2,6);
        a.Area(3);
    }

    void say() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
