import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {
    /*
    *
    * LockSupport 的unpark可以先执行，之后再调用一次park也不会停止，但是如果调用多次就会停止 ,它应该是维护了一个park的数量
    *
    * */
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i==5){
                    LockSupport.park();
                    LockSupport.park();
                    LockSupport.park();

                }
            }
        });
        thread.start();
        LockSupport.unpark(thread);

        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------");
        LockSupport.unpark(thread);


    }
}
