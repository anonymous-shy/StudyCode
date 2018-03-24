package xyz.shy.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Shy on 2018/3/22
 * CountDownLatch : 闭锁，在完成某些运算时，只有其他的所有线程全部完成，当前运算才继续执行；
 */

public class TestCountDownLatch {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        LatchDemo latchDemo = new LatchDemo(countDownLatch);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(latchDemo).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start));
    }
}

class LatchDemo implements Runnable {

    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                for (int i = 0; i <= 100; i++) {
                    if (i % 2 == 0)
                        System.out.println(i);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + " : " + latch.getCount());
                latch.countDown();
            }
        }
    }
}
