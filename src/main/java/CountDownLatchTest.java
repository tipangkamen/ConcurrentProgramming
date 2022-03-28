import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    public static void main(String[] args) {

        usingCountDownLatch();
        /*   usingJoin();*/
    }

    public static void usingCountDownLatch() {
        Thread[] threads = new Thread[3];
        CountDownLatch countDownLatch = new CountDownLatch(threads.length);

        System.out.println("count"+countDownLatch.getCount());
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> System.out.println("thread" + Thread.currentThread().getName() + "运行完毕"));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            countDownLatch.countDown();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("所有线程运行完毕");
    }

    public static void usingJoin() {
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> System.out.println("thread---join" + Thread.currentThread().getName() + "运行完毕"));
        }

        for (int i = 0; i < threads.length - 1; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length - 1; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("所有线程运行完毕");

    }
}
