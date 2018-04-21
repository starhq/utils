package com.star.test;


import com.star.string.StringUtil;
import org.junit.Test;

/**
 * Created by win7 on 2017/5/7.
 */
public class StringTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    private static final String STR = "abc";
    private static final String PRE = "a";

    @Test
    public void testTrimBySubstring() {
        System.out.println(StringUtil.str(1));

//        Nano.bench().measurements(measurements).threads(threads).measure("string regionMatches", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                StringUtil.startWith(STR, PRE, true);
//            }
//        });
    }

    @Test
    public void testTrimByJDK() {
//        Nano.bench().measurements(measurements).threads(threads).measure("string startwith", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                startWith(STR, PRE, true);
//            }
//        });
    }

    public static boolean startWith(CharSequence str, CharSequence prefix, boolean isIgnoreCase) {
        if (isIgnoreCase) {
            return str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
        } else {
            return str.toString().startsWith(prefix.toString());
        }
    }
}
