package xyz.shy;

/**
 * Created by Shy on 2018/1/30
 */

public class Singleton3 {
    static class SingletonHolder {
        static Singleton3 instance = new Singleton3();
    }

    public static Singleton3 getInstance() {
        return SingletonHolder.instance;
    }
}
