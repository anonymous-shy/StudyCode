package xyz.shy;

/**
 * Created by Shy on 2016/5/17.
 */
public class TreadTest {

    public static void main(String[] args) {
        MyThread1 mt = new MyThread1();
        Thread t2 = new Thread(new MyThread2());
        t2.start();
        mt.start();
        for (int i = 0; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}

class MyThread1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}

class MyThread2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}
