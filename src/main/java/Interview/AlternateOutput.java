package Interview;

import java.util.concurrent.TimeUnit;

/*
 *  两个线程 一个输出ABCDEFG
 *   另一个输出 123456
 * 要求交替输出
 *
 * */
public class AlternateOutput {
    static Thread t1 = null, t2 = null;
    private static final Object LOCK = new Object();

    public static void main(String[] args) {

        t1 = new Thread(() -> {
            synchronized (LOCK) {
                for (int i = 0; i < 26; i++) {
                    System.out.println((char) (i + 65));
                    try {
                        LOCK.notify();
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                LOCK.notify();
            }

        });

        t2 = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    for (int i = 1; i < 27; i++) {
                        System.out.println(i);
                        LOCK.notify();
                        LOCK.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        t1.start();
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("----");

    }
}
