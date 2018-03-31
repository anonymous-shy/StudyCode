package xyz.shy.java8;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Shy on 2018/3/31
 * ForkJoin 框架实现累加操作
 */

public class ForkJoinDemo extends RecursiveTask<Long> {

    private Long start;
    private Long end;
    private static final Long THRESHOLD = 10000L;

    public ForkJoinDemo(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        if (length <= THRESHOLD) {
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            Long middle = (start + end) / 2;
            ForkJoinDemo left = new ForkJoinDemo(start, middle);
            left.fork();
            ForkJoinDemo right = new ForkJoinDemo(middle + 1, end);
            right.fork();
            return left.join() + right.join();
        }
    }
}

