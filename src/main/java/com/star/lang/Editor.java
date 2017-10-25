package com.star.lang;

/**
 * 编辑器，修改对象
 *
 * @param <T> 泛型
 */
public interface Editor<T> {

    /**
     * 修改对象
     *
     * @param instance 对象
     * @return 修改后的对象
     */
    T edit(T instance);
}
