public class Pro6 {
    private   String str="a";

    public void lock() {
        System.out.println("进来了");
        synchronized (str) {
            int i = 0;
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + i++);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        Pro6 pro6 = new Pro6();
        strSync strSync = new strSync();
        new Thread(pro6::lock, "t1").start();
        new Thread(strSync::lock, "t2").start();
    }



}

class strSync{
    private   String str2= new String("a");

    public void lock() {
        System.out.println("进来了2");
        synchronized (str2) {
            int i = 0;
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + i++);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}