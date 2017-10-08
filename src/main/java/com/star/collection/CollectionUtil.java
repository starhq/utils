package com.star.collection;

import java.util.ArrayList;

/**
 * 集合工具类
 */
public final class CollectionUtil {

    private CollectionUtil() {
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
