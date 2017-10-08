package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.collection.ArrayUtil;
import com.star.string.HexUtil;
import com.star.string.StringUtil;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by win7 on 2017/5/7.
 */
public class StringTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    private String str = "AB";


    @Test
    public void testTrimBySubstring() {


//        Nano.bench().measurements(measurements).threads(threads).measure("custom", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//            }
//        });
    }

    @Test
    public void testTrimByJDK() {
        Nano.bench().measurements(measurements).threads(threads).measure("jdk", () -> {
            for (int i = 0; i < SerialTimes; i++) {
            }
        });
    }


}
