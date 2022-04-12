package Unsafe;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CounterEffectTest {
    public static int NUM_OF_THREADS = 1000;
    public static int NUM_OF_INCREMENTS = 100000;

    public static void main(String[] args) {

        SimpleCounter simpleCounter = new SimpleCounter();
        SyncCounter syncCounter = new SyncCounter();
        ReentrantLockCounter reentrantLockCounter = new ReentrantLockCounter();
        AtomicCounter atomicCounter = new AtomicCounter();
        CASCounter casCounter = null;
        try {
            casCounter = new CASCounter();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StatisticsTime(simpleCounter);
        StatisticsTime(syncCounter);
        StatisticsTime(reentrantLockCounter);
        StatisticsTime(atomicCounter);
        StatisticsTime(casCounter);

    }

    public static void StatisticsTime(Counter counter) {
        Thread[] threads = new Thread[NUM_OF_THREADS];
        long before = System.currentTimeMillis();

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            threads[i] = new Thread(() -> new CounterClient(counter, NUM_OF_INCREMENTS).run());
            threads[i].start();
        }

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long after = System.currentTimeMillis();
        System.out.println("Counter result: " + counter.getCounter());
        System.out.println("Time passed in ms:" + (after - before));
    }
}


interface Counter {
    void increment();

    long getCounter();
}

class CounterClient implements Runnable {
    private final Counter counter;
    private final int num;

    public CounterClient(Counter counter, int num) {
        this.counter = counter;
        this.num = num;
    }

    @Override
    public void run() {
        for (int i = 0; i < num; i++) {
            counter.increment();
        }
    }
}

class SimpleCounter implements Counter {
    private long counter = 0;

    @Override
    public void increment() {
        counter++;
    }

    @Override
    public long getCounter() {
        return counter;
    }
}

class SyncCounter implements Counter {
    private long counter = 0;

    @Override
    public synchronized void increment() {
        counter++;
    }

    @Override
    public long getCounter() {
        return counter;
    }
}

class ReentrantLockCounter implements Counter {
    private long counter = 0;
    private final ReentrantReadWriteLock.WriteLock writeLock = new ReentrantReadWriteLock().writeLock();

    @Override
    public void increment() {
        try {
            writeLock.lock();
            counter++;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public long getCounter() {
        return counter;
    }
}

class AtomicCounter implements Counter {
    private final AtomicLong counter = new AtomicLong(0L);

    @Override
    public void increment() {
        counter.incrementAndGet();
    }

    @Override
    public long getCounter() {
        return counter.get();
    }
}

class CASCounter implements Counter {
    private final long counter = 0;
    private Unsafe unsafe;
    private final long offset;

  /*  public CASCounter() throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        unsafe = (Unsafe)theUnsafe.get(null);
        offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));
    }*/

    /*
    *  使用getUnsafe()需要加启动VM参数 -Xbootclasspath/a:E:\workspace\ConcurrentProgramming\target\classes
    *   但是这两种构造器最后累加所花费时间都很长  atomicCounter 花费 1992 ,而CASCounter要 13116 原因还不知道
    * */
    public CASCounter() throws Exception {
        unsafe = Unsafe.getUnsafe();
        offset = unsafe.objectFieldOffset(CASCounter.class.getDeclaredField("counter"));
    }

    @Override
    public void increment() {
        long before = counter;
        while (!unsafe.compareAndSwapLong(this, offset, before, before + 1)) {
            before = counter;
        }
    }

    @Override
    public long getCounter() {
        return counter;
    }

}

