import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("发车"));
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                try {
                    int await = barrier.await();
                    System.out.println("await"+Thread.currentThread().getName()+"="+await);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
