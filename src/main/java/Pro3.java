public class Pro3 {
    static Object object = new Object();
    public static void main(String[] args)  {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("--------");
                object.notify();
            }
        });
        threadA.start();
        threadB.start();

    }
}
