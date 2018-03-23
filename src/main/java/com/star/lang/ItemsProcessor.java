package com.star.lang;

import java.util.Collection;

/**
 * 遍历数据的处理
 *
 * @param <T> 输入泛型
 * @param <R> 返回值泛型
 * @author starhq
 */
@FunctionalInterface
public interface ItemsProcessor<R, T> {

    /**
     * 处理
     *
     * @param instances 需要处理的数据
     * @return 返回值
     */
    R process(Collection<T> instances);
}
