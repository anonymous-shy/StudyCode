package xyz.shy.java8;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Created by Shy on 2018/3/27
 */

public class LambdaTest {


    @Test
    public void test1() {
        Runnable r0 = new Runnable() {
            @Override
            public void run() {
                System.out.println("r0 run...");
            }
        };
        r0.run();
        Runnable r1 = () -> System.out.println("r1 run...");
        r1.run();
    }

    /**
     * 语法二: 一个参数,无返回值
     * (x) -> System.out.println(x)
     * 若只有一个参数,左侧 () 可以不添加
     */
    @Test
    public void test2() {
        Consumer<String> c = x -> System.out.println(x);
        c.accept("xyz");
    }

    /**
     * 语法三: 有两个以上参数,有返回值
     * lambda 体中有多条语句
     * (x,y) -> {return Integer.compare(x, y);}
     * lambda 体中只有一条语句
     * (x,y) -> Integer.compare(x, y)
     */
    @Test
    public void test3() {
        Comparator<Integer> com = (x, y) -> {
            System.out.println("x=" + x + ", y=" + y);
            return Integer.compare(x, y);
        };
    }

    /**
     *
     */

    public void op(Long l1, Long l2, MyFunc<Long, String> myFunc) {
        System.out.println(myFunc.getValue(l1, l2));
    }

    @Test
    public void test4() {
        op(101L, 123L, (x, y) -> String.valueOf(x + y));
    }
}
