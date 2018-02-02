package xyz.shy;

/**
 * Created by Shy on 2018/1/26
 * 懒汉式。有线程安全问题
 */

public class Singleton2 {

    private Singleton2() {
    }

    private static int a;
    private static int b;
    private static volatile Singleton2 instance;

    /**
     * 双重检查加锁
     *
     * @return
     */
    public static Singleton2 getInstance() {
        if (instance == null) {
            synchronized (Singleton2.class) {
                if (instance == null) {
                    a = 1;
                    b = 1;
                    instance = new Singleton2(); //指令重排序

                }
            }
        }
        return instance;
    }
}
