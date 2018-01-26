package xyz.shy;

/**
 * Created by Shy on 2016/5/17.
 */
public class TreadTest {

    public static void main(String[] args) throws InterruptedException {
        MyThread1 mt = new MyThread1("MT-1");
//        mt.setDaemon(true);
        Thread t2 = new Thread(new MyThread2());
        mt.start();
        t2.start();
        t2.interrupt();
        for (int i = 0; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}

class MyThread1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public MyThread1(String name) {
        super(name);
    }
}

class MyThread2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}
