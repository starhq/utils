package com.star.collection;

import java.util.Collection;

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




}
