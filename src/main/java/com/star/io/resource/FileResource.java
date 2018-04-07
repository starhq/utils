package com.star.io.resource;

import com.star.net.URLUtil;

import java.io.File;
import java.nio.file.Paths;

/**
 * 文件资源访问对象
 *
 * @author looly
 */
public class FileResource extends UrlResource {

    // ----------------------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param file 文件
     */
    public FileResource(File file) {
        super(URLUtil.getURL(file));
    }

    /**
     * 构造
     *
     * @param path 文件绝对路径或相对ClassPath路径，但是这个路径不能指向一个jar包中的文件
     */
    public FileResource(String path) {
        this(Paths.get(path).toFile());
    }
    // ----------------------------------------------------------------------- Constructor end

}