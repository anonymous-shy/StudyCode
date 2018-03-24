package xyz.shy.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Shy on 2018/3/23
 */

public class TestLock {
    public static void main(String[] args) {
        Ticket t = new Ticket();
        new Thread(t, "No.1").start();
        new Thread(t, "No.2").start();
        new Thread(t, "No.3").start();
    }
}

class Ticket implements Runnable {

    private int ticket = 100;

    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                if (ticket > 0) {
                    Thread.sleep(100);
                    System.out.println(Thread.currentThread().getName() + "完成售票,余票: " + --ticket);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
