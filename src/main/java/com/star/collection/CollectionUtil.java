package com.star.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * map是否为空
     *
     * @param map map对象
     * @return 是否为空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }


    /**
     * 新建一个ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(final T... values) {
        ArrayList<T> arrayList;
        if (ArrayUtil.isEmpty(values)) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = new ArrayList<>(values.length);
            for (final T value : values) {
                arrayList.add(value);
            }
        }
        return arrayList;
    }
}
