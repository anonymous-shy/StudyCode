package xyz.shy.thread;

/**
 * Created by Shy on 2016/5/18.
 */
public class TicketTest {

    public static void main(String[] args) {
        Window w = new Window();
        Thread t1 = new Thread(w);
        Thread t2 = new Thread(w);
        Thread t3 = new Thread(w);
        t1.setName("Window1");
        t2.setName("Window2");
        t3.setName("Window3");
        t1.start();
        t2.start();
        t3.start();
    }
}

class Window implements Runnable {
    int ticket = 100;

    @Override
    public void run() {
        while (true) {
            synchronized (this) {//this表示当前对象，即w
                if (ticket > 0) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.err.println(Thread.currentThread().getName() +
                            " Sells,Ticket Num : " + ticket--);
                }
            }
        }
    }
}
