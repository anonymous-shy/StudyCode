package xyz.shy.thread;

/**
 * Created by Shy on 2018/3/16
 */

public class TestAtomicDemo {

    public static void main(String[] args) {
        AtomicDemo ad = new AtomicDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(ad).start();
        }
    }
}

class AtomicDemo implements Runnable {

    private int serialNumber = 0;

    public int getSerialNumber() {
        return serialNumber++;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " : " + getSerialNumber());
    }
}
