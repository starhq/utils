package com.star.test;

import com.star.time.DateTimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class InstantUtilTest {

    @Test
    public void test() {
        System.out.println(DateTimeUtil.getEndOfMonth(LocalDateTime.now()));
    }
}