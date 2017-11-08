package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.collection.array.ArrayUtil;
import org.junit.Test;

import java.util.Date;

public class ConstructorTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    Date date = new Date();


    @Test
    public void testTrimBySubstring() {
//        System.out.println(ArrayUtil.toString(MethodUtil.getMethods(Date.class)));
        System.out.println(ArrayUtil.toString(Date.class.getMethods()));
//        Nano.bench().measurements(measurements).threads(threads).measure("jdk version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                try {
//                    Date.class.getDeclaredField("fastTime");
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Test
    public void testTrimByJDK() {
        Nano.bench().measurements(measurements).threads(threads).measure("classvalue version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
            }
        });
    }

    private class Demo {

        public void test() {

        }
    }
}

