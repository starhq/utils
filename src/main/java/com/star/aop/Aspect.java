package com.star.aop;

import com.star.exception.ToolException;
import com.star.reflect.MethodUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 切面抽象
 *
 * @author Looly
 */
public abstract class Aspect implements InvocationHandler {

    private Object target;

    public Aspect(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return this.target;
    }

    /**
     * 目标方法执行前的操作
     *
     * @param target 目标对象
     * @param method 目标方法
     * @param args   参数
     * @return 是否继续执行接下来的操作
     */
    public abstract boolean before(Object target, Method method, Object[] args);

    /**
     * 目标方法执行后的操作
     *
     * @param target 目标对象
     * @param method 目标方法
     * @param args   参数
     * @return 是否允许返回值（接下来的操作）
     */
    public abstract boolean after(Object target, Method method, Object[] args);

    /**
     * 目标方法抛出异常时的操作
     *
     * @param target 目标对象
     * @param method 目标方法
     * @param args   参数
     * @param e      异常
     * @return 是否允许抛出异常
     */
    public abstract boolean afterException(Object target, Method method, Object[] args, Throwable e);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Object result = null;
        if (before(target, method, args)) {
            try {
                result = MethodUtil.invoke(target, method, args);
            } catch (ToolException e) {
                final Throwable cause = e.getCause();
                if (e.getCause() instanceof InvocationTargetException) {
                    afterException(target, method, args, ((InvocationTargetException) cause).getTargetException());
                } else {
                    throw e;
                }
            }
        }
        if (after(target, method, args)) {
            return result;
        }
        return null;
    }
}
