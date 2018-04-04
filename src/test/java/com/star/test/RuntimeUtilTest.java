package com.star.test;

import com.star.io.CharsetUtil;
import com.star.runtime.RuntimeUtil;
import org.junit.Test;


public class RuntimeUtilTest {

    @Test
    public void test() {
        System.out.println(RuntimeUtil.execForStr(CharsetUtil.CHARSET_GBK, "java -version"));
    }

}