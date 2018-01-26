package xyz.shy;

/**
 * Created by Shy on 2018/1/26
 * 恶汉式。没有线程安全性问题
 */

public class Singleton {

    // 私有化构造方法
    private Singleton() {
    }

    private static Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }


}
