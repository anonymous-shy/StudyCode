package xyz.shy;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shy on 2018/1/24
 * 定时器 Timer Quartz
 */

public class ThreadTest3 {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("TimerTask() running...");
            }
        },0,1000);
    }
}
