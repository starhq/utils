package com.star.runtime;

import com.star.collection.array.ArrayUtil;
import com.star.exception.IORuntimeException;
import com.star.exception.ToolException;
import com.star.io.CharsetUtil;
import com.star.io.IoUtil;
import com.star.string.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统运行时工具类，用于执行系统命令的工具
 *
 * @author Looly
 * @since 3.1.1
 */
public final class RuntimeUtil {

    private RuntimeUtil() {
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param cmds 命令列表，每个元素代表一条命令
     * @return 执行结果
     * @throws IORuntimeException IO异常
     */
    public static String execForStr(final String... cmds) throws IORuntimeException {
        return execForStr(CharsetUtil.DEFAULT, cmds);
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param charset 编码
     * @param cmds    命令列表，每个元素代表一条命令
     * @return 执行结果
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static String execForStr(final Charset charset, final String... cmds) throws IORuntimeException {
        return getResult(exec(cmds), charset);
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param cmds 命令列表，每个元素代表一条命令
     * @return 执行结果，按行区分
     * @throws IORuntimeException IO异常
     */
    public static List<String> execForLines(final String... cmds) throws IORuntimeException {
        return execForLines(CharsetUtil.DEFAULT, cmds);
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param charset 编码
     * @param cmds    命令列表，每个元素代表一条命令
     * @return 执行结果，按行区分
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static List<String> execForLines(final Charset charset, final String... cmds) throws IORuntimeException {
        return getResultLines(exec(cmds), charset);
    }

    /**
     * 执行命令<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param cmds 命令
     * @return {@link Process}
     */
    public static Process exec(String... cmds) {
        if (ArrayUtil.isEmpty(cmds)) {
            throw new ToolException("command is empty!");
        }

        // 单条命令的情况
        if (1 == cmds.length) {
            final String cmd = cmds[0];
            if (StringUtil.isBlank(cmd)) {
                throw new ToolException("Command is empty !");
            }
            cmds = cmd.split(StringUtil.SPACE);
        }

        Process process;
        try {
            process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return process;
    }

    // -------------------------------------------------------------------------------------------------- result

    /**
     * 获取命令执行结果，使用系统默认编码，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @return 命令执行结果列表
     */
    public static List<String> getResultLines(final Process process) {
        return getResultLines(process, CharsetUtil.DEFAULT);
    }

    /**
     * 获取命令执行结果，使用系统默认编码，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @param charset 编码
     * @return 命令执行结果列表
     * @since 3.1.2
     */
    public static List<String> getResultLines(final Process process, final Charset charset) {
        InputStream input = null;
        try {
            input = process.getInputStream();
            return IoUtil.readLines(input, charset, new ArrayList<>());
        } finally {
            IoUtil.close(input);
        }
    }

    /**
     * 获取命令执行结果，使用系统默认编码，，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @return 命令执行结果列表
     * @since 3.1.2
     */
    public static String getResult(final Process process) {
        return getResult(process, CharsetUtil.DEFAULT);
    }

    /**
     * 获取命令执行结果，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @param charset 编码
     * @return 命令执行结果列表
     * @since 3.1.2
     */
    public static String getResult(final Process process, final Charset charset) {
        InputStream input = null;
        try {
            input = process.getInputStream();
            return IoUtil.read(input, charset);
        } finally {
            IoUtil.close(input);
            destroy(process);
        }
    }

    /**
     * 销毁进程
     *
     * @param process 进程
     * @since 3.1.2
     */
    public static void destroy(final Process process) {
        if (null != process) {
            process.destroy();
        }
    }

    /**
     * 增加一个JVM关闭后的钩子，用于在JVM关闭时执行某些操作
     *
     * @param hook 钩子
     * @since 4.0.5
     */
    public static void addShutdownHook(final Runnable hook) {
        Runtime.getRuntime().addShutdownHook(hook instanceof Thread ? (Thread) hook : new Thread(hook));
    }
}
