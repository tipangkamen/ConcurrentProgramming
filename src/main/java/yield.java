
public class yield {
    public static void main(String[] args) {


        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("-------------");
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        });


        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("11111111111");
            }
        });

        Thread thread2 = new Thread(() -> {

            System.out.println("2222222222");
            /*for (int i = 0; i < 5; i++) {
                System.out.println("222222222:i"+i);
                if (i>2){

                }
            }*/
            /*try {
                System.out.println("2222222");
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        });

        /* thread1.start();*/
        thread1.setName("thread1");
        thread2.setName("thread2");
        thread1.start();
        thread2.start();
        System.out.println("--------------");
        // 主线程要等待thread1,thread2返回结果，不会再执行
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--------------");

       /* thread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("aaaaaaaaaa");
        }*/

    }
}

