package com.star.object;

import com.star.clazz.ClassUtil;
import com.star.collection.array.ArrayUtil;
import com.star.reflect.MethodUtil;
import com.star.string.StringUtil;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
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
    public static <T extends Cloneable> T clone(final T obj) {
        final Method cloneMethod = MethodUtil.findDeclaredMethod(obj.getClass(), "clone");
        return MethodUtil.invoke(obj, cloneMethod);
    }

    /**
     * 克隆集合
     *
     * @param orig 源
     * @param <T>  泛型
     * @param <U>  泛型
     * @return 新集合
     */
    @SuppressWarnings("unchecked")
    public static <T extends Collection<U>, U extends Cloneable> T cloneCollection(final T orig) {
        return (T) cloneCollection(orig.getClass(), orig);
    }

    /**
     * 克隆集合
     *
     * @param type 类型
     * @param orig 源
     * @param <I>  源的类型
     * @param <O>  克隆泛型
     * @param <U>  泛型
     * @return 新集合
     */
    public static <I extends Collection<U>, O extends Collection<U>, U extends Cloneable> O cloneCollection(
            final Class<O> type, final I orig) {
        O obj = null;
        if (!Objects.isNull(orig)) {
            obj = ClassUtil.newInstance(type);
            for (final U element : orig) {
                obj.add(clone(element));
            }
        }
        return obj;
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
