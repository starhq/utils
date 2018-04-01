package com.star.object;

import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.io.serializer.JavaSerializer;
import com.star.io.serializer.Serializer;
import com.star.reflect.MethodUtil;
import com.star.string.StringUtil;
import com.sun.xml.internal.ws.util.UtilException;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Object工具类
 *
 * @author starhq
 */
public final class ObjectUtil {

    private ObjectUtil() {
    }


    /**
     * 克隆对象
     *
     * @param obj 源
     * @param <T> 泛型
     * @return 新对象
     */
    public static <T> T clone(final T obj) {
        T result = ArrayUtil.clone(obj);
        if (null == result) {
            if (obj instanceof Cloneable) {
                final Method cloneMethod = MethodUtil.getMethod(obj.getClass(), "clone").orElseThrow(ToolException::new);
                result = MethodUtil.invoke(obj, cloneMethod);
            } else {
                result = cloneByStream(obj);
            }
        }
        return result;
    }

    /**
     * 返回克隆后的对象，如果克隆失败，返回原对象
     *
     * @param <T> 对象类型
     * @param obj 对象
     * @return 克隆后或原对象
     */
    public static <T> T cloneIfPossible(final T obj) {
        T clone = null;
        try {
            clone = clone(obj);
        } catch (Exception e) {
            // pass
        }
        return clone == null ? obj : clone;
    }

    /**
     * 序列化后拷贝流的方式克隆<br>
     * 对象必须实现Serializable接口
     *
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象
     * @throws UtilException IO异常和ClassNotFoundException封装
     */
    @SuppressWarnings("unchecked")
    public static <T> T cloneByStream(final T obj) {
        T result;
        if (!Objects.isNull(obj) && obj instanceof Serializable) {
            Serializer serializer = new JavaSerializer();
            result = (T) serializer.deserialize(serializer.serialize(obj));
        } else {
            result = null;
        }
        return result;
    }


    /**
     * 对象转字符串
     *
     * @param object  对象
     * @param charset 编码
     * @return 字符串
     */
    public static String object2String(final Object object, final Charset charset) {
        String str;
        if (object instanceof byte[] || object instanceof Byte[]) {
            str = StringUtil.str((byte[]) object, charset);
        } else if (object instanceof ByteBuffer) {
            str = StringUtil.str((ByteBuffer) object, charset);
        } else if (ArrayUtil.isArray(object)) {
            str = ArrayUtil.toString(object);
        } else {
            str = object.toString();
        }

        return str;

    }
}
