package np.edu.scst.lab2;

public class AreaCircle {
    public static float areaCircle(float r){
        float PI=3.14f;
        return PI*r*r;
    }
    public static void main(String[] args) {
        float radius=9f;
        System.out.println("Area of circle with radius "+ radius +" is: " + areaCircle(radius));
    }
}
