package np.edu.scst.lab4;
public class ThisKeyword {
    String carName;
    public ThisKeyword(String a){
        this.carName = a;
    }
    public static void main(String[] args) {
        ThisKeyword car = new ThisKeyword("Range ROver");
        System.out.println("Name of car is "+car.carName);
    }
}
