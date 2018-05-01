package com.star.test;


import com.star.id.Snowflake;
import org.junit.Test;

public class SnowflakeTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    @Test
    public void testSnowflake() {
        Snowflake snowflake = new Snowflake(0, 0, true);

//        Nano.bench().measurements(measurements).threads(threads).measure("snowflake", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                System.out.println(snowflake.nextId());
//            }
//        });
    }

//    @Test
//    public void testNessUUid() {
//        Nano.bench().measurements(measurements).threads(threads).measure("nessuuid", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                NessUUID.toString(UUID.randomUUID());
//            }
//        });
//    }
//
//    @Test
//    public void testJDKUUid() {
//        Nano.bench().measurements(measurements).threads(threads).measure("jdkid", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                UUID.randomUUID().toString();
//            }
//        });
//    }
}
