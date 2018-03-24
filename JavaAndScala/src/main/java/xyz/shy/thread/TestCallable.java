package xyz.shy.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Shy on 2018/3/22
 * 创建执行线程方式三：实现Callable接口，相较于实现Runnable接口的方式，方法可以有返回值，并且可以抛出异常。
 * 执行 Callable 方式，需要执行 FutureTask 实现类的支持，用于接收运算结果。
 */

public class TestCallable {
    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        // 1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收返回结果。
        FutureTask<Integer> result = new FutureTask<>(td);
        new Thread(result).start();
        // 2.接收线程运算后的结果
        try {
            Integer sum = result.get(); //FutureTask 也可以用于闭锁
            System.out.println("sum = " + sum);
            System.out.println("-----");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class ThreadDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            System.out.println(i);
            sum += i;
        }
        return sum;
    }
}
