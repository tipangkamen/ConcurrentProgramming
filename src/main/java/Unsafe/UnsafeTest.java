package Unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {
    public static void main(String[] args) {
        //这样会抛出SecurityException
     /*   Unsafe unsafe = Unsafe.getUnsafe();
        System.out.println(unsafe);*/
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            System.out.println(theUnsafe);
            theUnsafe.setAccessible(true);
            Unsafe Unsafe = (Unsafe)theUnsafe.get(null);
            System.out.println(Unsafe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
