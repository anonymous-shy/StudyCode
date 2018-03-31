package xyz.shy.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by Shy on 2018/3/29
 */

public class StreamDemo1 {

    @Test
    public void test1() {
        // 创建 Stream
        // 通过Collection系列集合提供的 stream() 或 parallelStream()
        List<String> list = new ArrayList<>();
        Stream<String> stream1 = list.stream();
        // 通过Arrays中的静态方法 stream() 获取数组流
        String[] strArr = new String[10];
        Stream<String> stream2 = Arrays.stream(strArr);
        // 通过 Stream 类中的静态方法 of()
        Stream<String> stream3 = Stream.of("AA", "BB", "CC");
        // 创建无限流
        Stream.iterate(0, (x) -> x + 2)
                .limit(10)
                .forEach(System.out::println);
        Stream.generate(() -> new Random().nextInt(100))
                .limit(10)
                .forEach(System.out::println);
    }

    @Test
    public void test2() {
        // map
        List<String> asList = Arrays.asList("AA", "BB", "CC", "DD");
        asList.forEach(System.out::println);
        asList.stream()
                .map(String::toLowerCase)
                .forEach(System.out::println);
        List<Emp> emps = new ArrayList<>();
        emps.add(new Emp(1, "A"));
        emps.add(new Emp(2, "B"));
        emps.add(new Emp(3, "C"));
        emps.add(new Emp(4, "D"));
        emps.add(new Emp(5, "E"));
        emps.stream()
                .map(Emp::getName)
                .forEach(System.out::println);
    }
}
