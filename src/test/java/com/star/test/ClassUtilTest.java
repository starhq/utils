package com.star.test;

import com.star.clazz.ClassUtil;
import com.star.collection.array.ArrayUtil;
import org.junit.Test;

public class ClassUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    @Test
    public void test() {
        System.out.println(ArrayUtil.toString(ClassUtil.getJavaClassPaths()));
//        Nano.bench().measurements(measurements).threads(threads).measure("caset version version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//
//            }
//        });
    }
}
