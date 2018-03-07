package com.star.test;

import com.star.io.FastByteArrayOutputStream;
import org.junit.Test;

import java.io.IOException;

public class FastByteArrayOutputStreamTest {

    @Test
    public void test() throws IOException {
        FastByteArrayOutputStream faos = new FastByteArrayOutputStream();
        byte[] bytes = "helloworld".getBytes();
        faos.write(bytes);
        System.out.println(faos.toString());
    }
}
