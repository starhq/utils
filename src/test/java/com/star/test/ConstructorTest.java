package com.star.test;

import com.alisoft.nano.bench.Nano;
import org.junit.Test;

import java.util.Date;

public class ConstructorTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数


    @Test
    public void testTrimBySubstring() {
        Nano.bench().measurements(measurements).threads(threads).measure("jdk version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
                try {
                    Date.class.getDeclaredConstructor(String.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testTrimByJDK() {
        Nano.bench().measurements(measurements).threads(threads).measure("classvalue version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
                com.star.reflect.ConstructorUtil.getConstructor(Date.class, String.class);
            }
        });
    }
}
