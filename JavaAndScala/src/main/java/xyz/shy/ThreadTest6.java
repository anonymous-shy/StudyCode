package xyz.shy;

/**
 * Created by Shy on 2018/1/25
 * synchronized 解决线程安全问题
 */

public class ThreadTest6 {

    public static void main(String[] args) {
        Seq s = new Seq();
        new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " " + s.getNext());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " " + s.getNext());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class Seq {
    private int v;

    synchronized int getNext() {
        return v++;
    }
}