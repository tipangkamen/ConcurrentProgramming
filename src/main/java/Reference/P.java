package Reference;


import java.io.IOException;
import java.util.WeakHashMap;
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


