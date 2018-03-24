package com.star.io.file;

import com.star.exception.IORuntimeException;
import com.star.io.ResourceUtil;
import com.star.io.file.filevisitor.CopyDirVisitor;
import com.star.io.file.filevisitor.DeleteDirVisitor;
import com.star.io.file.filevisitor.MoveDirVisitor;
import com.star.lang.Filter;
import com.star.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 路径工具类
 *
 * @author starhq
 */
public final class PathUtil {

    private PathUtil() {
    }

    /**
     * 获取目录下的所有文件
     *
     * @param path 目录
     * @return 符合条件的过滤器
     */
    public static List<Path> loopFiles(final File path) {
        return loopFiles(path.toPath(), null, null);
    }

    /**
     * 获取目录下的所有文件
     *
     * @param path 目录
     * @return 符合条件的过滤器
     */
    public static List<Path> loopFiles(final String path) {
        return loopFiles(Paths.get(path), null, null);
    }

    /**
     * 获取目录下的所有文件
     *
     * @param path       目录
     * @param fileFilter 文件过滤器
     * @return 符合条件的过滤器
     */
    public static List<Path> loopFiles(final String path, final Filter<Path> fileFilter) {
        return loopFiles(Paths.get(path), fileFilter, null);
    }

    /**
     * 获取目录下的所有文件
     *
     * @param path            目录
     * @param fileFilter      文件过滤器
     * @param directoryFilter 目录过滤器
     * @return 符合条件的过滤器
     */
    public static List<Path> loopFiles(final Path path, final Filter<Path> fileFilter,
                                       final Filter<Path> directoryFilter) {
        final List<Path> paths = new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                /**
                 * 遍历目录
                 * @param dir 目录
                 * @param attrs 属性
                 * @return 访问或者跳过目录
                 */
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return Objects.isNull(directoryFilter) ? FileVisitResult.CONTINUE
                            : directoryFilter.accept(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
                }

                /**
                 * 遍历w文件
                 * @param file 文件
                 * @param attrs 属性
                 * @return 继续访问
                 */
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (Objects.requireNonNull(fileFilter).accept(file)) {
                        paths.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("get path {}'s file failure,the reason is {}", path, e.getMessage()), e);
        }
        return paths;
    }

    /**
     * 判断path是否存在
     *
     * @param path 路径
     * @return 是否存在
     */
    public static boolean exist(final Path path) {
        return !Objects.isNull(path) && Files.exists(path);
    }


    /**
     * 创建文件
     *
     * @param path 文件
     * @return 文件
     */
    public static Path touch(final Path path) {
        if (!Files.exists(path)) {
            mkParentDirs(path);
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new IORuntimeException(
                        StringUtil.format("touch path {} failure.the reason is: {}", path, e.getMessage()), e);
            }
        }
        return path;
    }

    /**
     * 创建父目录
     *
     * @param path 文件
     * @return 目录
     */
    public static Path mkParentDirs(final Path path) {
        final Path parent = path.getParent();
        if (!Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new IORuntimeException(
                        StringUtil.format("make {}'s parent dir failure,the reason is: {}", path, e.getMessage()), e);
            }
        }
        return parent;
    }

    /**
     * 创建目录
     *
     * @param path 目录
     * @return 目录
     */
    public static Path mkDirs(final Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new IORuntimeException(
                        StringUtil.format("make {}'s  dir failure,the reason is: {}", path, e.getMessage()), e);
            }
        }
        return path;
    }

    /**
     * 删除文件
     *
     * @param path 目录
     */
    public static void loopDelete(final Path path) {
        try {
            Files.walkFileTree(path, new DeleteDirVisitor());
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("loop delete direcotry {}'s file failure,the reason is {}", path, e.getMessage()),
                    e);
        }
    }

    /**
     * 复制文件
     *
     * @param src  源
     * @param dest 目的地
     */
    public static void copy(final Path src, final Path dest) {
        try {
            Files.walkFileTree(src, new CopyDirVisitor(src, dest));
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("copy {} to {} failure the reason is {}", src, dest, e.getMessage()), e);
        }
    }

    /**
     * 移动目录
     *
     * @param src  源
     * @param dest 目的地
     */
    public static void move(final Path src, final Path dest) {
        try {
            Files.walkFileTree(src, new MoveDirVisitor(src, dest));
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("move {} to {} failure the reason is {}", src, dest, e.getMessage()), e);
        }
    }

    /**
     * 获得绝对路径
     *
     * @param path 路径
     * @return 绝对路径
     */
    public static Path getAbsolutePath(final String path) {
        final Path result = Paths.get(path);
        return result.isAbsolute() ? result : result.toAbsolutePath();
    }

    /**
     * 获得绝对路径
     *
     * @param path 路径
     * @return 绝对路径
     */
    public static Path getAbsoluteClassPath(final String path) {
        final URL url = ResourceUtil.getResource(path);
        try {
            final Path result = Paths.get(url.toURI());
            return result.isAbsolute() ? result : result.toAbsolutePath();
        } catch (URISyntaxException e) {
            throw new IORuntimeException(
                    StringUtil.format("get {}'s absolure class path failure,the reason is {}", path, e.getMessage()),
                    e);
        }
    }

    /**
     * 相对子路径
     * <p>
     * 简单点就是child路径去掉root路径
     */
    public static Path subPath(final Path root, final Path child) {
        return child.subpath(root.getNameCount(), child.getNameCount());
    }
}
