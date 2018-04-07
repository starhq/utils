package com.star.test;

import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class InstantUtilTest {

    @Test
    public void test() {
        LocalDate localDate = LocalDate.of(1998, 4, 7);
        System.out.println(localDate.until(LocalDate.now(), ChronoUnit.YEARS));
    }
}