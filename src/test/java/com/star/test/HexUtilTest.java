package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.string.HexUtil;
import org.junit.Test;

public class HexUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数

    String str = "7d";

    @Test
    public void testTrimBySubstring() {
//        System.out.println(HexUtil.decode(str.toCharArray())[0]);
//        System.out.println(hex2Byte(str.toUpperCase())[0]);

        Nano.bench().measurements(measurements).threads(threads).measure("hex new version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
                HexUtil.decode(str.toCharArray());
            }
        });
    }

    @Test
    public void testTrimByJDK() {
        Nano.bench().measurements(measurements).threads(threads).measure("hex old version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
                hex2Byte(str.toUpperCase());
            }
        });
    }

    public static byte[] hex2Byte(final String str) {
        final int length = str.length() / 2;
        final char[] hexChars = str.toCharArray();
        byte[] out = new byte[length];
        for (int i = 0; i < length; i++) {
            final int pos = i * 2;
            out[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return out;
    }

    private static byte charToByte(final char data) {
        return (byte) "0123456789ABCDEF".indexOf(data);
    }
}
