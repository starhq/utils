package com.star.io.serializer;

import com.star.clazz.ClassUtil;

import java.util.Objects;

/**
 * 序列化工具类
 *
 * @author j2cache
 */
public final class SerializationUtils {

    /**
     * 序列化接口
     */
    private static Serializer serializer;

    private SerializationUtils() {
    }

    /**
     * 确定序列化的具体实现
     *
     * @param name 选哪种实现，如果自定义，name输入全限定路径的类名
     */
    public static void init(final String name) {
        switch (name) {
            case "java":
                serializer = new JavaSerializer();
                break;
            case "fst":
                serializer = new FSTSerializer();
                break;
            case "kryo":
                serializer = new KryoSerializer();
                break;
            case "kryo-pool":
                serializer = new KryoPoolSerializer();
                break;
            default:
                serializer = ClassUtil.newInstance(name);
                break;
        }
    }

    /**
     * 针对不同类型做单独处理
     *
     * @param obj 待序列化的对象
     * @return 返回序列化后的字节数组
     */
    public static byte[] serialize(final Object obj) {
        return Objects.isNull(obj) ? new byte[0] : serializer.serialize(obj);
    }

    /**
     * 反序列化
     *
     * @param bytes 待反序列化的字节数组
     * @return 序列化后的对象
     */
    public static Object deserialize(final byte[] bytes) {
        return serializer.deserialize(Objects.requireNonNull(bytes));
    }

}
