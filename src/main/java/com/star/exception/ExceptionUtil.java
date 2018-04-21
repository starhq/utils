package com.star.exception;

import com.star.clazz.ClassUtil;
import com.star.collection.map.MapUtil;
import com.star.io.CharsetUtil;
import com.star.io.FastByteArrayOutputStream;
import com.star.string.StringUtil;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常工具类
 *
 * @author Looly
 */
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    /**
     * 获得完整消息，包括异常名
     *
     * @param throwable 异常
     * @return 完整消息
     */
    public static String getMessage(final Throwable throwable) {
        return StringUtil.format("{}: {}", ClassUtil.getClassName(throwable.getClass(), true), throwable.getMessage());
    }

    /**
     * 使用运行时异常包装编译异常
     *
     * @param throwable 异常
     * @return 运行时异常
     */
    public static RuntimeException wrapRuntime(final Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
    }

    /**
     * 包装一个异常
     *
     * @param throwable     异常
     * @param wrapThrowable 包装后的异常类型
     * @param <T>           泛型
     * @return 包抓昂后的异常类
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T wrap(final Throwable throwable, final Class<T> wrapThrowable) {
        return wrapThrowable.isInstance(throwable) ? (T) throwable : ClassUtil.newInstance(wrapThrowable, throwable);
    }

    /**
     * 获取当前栈信息
     *
     * @return 栈信息数组
     */
    public static StackTraceElement[] getStackElements() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 堆栈转为单行完整字符串
     *
     * @param throwable 异常对象
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToOneLineString(final Throwable throwable) {
        return stacktraceToOneLineString(throwable, 3000);
    }

    /**
     * 堆栈转为单行完整字符串
     *
     * @param throwable 异常对象
     * @param limit     限制最大长度
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToOneLineString(final Throwable throwable, final int limit) {
        final Map<Character, String> replaceCharToStrMap = new HashMap<>(3);
        replaceCharToStrMap.put(StringUtil.C_CR, StringUtil.SPACE);
        replaceCharToStrMap.put(StringUtil.C_LF, StringUtil.SPACE);
        replaceCharToStrMap.put(StringUtil.C_TAB, StringUtil.SPACE);

        return stacktraceToString(throwable, limit, replaceCharToStrMap);
    }

    /**
     * 堆栈转为完整字符串
     *
     * @param throwable 异常对象
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(final Throwable throwable) {
        return stacktraceToString(throwable, 3000);
    }

    /**
     * 堆栈转为完整字符串
     *
     * @param throwable 异常对象
     * @param limit     限制最大长度
     * @return 堆栈转为的字符串
     */
    public static String stacktraceToString(final Throwable throwable, final int limit) {
        return stacktraceToString(throwable, limit, null);
    }

    /**
     * 堆栈转为完整字符串
     *
     * @param throwable           异常对象
     * @param limit               限制最大长度
     * @param replaceCharToStrMap 替换字符为指定字符串
     * @return 异常信息字符串
     */
    public static String stacktraceToString(final Throwable throwable, final int limit, final Map<Character, String> replaceCharToStrMap) {
        final FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        try {
            throwable.printStackTrace(new PrintStream(baos, false, CharsetUtil.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new IORuntimeException(StringUtil.format("throwable print stacktrace failure,the reason is {}", e.getMessage()), e);
        }
        String exceptionStr = baos.toString();
        int length = exceptionStr.length();
        if (limit > 0 && limit < length) {
            length = limit;
        }
        if (!MapUtil.isEmpty(replaceCharToStrMap)) {
            final StringBuilder builder = StringUtil.builder();
            char cha;
            String value;
            for (int i = 0; i < length; i++) {
                cha = exceptionStr.charAt(i);
                value = replaceCharToStrMap.get(cha);
                if (null == value) {
                    builder.append(cha);
                } else {
                    builder.append(value);
                }
            }
            exceptionStr = builder.toString();
        }
        return exceptionStr;
    }

    /**
     * 剥离反射引发的InvocationTargetException、UndeclaredThrowableException中间异常，返回业务本身的异常
     *
     * @param wrapped 包装的异常
     * @return 剥离后的异常
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }
}
