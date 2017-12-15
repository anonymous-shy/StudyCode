package xyz.shy;

/**
 * Created by Shy on 2016/5/18.
 */
public class CommunicationTest {

    public static void main(String[] args) {

        PrintNum p = new PrintNum();
        Thread t1 = new Thread(p);
        Thread t2 = new Thread(p);
        t1.setName("甲");
        t2.setName("乙");
        t1.start();
        t2.start();
    }
}

class PrintNum implements Runnable {

    int num = 1;

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                notify();
                if (num <= 100) {
                    try {
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.err.println(Thread.currentThread().getName() + ":" + num);
                    num++;
                } else {
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}