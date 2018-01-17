package xyz.shy;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shy on 2016/5/19.
 */
public class TraditionalTimerTest {

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.err.println("Timer Test");
            }
        }, 5000, 3000);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
