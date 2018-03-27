package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.collection.array.ArrayUtil;
import com.star.math.ArithmeticUtil;
import org.junit.Test;

import java.math.BigDecimal;

public class ArithmeticUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    int[] array = {1, 2, 3};


    @Test
    public void testTrimBySubstring() {
        System.out.println(ArithmeticUtil.add(2, new BigDecimal("1"), null, new BigDecimal("3")));
//        Class<?> clazz = ClassUtil.getComponentType(array);
//        System.out.println(clazz);
//        Nano.bench().measurements(measurements).threads(threads).measure("caset version version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//
//            }
//        });
    }

    @Test
    public void testTrimByJDK() {
        Nano.bench().measurements(measurements).threads(threads).measure("normal version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
                ArrayUtil.isEmpty(array);
            }
        });
    }
}
