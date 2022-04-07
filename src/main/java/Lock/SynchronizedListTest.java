package Lock;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

class SynchronizedList<T> {

    private final ArrayList<T> arrayList = new ArrayList();

    public void add(T t) {
        arrayList.add(t);
    }

    public int size() {
        return arrayList.size();
    }

}

/*
* 实现一个容器，提供一两个方法 ，add，size
 *   写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
* */

public class SynchronizedListTest {
    public static void main(String[] args) {
        SynchronizedList<Integer> synchronizedList = new SynchronizedList();
        final Object lock = new Object();
        /*
         *   之所以t1唤醒t2之后要自己wait是因为notify方法不会立刻释放锁，而是要等t1执行完了才会释放锁，因此t1还是拿到锁的，t2被唤醒之后，还是无法执行
         *
         * */
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                while (true) {
                    if (synchronizedList.size() == 5) {
                        System.out.println("t2---");
                        break;
                    }
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.notify();
                }
            }
        });

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    System.out.println("add" + i);
                    synchronizedList.add(i);
                    if (synchronizedList.size() == 5) {
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();


    }
}

class SynchronizedListTest2 {
    /*
     * 使用countDownLatch来完成
     * */
    public static void main(String[] args) {
        SynchronizedList<Integer> synchronizedList = new SynchronizedList();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("add" + i);
                synchronizedList.add(i);
                if (synchronizedList.size() == 5) {
                    try {
                        countDownLatch.countDown();
                        countDownLatch2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        Thread t2 = new Thread(() -> {
            while (true) {
                if (synchronizedList.size() == 5) {
                    System.out.println("t2---");
                    countDownLatch2.countDown();
                    break;
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.start();
    }
}

class SynchronizedListTest3 {
    /*
     * 使用LockSupport来完成,使用这个不需要t2先执行
     * */

    static Thread t1 = null, t2 = null;

    public static void main(String[] args) {
        SynchronizedList<Integer> synchronizedList = new SynchronizedList();

        t2 = new Thread(() -> {
            while (true) {
                LockSupport.park();
                LockSupport.unpark(t1);
                System.out.println("t2---");
                break;
            }
        });

        t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("add" + i);
                synchronizedList.add(i);
                if (synchronizedList.size() == 5) {
                    LockSupport.unpark(t2);
                    LockSupport.park(t1);
                }
            }

        });

        t1.start();
        t2.start();
    }
}


