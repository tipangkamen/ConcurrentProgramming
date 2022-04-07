package ProblemTest;

public class Pro4 {
    static Object object = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (object){
                        System.out.println("threadA start");
                        object.wait();
                        System.out.println("threadA end");
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
                try {
                    synchronized (object){
                        System.out.println("threadB start");
                        object.wait();
                        System.out.println("threadB end");
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                    synchronized (object){
                        System.out.println("threadC start");
                        object.notify();
                        System.out.println("threadC end");
                    }
            }
        });
        threadA.start();
        threadB.start();
        Thread.sleep(1000);
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();


    }
}
