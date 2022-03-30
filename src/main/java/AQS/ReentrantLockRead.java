package AQS;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockRead {
    public static void main(String[] args) {

        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.lock();


    }
}
