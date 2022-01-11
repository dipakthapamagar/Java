package np.edu.scst.lab8;
public class DeadLock {
    public static void main(String[] args) {
        String resource1 = "Item 1";
        String resource2 = "Item 2";
        Thread staff1 = new Thread("Staff1") {
            @Override
            public void run() {
                synchronized (resource1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(resource1 + " is locked by " + Thread.currentThread().getName());
                    synchronized (resource2) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        System.out.println(resource2 + " is locked by " + Thread.currentThread().getName());
                    }
                }
            }
        };
        Thread staff2 = new Thread("Staff2") {
            @Override
            public void run() {
                synchronized (resource2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(resource2 + " is locked by " + Thread.currentThread().getName());
                    synchronized (resource1) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        System.out.println(resource1 + " is locked by " + Thread.currentThread().getName());
                    }
                }
            }
        };
        staff1.start();
        staff2.start();
    }
}
