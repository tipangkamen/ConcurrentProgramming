import java.util.concurrent.locks.ReentrantLock;

public class test3 extends Thread{
    private static ReentrantLock lock=new ReentrantLock(true); //参数为true表示为公平锁，请对比输出结果
    public void run() {
        for(int i=0; i<30; i++) {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName()+"获得锁");
            }finally{
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) {
        test3 rl=new test3();
        Thread th1=new Thread(rl);
        Thread th2=new Thread(rl);
        th1.start();
        th2.start();
    }
}
