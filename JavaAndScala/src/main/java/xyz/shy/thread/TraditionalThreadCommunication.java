package xyz.shy.thread;

/**
 * Created by Shy on 2016/5/19.
 */
public class TraditionalThreadCommunication {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 50; i++)
                    synchronized (TraditionalThreadCommunication.class) {
                        for (int j = 1; j <= 10; j++) {
                            System.err.println("sub thread : " + j + " loops : " + i);
                        }
                    }
            }
        }) {
        }.start();

        for (int i = 1; i <= 50; i++)
            synchronized (TraditionalThreadCommunication.class) {
                for (int j = 1; j <= 10; j++) {
                    System.out.println("main thread : " + j + " loops : " + i);
                }
            }
    }
}
