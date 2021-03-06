package com.star.log.dialect.console;

import com.star.log.Log;
import com.star.log.LogFactory;

/**
 * 利用System.out.println()打印日志
 *
 * @author Looly
 */
public class ConsoleLogFactory extends LogFactory {

    public ConsoleLogFactory() {
        super("Hutool Console Logging");
    }

    @Override
    public Log createLog(String name) {
        return new ConsoleLog(name);
    }

    @Override
    public Log createLog(Class<?> clazz) {
        return new ConsoleLog(clazz);
    }

}
