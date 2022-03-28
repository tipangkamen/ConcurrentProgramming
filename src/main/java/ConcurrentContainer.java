import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 *   面试题：  写一个固定容量同步容器，拥有put和get方法，以及getCount方法，能够支持2个生产者线程以及10个消费者线程的阻塞调用
 */

/*
 * 这种写法不太好的地方在于notify是唤醒全部线程，当容器为空时可能还是消费者拿到锁，反之亦然，无法唤醒指定的线程
 * */
public class ConcurrentContainer<T> {

    private final ArrayList<T> arrayList = new ArrayList<>();

    private int count = 0;

    private static final int MAX_SIZE = 10;

    public synchronized Object get() {
        //使用while不是if是因为如果被唤醒了，如果此时生产出来的被其他的消费了，用if不会进行判断，而是会直接进行下一步
        while (arrayList.size() == 0) {
            System.out.println("容器空了,等待生产");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        T t = arrayList.get(0);
        arrayList.remove(t);
        count--;
        //notify一定要写在最后面，因为notify是要等synchronized代码块执行完毕后，才能释放锁，不是立刻释放锁
        this.notifyAll();
        return t;
    }

    public synchronized void put(T t) {
        while (arrayList.size() == MAX_SIZE) {
            System.out.println("容器已满,等待消费");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        arrayList.add(t);
        count++;
        this.notifyAll();
    }

    public Integer getCount() {
        return count;
    }

}

class ConcurrentContainerPlus<T> {

    private final ArrayList<T> arrayList = new ArrayList<>();
    private int count = 0;
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final Condition consumer = reentrantLock.newCondition();
    private final Condition producer = reentrantLock.newCondition();
    private static final int MAX_SIZE = 10;

    public Object get() {
        T t = null;
        try {
            reentrantLock.lock();
            while (arrayList.size() == 0) {
                System.out.println("容器空了,等待生产");
                //消费者线程等待
                consumer.await();
            }
            t = arrayList.get(0);
            arrayList.remove(t);
            count--;
            //唤醒生产者的所有线程
            producer.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        return t;
    }

    public void put(T t) {
        try {
            reentrantLock.lock();
            while (arrayList.size() == MAX_SIZE) {
                System.out.println("容器已满,等待消费");
                producer.await();
            }
            arrayList.add(t);
            count++;
            consumer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public Integer getCount() {
        return count;
    }
}


class monitorTest {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentContainer<Integer> concurrentContainer = new ConcurrentContainer<>();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    concurrentContainer.put(1);
                    System.out.println("放入");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    Object o = concurrentContainer.get();
                    System.out.println("拿出");
                }
            }).start();
        }

    }
}

class monitorPlusTest {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentContainerPlus<Integer> concurrentContainerPlus = new ConcurrentContainerPlus<>();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    concurrentContainerPlus.put(1);
                    System.out.println("放入");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    Object o = concurrentContainerPlus.get();
                    System.out.println("拿出");
                }
            }).start();
        }
    }
}