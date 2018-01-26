package xyz.shy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Shy on 2018/1/24
 * 实现带返回值的线程
 */

public class ThreadTest2 implements Callable<String>{
    @Override
    public String call() throws Exception {
        System.out.println("I'm coming...");
        Thread.sleep(1000);
        System.out.println("mission Complete...");
        return "Shy";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadTest2 tt2 = new ThreadTest2();
        FutureTask<String> task = new FutureTask<>(tt2);
        Thread t = new Thread(task);
        t.start();
        String s = task.get();
        System.out.println(s);
    }
}
