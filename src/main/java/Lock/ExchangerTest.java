package Lock;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerTest {

    public static void main(String[] args) {
        /*
        *   如果exchanger中只有一个线程，或者说一个线程一直没交换，那么另一个线程就会一直等待
        * */
        Exchanger<String> stringExchanger = new Exchanger<>();
        new Thread(()->{
            String result = "s1";
            System.out.println(result);
            String exchange = null;
            try {
                Thread.sleep(1000);
                exchange = stringExchanger.exchange(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+exchange);
        },"s1").start();

        new Thread(()->{
            String result = "s2";
            System.out.println(result);
            String exchange = null;
            try {
                exchange = stringExchanger.exchange(result,10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+exchange);
        },"s2").start();
    }
}
