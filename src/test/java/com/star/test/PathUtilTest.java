package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.io.CharsetUtil;
import com.star.io.file.PathUtil;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    Path path = Paths.get("I:\\d3d\\123.txt");
    File file = new File("I:\\d3d");


    @Test
    public void testNioEmpty() {
        System.out.println(PathUtil.readString(path, CharsetUtil.CHARSET_UTF_8));
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
