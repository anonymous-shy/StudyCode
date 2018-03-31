package xyz.shy.java8;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Shy on 2018/3/31
 */

public class OptionalDemo {

    @Test
    public void test1() {
        Optional<HashMap<String, String>> mapOptional = Optional.of(new HashMap<String, String>());
        HashMap<String, String> hashMap = mapOptional.get();
        System.out.println(hashMap);
    }
}
