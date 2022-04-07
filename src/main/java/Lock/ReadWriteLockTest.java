package Lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        Thread[] reentrantLockS = new Thread[20];
        Thread[] readWriteLockS = new Thread[20];

        long reentrantLockStartTime = System.currentTimeMillis();

        for (int i = 0; i < 18; i++) {
            Thread thread = new Thread(() -> read(reentrantLock), "ReentrantReadLock" + i);
            reentrantLockS[i] = thread;
            thread.start();
        }
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> write(reentrantLock), "ReentrantWriteLock" + i);
            reentrantLockS[i+18] = thread;
            thread.start();
        }
        for (int i = 0; i < 20; i++) {
            try {
                reentrantLockS[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("--------------------------------");

        long reentrantLockEndTime = System.currentTimeMillis();

        for (int i = 0; i < 18; i++) {
            Thread thread = new Thread(() -> read(readWriteLock.readLock()), "ReadWriteLockRead" + i);
            readWriteLockS[i] = thread;
            thread.start();
        }
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> write(readWriteLock.writeLock()), "ReadWriteLockWrite" + i);
            readWriteLockS[i+18] = thread;
            thread.start();
        }
        for (int i = 0; i < 20; i++) {
            try {
                readWriteLockS[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long readWriteLockEndTime = System.currentTimeMillis();

        System.out.println("ReentrantLock花费时间 = "+ (reentrantLockEndTime-reentrantLockStartTime));
        System.out.println("ReadWriteLock花费时间 = "+ (readWriteLockEndTime-reentrantLockEndTime));
    }

    public static void read(Lock lock) {
        lock.lock();
        try {
            Thread.sleep(1000);
            System.out.println("read over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void write(Lock lock) {
        lock.lock();
        try {
            Thread.sleep(1000);
            System.out.println("write over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
