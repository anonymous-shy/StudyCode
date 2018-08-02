package xyz.shy.json;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Created by Shy on 2018/5/31
 Fastjson API入口类是com.alibaba.fastjson.JSON，常用的序列化操作都可以在JSON类上的静态方法直接完成。
 public static final Object parse(String text); // 把JSON文本parse为JSONObject或者JSONArray
 public static final JSONObject parseObject(String text)； // 把JSON文本parse成JSONObject
 public static final <T> T parseObject(String text, Class<T> clazz); // 把JSON文本parse为JavaBean
 public static final JSONArray parseArray(String text); // 把JSON文本parse成JSONArray
 public static final <T> List<T> parseArray(String text, Class<T> clazz); //把JSON文本parse成JavaBean集合
 public static final String toJSONString(Object object); // 将JavaBean序列化为JSON文本
 public static final String toJSONString(Object object, boolean prettyFormat); // 将JavaBean序列化为带格式的JSON文本
 public static final Object toJSON(Object javaObject); 将JavaBean转换为JSONObject或者JSONArray。

 SerializeWriter：相当于StringBuffer
 JSONArray：相当于List<Object>
 JSONObject：相当于Map<String, Object>

 JSON反序列化没有真正数组，本质类型都是List<Object>

 fastjson还有很多很高级的特性，比如支持注解、支持全类型序列化，这些都是很好的特性，功能强大，不在本次测试范围。
 */

public class FastjsonTest {

    @Test
    public void testJson1() {
        Goods goods = new Goods(1001, "AAA", true, LocalDateTime.now().toString(), 11111L);
        Object parse = JSON.toJSON(goods);
        System.out.println(parse);
    }


}
