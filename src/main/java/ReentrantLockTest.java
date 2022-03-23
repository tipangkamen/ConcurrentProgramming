import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    private   ReentrantLock reentrantLock = new ReentrantLock();
    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest = new ReentrantLockTest();
        new Thread(reentrantLockTest::t1).start();
        new Thread(reentrantLockTest::t2).start();
    }

    public void t1() {
        boolean b = false;
        try {
            b = reentrantLock.tryLock();
            for (int i = 0; i < 5; i++) {
                System.out.println(i);
                Thread.sleep(1000);
            }
            System.out.println("t1-b" + b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (b) {
                reentrantLock.unlock();
            }
        }
    }

    public void t2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean b = false;
        try {
            b = reentrantLock.tryLock();
            System.out.println("t2-b" + b);
        } finally {
            if (b) {
                reentrantLock.unlock();
            }
        }
    }
}
