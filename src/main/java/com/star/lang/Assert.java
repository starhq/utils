package com.star.lang;

import com.star.exception.ToolException;

/**
 * 断言工具类
 * <p>
 * Created by win7 on 2017/5/20.
 */
public final class Assert {

    private Assert() {
    }

    /**
     * 为真断言
     *
     * @param expression 表达式
     * @param message    条件不成立异常的附带信息系
     */
    public static void isTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new ToolException(message);
        }
    }

    /**
     * 为真断言
     *
     * @param expression 表达式
     */
    public static void isTrue(final boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

}
