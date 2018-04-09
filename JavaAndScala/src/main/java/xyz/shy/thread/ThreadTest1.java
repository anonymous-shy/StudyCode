package xyz.shy.thread;

/**
 * Created by Shy on 2018/1/24
 */

public class ThreadTest1 {

    public static void main(String[] args) {
//        new Thread() {
//            @Override
//            public void run() {
//                System.out.println(getName() + " Start...");
//            }
//        }.start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName() + " Start...");
//            }
//        }).start();
//
//        new Thread(() -> System.out.println(Thread.currentThread().getName() + " Start...")) {
//        }.start();

        MyRunnable runnable = new MyRunnable();
        new Thread(runnable).start();
        new Thread(runnable).start();
        runnable.run();
    }
}

class MyRunnable implements Runnable {

    private Integer tickets = 5;

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            if (tickets > 0)
                System.out.println("ticket = " + tickets--);
        }
    }
}