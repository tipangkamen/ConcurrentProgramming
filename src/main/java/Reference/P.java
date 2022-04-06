package Reference;


import java.io.IOException;
import java.lang.ref.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class P {
    private String name;

    public P(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() {
        System.out.println("垃圾回收了");

    }
}

class StrongReferenceTest {
    public static void main(String[] args) throws IOException {
        P p = new P("name");
        p = null;
        System.gc();
        LockSupport.park();
    }
}

class WeakReferenceTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        P p = new P("name");
        WeakReference<P> pWeakReference = new WeakReference<>(p);
        //没有这个不会回收是因为P p 是个强引用，可以直接将new写在WeakReference里面，就不需要这个
        /*p=null;*/
        System.out.println(pWeakReference.get());
        System.gc();
        Thread.sleep(500);
        System.out.println(pWeakReference.get());


        ThreadLocal<P> threadLocal = new ThreadLocal<>();
        threadLocal.set(p);
        p = null;
        threadLocal.remove();
    }
}

class SoftReferenceTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        SoftReference<byte[]> softReference = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(softReference.get());
        System.gc();
        Thread.sleep(5000);
        System.out.println(softReference.get());
        byte[] bytes1 = new byte[1024 * 1024 * 15];
        System.out.println(softReference.get());
        System.out.println(bytes1);
    }
}

class PhantomReferenceTest {
    public static void main(String[] args) throws Exception {
        List<Object> LIST = new LinkedList<>();
        ReferenceQueue<P> QUEUE = new ReferenceQueue<>();
        PhantomReference<P> phantomReference = new PhantomReference<>(new P("name"), QUEUE);

        new Thread(() -> {
            while (true) {
                LIST.add(new byte[1024 * 1024]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(phantomReference.get());
            }
        }).start();

        new Thread(() -> {
            while (true) {
                Reference<? extends P> poll = QUEUE.poll();
                if (poll != null) {
                    System.out.println("--- 虚引用对象被jvm回收了 ---- " + poll);
                }
            }
        }).start();

        Thread.sleep(500);

    }
}


