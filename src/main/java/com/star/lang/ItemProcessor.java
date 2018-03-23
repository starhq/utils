package com.star.lang;

/**
 * 遍历数据的处理
 *
 * @param <T> 输入泛型
 * @param <R> 返回值泛型
 * @author star
 */
public interface ItemProcessor<R, T> {

    /**
     * 处理
     *
     * @param instance 需要处理的数据
     * @return 返回值
     */
    R process(T instance);
}
