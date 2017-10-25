package com.star.lang;

import java.util.Collection;

/**
 * 遍历数据的处理
 *
 * @param <T> 输入泛型
 * @param <R> 返回值泛型
 */
public interface ItemsProcessor<T, R> {

    /**
     * 处理
     *
     * @param instances 需要处理的数据
     * @param <T>       输入泛型
     * @param <R>       返回值泛型
     * @return 返回值
     */
    <T, R> R process(Collection<T> instances);
}
