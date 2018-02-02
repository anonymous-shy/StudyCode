package xyz.shy;

/**
 * Created by Shy on 2018/1/24
 */

public class ThreadTest1 {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + " Start...");
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " Start...");
            }
        }).start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + " Start...")) {
        }.start();
    }
}
