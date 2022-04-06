package AQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockRead {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock reentrantLock = new ReentrantLock();

        Thread thread1 = new Thread(() -> {
            reentrantLock.lock();
            reentrantLock.unlock();
        }
        );
        thread1.start();
        TimeUnit.SECONDS.sleep(1);

        Thread thread2 = new Thread(() -> reentrantLock.lock());
        thread2.start();



        new Thread(()->reentrantLock.lock()).start();
    }
}
