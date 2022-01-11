package np.edu.scst.lab3;

public class RectangleArea {
    public static double areaOfRectangle(double l, double b) {
        return l*b;
    }
    public static void main(String[] args) {
        double a=2.21d, b=1.0d;
        System.out.println("The area of rectangle is " + areaOfRectangle(a,b));
    }
}
