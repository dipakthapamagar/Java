package np.edu.scst.lab7;
public class ListException {
    public static void main(String[] args) {
        String[] count = {"One","Two","Three","Four","Five","Six","Seven","Eight","Nine"};
        try{
            for (int i = 0; i < count.length+2; i++) {
                System.out.println(count[i]);
            }
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println(e);
        }finally{
            System.out.println("End of program.");
        }
    }
}
