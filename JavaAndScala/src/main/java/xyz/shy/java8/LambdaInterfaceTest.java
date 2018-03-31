package xyz.shy.java8;

import org.junit.Test;

import java.util.*;
import java.util.function.*;

/**
 * Created by Shy on 2018/3/28
 */

public class LambdaInterfaceTest {

    // Consumer<T> 消费型接口
    public void ConsumerDemo(double money, Consumer<Double> consumer) {
        consumer.accept(money);
    }

    // Supplier<T> 消费型接口
    public List<Integer> SupplierDemo(int num, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    // Function<T, R> 函数式接口
    public String FunctionDemo(String str, Function<String, String> function) {
        return function.apply(str);
    }

    public List<String> Predicate(List<String> list, Predicate<String> predicate) {
        List<String> strList = new ArrayList<>();
        for (String s : list) {
            if (predicate.test(s))
                strList.add(s);
        }
        return strList;
    }

    @Test
    public void test1() {
        ConsumerDemo(1000, (m) -> System.out.println("Consumer: " + m));
        List<Integer> list = SupplierDemo(10, () -> new Random().nextInt(100));
        for (Integer num : list)
            System.out.println(num);
        System.out.println(FunctionDemo("AnonYmous", (s) -> s.toUpperCase()));
        List<String> asList = Arrays.asList("Shy", "Emma", "Taylor", "Dilraba");
        List<String> predicateList = Predicate(asList, s -> s.length() > 3);
        for (String s : predicateList)
            System.out.println(s);
    }

    @Test
    public void test2() {
        // 对象::实例方法名
        Consumer<String> c0 = s -> System.out.println(s);
        Consumer<String> c1 = System.out::println;
        c0.accept("AA");
        c1.accept("aa");

        Supplier<Integer> s0 = () -> new Random().nextInt(100);
        Supplier<Integer> s1 = new Random()::nextInt;
        System.out.println(s0.get());
        System.out.println(s1.get());
        // 类::静态方法名
        Comparator<Integer> com0 = (x, y) -> Integer.compare(x, y);
        Comparator<Integer> com1 = Integer::compare;
        // 类::实例方法名
        BiPredicate<String, String> bp = (x, y) -> x.equals(y);
        BiPredicate<String, String> bp1 = String::equalsIgnoreCase;
    }

    @Test
    public void test3() {
        Supplier<Emp> sup1 = () -> new Emp();
        Supplier<Emp> sup2 = Emp::new;
        Emp emp = sup2.get();
        System.out.println(emp);

        Function<Integer, Emp> f1 = (i) -> new Emp(i);
        Function<Integer, Emp> f2 = Emp::new;
        Emp emp1 = f2.apply(101);
        System.out.println(emp1);

        BiFunction<Integer, String, Emp> biFunction = Emp::new;
        Emp emp2 = biFunction.apply(101, "SHY");
        System.out.println(emp2);
    }

}
