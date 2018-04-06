package com.star.test;

import com.star.extra.EscapeUtil;
import org.junit.Test;

public class EscapeUtilTest {

    @Test
    public void test() {
        System.out.println(EscapeUtil.unescape("\u5C06\u5F53\u524D\u5173\u95ED\u52A8\u4F5C\u8BB0\u5F55\u5230\u65E5\u5FD7"));
    }

}