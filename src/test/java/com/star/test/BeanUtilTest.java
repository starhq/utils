package com.star.test;


import org.junit.Test;

public class BeanUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数


    @Test
    public void testCache() {
//        Nano.bench().measurements(measurements).threads(threads).measure("test cache version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                BeanUtil.getSimpleProperty(demo, "name");
//            }
//        });
    }

    @Test
    public void test() {
//        Nano.bench().measurements(measurements).threads(threads).measure("test normal version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//            }
//        });
    }

}
