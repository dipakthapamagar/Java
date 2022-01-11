package np.edu.scst.lab8;
public class ThreadRunnable implements Runnable{
   @Override
   public void run(){
        ThreadRunnable.print();
    }
      public static void print(){
         for(int i =0; i<5; i++){
            System.out.println("Name: "+Thread.currentThread().getName()+" Priority: "+Thread.currentThread().getPriority());
            try{Thread.sleep(1000); }catch(InterruptedException e){}
        }
    }
    public static void main(String[] args) {
        ThreadRunnable a1 = new ThreadRunnable();
        Thread t1 = new Thread(a1);
        t1.setPriority(3);
        Thread t2 = new Thread(a1);
        t2.setPriority(10);
        Thread t3 = new Thread(a1);
        t3.setPriority(1);
        t1.start();
        t2.start();
        t3.start();
    }
}
