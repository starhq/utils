package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.reflect.MethodUtil;
import org.junit.Test;

import java.util.Date;

public class ConstructorTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    Date date = new Date();


    @Test
    public void testTrimBySubstring() {
        System.out.println(MethodUtil.getMethod(Date.class, "before", Date.class));
        System.out.println(MethodUtil.getPublicMethod(Date.class, "before", Date.class));
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

}

