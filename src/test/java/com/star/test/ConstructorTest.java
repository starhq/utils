package com.star.test;

import com.alisoft.nano.bench.Nano;
import org.junit.Test;
import sun.reflect.misc.ConstructorUtil;

import java.lang.reflect.Constructor;
import java.util.Date;

public class ConstructorTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    private ClassValue<Constructor<?>[]> PROXY = new ClassValue<Constructor<?>[]>() {
        @Override
        protected Constructor<?>[] computeValue(Class<?> type) {
            return type.getDeclaredConstructors();
        }
    };


    @Test
    public void testTrimBySubstring() {
        ConstructorUtil.getConstructors(Date.class);
        ConstructorUtil.getConstructors(Date.class);
//        Nano.bench().measurements(measurements).threads(threads).measure("jdk version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                ConstructorUtil.getConstructors(Date.class);
//            }
//        });
    }

    @Test
    public void testTrimByJDK() {
        Nano.bench().measurements(measurements).threads(threads).measure("classvalue version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
                PROXY.get(Date.class);
            }
        });
    }
}
