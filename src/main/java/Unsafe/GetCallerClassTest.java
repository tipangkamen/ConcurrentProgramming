package Unsafe;

import sun.misc.Unsafe;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public class GetCallerClassTest {

    private static GetCallerClassTest getCallerClassTest;

    @CallerSensitive
    public static GetCallerClassTest unsafeTest(){
        /*  要使用getCallerClass()需要在调用的方法上加上 @CallerSensitive ,
        @CallerSensitive还有个特殊之处，必须由 启动类classloader加载（如rt.jar ），才可以被识别。
        所以rt.jar下面的注解可以正常使用。 开发者自己写的@CallerSensitive 不可以被识别。 但是，可以利用jvm参数 -Xbootclasspath/a: path 假装自己的程序是启动类。
        加上vm 启动参数 -Xbootclasspath/a:E:\workspace\ConcurrentProgramming\target\classes
        * */
        Class<?> var0 = Reflection.getCallerClass();
        System.out.println("var0"+var0);
        ClassLoader classLoader = var0.getClassLoader();
        System.out.println("classLoader"+classLoader);
        if (!VM.isSystemDomainLoader(classLoader)) {
            throw new SecurityException("Unsafe");
        } else {
            return getCallerClassTest;
        }
    }

    public static void main(String[] args) {
        GetCallerClassTest getCallerClassTest = unsafeTest();
    }

}

