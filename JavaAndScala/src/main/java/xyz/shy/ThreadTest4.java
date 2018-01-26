package xyz.shy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Shy on 2018/1/24
 * Executor 线程池
 */

public class ThreadTest4 {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0;i < 10 ;i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
        threadPool.shutdown();
    }
}
