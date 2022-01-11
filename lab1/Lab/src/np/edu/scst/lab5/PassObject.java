package np.edu.scst.lab5;
public class PassObject {
    int a;
    public PassObject(int a){
        this.a = a;
    }
    public static void print(PassObject ob){
        System.out.println("Square of "+ob.a+" is "+ob.a*ob.a);
    }
    public static void main(String[] args) {
        PassObject n1 = new PassObject(4);
        print((n1));
    } 
}

