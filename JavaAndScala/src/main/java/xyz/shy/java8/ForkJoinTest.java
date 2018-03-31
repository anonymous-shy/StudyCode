package xyz.shy.java8;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinTest {

    @Test
    public void test1() {
        Instant start = Instant.now();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinDemo task = new ForkJoinDemo(0L, 100000000L);
        Long invoke = pool.invoke(task);
        System.out.println(invoke);
        Instant end = Instant.now();
        System.out.println("耗时: " + Duration.between(start, end).toMillis());
    }
}