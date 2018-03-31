package xyz.shy.java8;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Created by Shy on 2018/3/31
 */

public class TimeDemo {

    @Test
    public void test1() {
        Instant utcDateTime = Instant.now(); // UTC时间戳
        ZonedDateTime zonedDateTime = utcDateTime.atZone(ZoneOffset.ofHours(8));
        System.out.println(utcDateTime);
        System.out.println(zonedDateTime);
    }
}
