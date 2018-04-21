package com.star.lang;

import com.star.collection.CollectionUtil;
import com.star.collection.array.ArrayUtil;
import com.star.collection.map.MapUtil;
import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 断言工具类
 * <p>
 *
 * @author starhq
 */
public final class Assert {

    private Assert() {
    }

    /**
     * 断言是否为正
     *
     * @param expression 是否为真的表达式
     * @param message    断言的信息
     */
    public static void isTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new ToolException(StringUtil.isBlank(message) ? "expression must be true" : message);
        }
    }

    /**
     * 断言对象为空
     *
     * @param object  对象
     * @param message 断言的信息
     */
    public static void isNull(final Object object, final String message) {
        if (!Objects.isNull(object)) {
            throw new ToolException(StringUtil.isBlank(message) ? "object must be null" : message);
        }
    }

    /**
     * 断言对象不为空
     *
     * @param object  对象
     * @param message 断言的信息
     */
    public static void notNull(final Object object, final String message) {
        if (Objects.isNull(object)) {
            throw new ToolException(StringUtil.isBlank(message) ? "object must be null" : message);
        }
    }

    /**
     * 字符串不能为空
     *
     * @param message 断言的信息
     * @param text    字符串
     */
    public static void notEmpty(final String text, final String message) {
        if (StringUtil.isEmpty(text)) {
            throw new ToolException(StringUtil.isBlank(message) ? "string must not be empty" : message);
        }
    }

    /**
     * 字符串不能为空白
     *
     * @param text    字符串
     * @param message 字符串
     */
    public static void notBlank(final String text, final String message) {
        if (StringUtil.isBlank(text)) {
            throw new ToolException(StringUtil.isBlank(message) ? "string must not be blank" : message);
        }
    }

    /**
     * textToSearch不能包含substring,好像作用不大啊
     *
     * @param message      断言信息
     * @param textToSearch 要搜索的字符串
     * @param substring    需要匹配的字符串
     */
    public static void notContain(final String textToSearch, final String substring, final String message) {
        if (!StringUtil.isEmpty(textToSearch) && !StringUtil.isEmpty(substring) && !textToSearch.contains(substring)) {
            throw new ToolException(StringUtil.isBlank(message)
                    ? StringUtil.format("string {} must not include sub string {}", textToSearch, substring) : message);
        }
    }

    /**
     * 数组不能为空
     *
     * @param array   对象数组
     * @param message 断言字符串
     */
    public static void notEmpty(final Object[] array, final String message) {
        if (ArrayUtil.isEmpty(array)) {
            throw new ToolException(StringUtil.isBlank(message) ? "array must not be empty" : message);
        }
    }

    /**
     * 数组中不能包含空
     *
     * @param array   对象数组
     * @param message 断言字符串
     */
    public static void noNullElements(final Object[] array, final String message) {
        if (ArrayUtil.isEmpty(array)) {
            for (final Object element : array) {
                if (Objects.isNull(element)) {
                    throw new ToolException(StringUtil.isBlank(message) ? "array must not has null element " : message);
                }
            }
        }
    }

    /**
     * 集合不能为空
     *
     * @param collection 集合
     * @param message    断言字符串
     */
    public static void notEmpty(final Collection<?> collection, final String message) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new ToolException(StringUtil.isBlank(message) ? "collection must not be empty" : message);
        }
    }

    /**
     * map不能为空
     *
     * @param map     map
     * @param message 断言字符串
     */
    public static void notEmpty(final Map<?, ?> map, final String message) {
        if (MapUtil.isEmpty(map)) {
            throw new ToolException(StringUtil.isBlank(message) ? "map must not be empty" : message);
        }
    }
}
