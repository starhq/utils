package com.star.io.file;

import com.star.collection.array.ArrayUtil;

import java.io.File;
import java.util.Objects;

/**
 * 文件操作工具类
 *
 * @author starhq
 */
public final class FileUtil {

    private FileUtil() {
    }

    /**
     * 目录是否为空
     *
     * @param file 指定目录
     * @return 是否为空
     */
    public static boolean isEmpty(final File file) {
        boolean result;
        if (!Objects.isNull(file) && file.isDirectory()) {
            final String[] subFiles = file.list();
            result = ArrayUtil.isEmpty(subFiles);
        } else {
            result = false;
        }
        return result;
    }

}

