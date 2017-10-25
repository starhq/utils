package com.star.lang;

/**
 * 遍历数据的处理
 *
 * @param <T> 输入泛型
 * @param <R> 返回值泛型
 */
public interface ItemProcessor<T, R> {

    /**
     * 处理
     *
     * @param instance 需要处理的数据
     * @param <T>      输入泛型
     * @param <R>      返回值泛型
     * @return 返回值
     */
    <T, R> R process(T instance);
}
