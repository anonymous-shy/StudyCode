package xyz.shy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Shy on 2018/1/26
 */

public class SingletonMain {

    public static void main(String[] args) {
//        Singleton s1 = Singleton.getInstance();
//        Singleton s2 = Singleton.getInstance();
//        Singleton s3 = Singleton.getInstance();
//        Singleton s4 = Singleton.getInstance();

//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s3);
//        System.out.println(s4);

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> System.out.println(Singleton2.getInstance()));
        }
        threadPool.shutdown();
    }
}
