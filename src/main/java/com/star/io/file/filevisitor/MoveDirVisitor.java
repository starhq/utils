package com.star.io.file.filevisitor;

import com.star.io.file.PathUtil;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 移动文件
 *
 * @author starhq
 */
public class MoveDirVisitor extends SimpleFileVisitor<Path> {

    /**
     * 目标
     */
    private final Path src;
    /**
     * 目的地
     */
    private final Path dest;
    /**
     * 拷贝选项
     */
    private StandardCopyOption copyOption;

    /**
     * 构造方法
     *
     * @param src        目标
     * @param dest       目的地
     * @param copyOption 拷贝选项
     */
    public MoveDirVisitor(final Path src, final Path dest, final StandardCopyOption copyOption) {
        super();
        this.src = src;
        this.dest = dest;
        this.copyOption = copyOption;
    }

    /**
     * 构造方法
     *
     * @param src  目标
     * @param dest 目的地
     */
    public MoveDirVisitor(final Path src, final Path dest) {
        this(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 移动文件
     *
     * @param dir   目录
     * @param attrs 文件属性
     * @return 是否继续访问
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
        final Path targetPath = dest.resolve(src.relativize(dir));
        PathUtil.mkDirs(targetPath);
        return FileVisitResult.CONTINUE;
    }

    /**
     * 移动文件
     *
     * @param file  文件
     * @param attrs 文件属性
     * @return 是否继续访问
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Files.move(file, dest.resolve(src.relativize(file)), copyOption);
        return FileVisitResult.CONTINUE;
    }

    /**
     * 删除老的目录
     *
     * @param dir 目录
     * @param exc 处理过程中的异常
     * @return 是否继续访问
     */
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        Files.deleteIfExists(dir);
        return FileVisitResult.CONTINUE;
    }

}
