package com.star.test;

import com.alisoft.nano.bench.Nano;
import com.star.collection.CollectionUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtilTest {

    private static int measurements = 100; // 测量次数

    private static int threads = 100; // 线程数

    private static int SerialTimes = 1000; // 每个线程执行序列化次数


    @Test
    public void testCache() {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        list1.add("c");
        list1.add("c");


        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("b");
        list2.add("c");
        list2.add("c");

        System.out.println(CollectionUtil.union(list1, list2));


//        Nano.bench().measurements(measurements).threads(threads).measure("test cache version", () -> {
//            for (int i = 0; i < SerialTimes; i++) {
//                BeanUtil.getSimpleProperty(demo, "name");
//            }
//        });
    }

    @Test
    public void test() {
        Nano.bench().measurements(measurements).threads(threads).measure("test normal version", () -> {
            for (int i = 0; i < SerialTimes; i++) {
            }
        });
    }

}
