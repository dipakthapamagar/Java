package np.edu.scst.lab7;
public class VoteException {
    public static void main(String[] args) {
        int age = 10;
        try{
            if(age>=18){
                System.out.println("You can vote.");
            }
            else{
            throw new Exception("You cannot vote.");
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            System.out.println("End of program.");
        }
    }
}
