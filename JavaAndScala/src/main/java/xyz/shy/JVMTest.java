package xyz.shy;

/**
 * Created by Shy on 2018/1/18
 */

public class JVMTest {

    public static void main(String[] args) {
//        byte[] b = new byte[4 * 1024 * 1024];
        String s = "Shy";
        String s1 = "yhS";
        System.out.println(s.hashCode());
        System.out.println(s1.hashCode());
    }

}
