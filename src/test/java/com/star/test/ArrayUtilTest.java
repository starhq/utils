package com.star.test;

import com.star.collection.ArrayUtil;
import org.junit.Test;

public class ArrayUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数


    @Test
    public void testTrimBySubstring() {
        int[] ints = {1, 2, 3, 4};
        System.out.println(ArrayUtil.toString(ints));


//        Nano.bench().measurements(measurements).threads(threads).measure("hex new version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//            }
//        });
    }

    @Test
    public void testTrimByJDK() {
//        Nano.bench().measurements(measurements).threads(threads).measure("hex old version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//            }
//        });
    }
}
