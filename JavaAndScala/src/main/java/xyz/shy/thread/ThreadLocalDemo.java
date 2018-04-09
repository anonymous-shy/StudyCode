package xyz.shy.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Shy on 2018/4/3
 */

public class ThreadLocalDemo {

    public static void main(String[] args) {
        TLRunnable runnable = new TLRunnable();
//        new Thread(runnable).start();
//        new Thread(runnable).start();
        System.out.println("--------------------------");
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = threadPool.submit(new TLCallable());
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
    }
}

class TLRunnable implements Runnable {

    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    @Override
    public void run() {
        threadLocal.set(new Random().nextInt(100));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadLocal.get());
    }
}

class TLCallable implements Callable<Integer> {

    private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };
    private ThreadLocal<String> stringThreadLocal = ThreadLocal.withInitial(() -> "AnonYmous");

    @Override
    public Integer call() throws Exception {
        System.out.println("call()方法被自动调用！！！" + Thread.currentThread().getName());
        threadLocal.set(new Random().nextInt(100));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadLocal.get();
    }
}