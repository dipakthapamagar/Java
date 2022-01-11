package np.edu.scst.lab4;
public class ClassCircleOverloading {
    int x,y,radius=4;
    public ClassCircleOverloading(){}
    public ClassCircleOverloading(int x,int y){
        this.x=x;
        this.y=y;
    }
    public ClassCircleOverloading(int x,int y,int r){
        this.x=x;
        this.y=y;
        this.radius=r;
    }
    public void display(){
        System.out.println("Center is ("+x+","+y+")");
        System.out.println("Radius is "+radius);
    }
    public static void main(String[] args) {
        ClassCircleOverloading c = new ClassCircleOverloading(3,4,5);
        ClassCircleOverloading c1 = new ClassCircleOverloading(3,4);
        c.display();
        c1.display();
    }
}
