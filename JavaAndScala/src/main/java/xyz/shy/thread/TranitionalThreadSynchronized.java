package xyz.shy.thread;

/**
 * Created by Shy on 2016/5/19.
 */
public class TranitionalThreadSynchronized {

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }) {
        }.start();
    }

    class OutPrint {
        public void outPrint(String name) {
            int length = name.length();
            synchronized (this) {
                for (int i = 0; i < length; i++) {
                    System.err.print(name.charAt(i));
                }
                System.out.println();
            }
        }
    }
}
