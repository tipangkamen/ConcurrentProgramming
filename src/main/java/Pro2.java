
public class Pro2 {
    static Object object = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("threadA 进入");
                synchronized (object){
                    System.out.println("threadA 持有obj对象");
                    object.wait();
                    Thread.sleep(10000);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadA.start();
        threadA.interrupt();
        while (true){
            Thread.sleep(1000);
            System.out.println("1");
        }
    }
}
