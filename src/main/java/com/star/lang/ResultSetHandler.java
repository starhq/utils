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
     */
    T handle(ResultSet resultSet);
}
