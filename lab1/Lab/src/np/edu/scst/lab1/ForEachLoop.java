package np.edu.scst.lab1;

public class ForEachLoop {
    public static void main(String[] args) {
        float i[]= new float[10];
        for (int j = 0; j < i.length; j++) {
            i[j]=(float)j+1; 
        }
        for(float a : i){
            System.out.println(a);
        }
    }
}
