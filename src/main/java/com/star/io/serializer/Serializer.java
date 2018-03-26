package com.star.io.serializer;

/**
 * 序列化接口
 *
 * @author j2cache
 */
public interface Serializer {
    /**
     * 用的哪种序列化方式
     */
    String name();

    /**
     * 序列化
     *
     * @param obj 对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes 字节数组
     * @return 对象
     */
    Object deserialize(byte[] bytes);
}
