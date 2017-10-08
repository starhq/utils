package com.star.lang;

/**
 * 过滤器
 *
 * @param <T> 泛型
 */
public interface Filter<T> {

    /**
     * 是否通过验证
     *
     * @param instance 需要验证的对象
     * @return 是否符合条件
     */
    boolean accept(T instance);
}
