public class Pro5 {
    /*
    *   synchronized方法调用时，可以同时调用调用非synchronized方法吗
    * */
    public synchronized void lock()  {
        //此时的synchronized 相当于 synchronized（Pro5.class）
        System.out.println("locking.....");
        try {
            Thread.sleep(10000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void noLock()  {
        for (int i = 0; i < 5; i++) {
            System.out.println("nolock...");
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Pro5 pro5 = new Pro5();
        new Thread(pro5::lock,"t1").start();
        new Thread(pro5::noLock,"t2").start();

    }



}
