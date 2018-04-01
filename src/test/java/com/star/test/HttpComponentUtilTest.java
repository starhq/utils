package com.star.test;

import com.alisoft.nano.bench.Nano;
import org.junit.Test;

public class HttpComponentUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数


    @Test
    public void testNioEmpty() {

//        Nano.bench().measurements(measurements).threads(threads).measure("nio dir emtpy", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                PathUtil.loopFiles(path, null, null);
//            }
//        });
    }

    @Test
    public void testFileEmpty() {
        Nano.bench().measurements(measurements).threads(threads).measure("file dir empty", () -> {
            for (int i = 0; i < SerialTimes; i++) {
//                FileUtil.loopFiles(file, null);
            }
        });
    }
}
