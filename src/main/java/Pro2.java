import com.sun.org.apache.bcel.internal.generic.NEW;

public class Pro2 {
    static Object object = new Object();
    public static void main(String[] args) {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("threadA 进入");
                    synchronized (object){
                        System.out.println("threadA 持有obj对象");
                        object.wait();
                        Thread.sleep(1000);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadA.start();
        threadA.interrupt();
    }
}
