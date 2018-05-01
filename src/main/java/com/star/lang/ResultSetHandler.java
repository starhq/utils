package com.star.lang;


import java.sql.ResultSet;

/**
 * 结果集处理器
 *
 * @author dbutil
 */
public interface ResultSetHandler<T> {

    /**
     * 处理结果集
     * <p>
     * 常用场景将rs转为pojo
     *
     * @param resultSet 结果集
     * @return 返回对象
     */
    T handle(ResultSet resultSet);
}
