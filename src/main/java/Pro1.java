

public class Pro1 {
    private static final Object resourceA =new Object();
    private static final Object resourceB =new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread threadA = new Thread(() -> {
            try {
                //获取A共享资源的监视器锁
                synchronized (resourceA){
                    System.out.println("threadA get resourceA lock");
                    //获取B共享资源的监视器锁,A资源释放
                    synchronized (resourceB){
                        System.out.println("threadA get resourceB lock");
                        resourceA.wait();
                        System.out.println("threadA release resourceA lock");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000);
                synchronized (resourceA){
                    System.out.println("threadB get resourceA lock");
                    System.out.println("threadB  try get resourceB lock...");

                    synchronized (resourceB){
                        System.out.println("threadB get resouceA lock");
                        System.out.println("threadB release resourA lock");
                        resourceA.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadB.start();
        threadA.start();

        threadA.join();
        threadB.join();
        System.out.println("main over");
    }
}
