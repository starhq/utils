package com.star.test;

import com.star.security.digest.DigestUtil;
import org.junit.Test;

public class DigestUtilTest {

    @Test
    public void test() {
        System.out.println(DigestUtil.sha256Hex("helloworld"));
    }

}