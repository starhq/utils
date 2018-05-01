package com.star.io.file;

import com.star.collection.array.ArrayUtil;
import com.star.exception.IORuntimeException;
import com.star.io.CharsetUtil;
import com.star.io.IoUtil;
import com.star.io.file.filevisitor.CopyDirVisitor;
import com.star.io.file.filevisitor.DeleteDirVisitor;
import com.star.io.file.filevisitor.MoveDirVisitor;
import com.star.io.resource.ResourceUtil;
import com.star.lang.Filter;
import com.star.lang.LineHandler;
import com.star.string.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
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
        if (!Objects.isNull(parent) && !Files.exists(parent)) {
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
        copy(src, dest, null);
    }

    /**
     * 复制文件
     *
     * @param src        源
     * @param dest       目的地
     * @param copyOption 拷贝属性
     */
    public static void copy(final Path src, final Path dest, final StandardCopyOption copyOption) {
        try {
            Files.walkFileTree(src, Objects.isNull(copyOption) ? new CopyDirVisitor(src, dest) : new CopyDirVisitor(src, dest, copyOption));
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
        move(src, dest, null);
    }

    /**
     * 移动目录
     *
     * @param src        源
     * @param dest       目的地
     * @param copyOption 拷贝属性
     */
    public static void move(final Path src, final Path dest, final StandardCopyOption copyOption) {
        try {
            Files.walkFileTree(src, Objects.isNull(copyOption) ? new MoveDirVisitor(src, dest) : new MoveDirVisitor(src, dest, copyOption));
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
     *
     * @param root  父路径
     * @param child 子路径
     * @return 自路径
     */
    public static Path subPath(final Path root, final Path child) {
        return child.subpath(root.getNameCount(), child.getNameCount());
    }

    /**
     * 获取指定位置的子路径部分，支持负数，例如起始为-1表示从后数第一个节点位置
     *
     * @param path  路径
     * @param index 起始位置
     * @return 获取的子路径
     */
    public static Path subIndexPath(final Path path, final int index) {
        return subPath(path, index, index == -1 ? path.getNameCount() : index + 1);
    }

    /**
     * 获取最后位置的子路径部分
     *
     * @param path 路径
     * @return 获取的子路径
     */
    public static Path subLastPath(final Path path) {
        return subIndexPath(path, path.getNameCount() - 1);
    }

    /**
     * 获取指定位置的子路径部分，支持负数，例如起始为-1表示从后数第一个节点位置
     *
     * @param path      路径
     * @param fromIndex 起始路径节点（包括）
     * @param toIndex   结束路径节点（不包括）
     * @return 获取的子路径
     */
    public static Path subPath(final Path path, final int fromIndex, final int toIndex) {
        final int len = path.getNameCount();
        int start = fromIndex < 0 ? len + fromIndex : fromIndex;
        int end = toIndex < 0 ? len + toIndex : toIndex == 0 && fromIndex < 0 ? len :
                toIndex;
        if (end < start) {
            start = start ^ end;
            end = start ^ end;
            start = start ^ end;
        }

        return path.subpath(start, end);
    }

    /**
     * 文件是否为空（目录判断目录下是否为空，文件判断长度是否大于0）
     *
     * @param file 指定目录或者文件
     * @return 是否为空
     */
    public static boolean isEmpty(final File file) {
        boolean result;
        if (!Objects.isNull(file) && file.isDirectory()) {
            final String[] subFiles = file.list();
            result = ArrayUtil.isEmpty(subFiles);
        } else {
            result = file.length() <= 0;
        }
        return result;
    }

    /**
     * 返回主文件名
     *
     * @param file 文件
     * @return 主文件名
     */
    public static String mainName(final File file) {
        return file.isDirectory() ? file.getName() : mainName(file.getName());
    }

    /**
     * 返回主文件名
     *
     * @param fileName 完整文件名
     * @return 主文件名
     */
    public static String mainName(final String fileName) {
        final int idx = Objects.requireNonNull(fileName).lastIndexOf(StringUtil.DOT);
        return idx == -1 ? StringUtil.EMPTY : StringUtil.sub(fileName, idx);
    }

    /**
     * 获取文件扩展名，扩展名不带“.”
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(final File file) {
        return file.isDirectory() ? file.getName() : extName(file.getName());
    }

    /**
     * 获得文件的扩展名，扩展名不带“.”
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String extName(final String fileName) {
        final int idx = Objects.requireNonNull(fileName).lastIndexOf(StringUtil.DOT);
        return idx == -1 ? StringUtil.EMPTY : StringUtil.sub(fileName, idx + 1);
    }

    /**
     * 获得输入流
     *
     * @param path 文件
     * @return 输入流
     */
    public static BufferedInputStream getInputStream(final Path path) {
        try {
            return new BufferedInputStream(Files.newInputStream(path));
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("get {}'s inputstream failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 获得文件的输入流
     *
     * @param path 文件
     * @return 输入流
     */
    public static BufferedReader getUTF8Reader(final Path path) {
        return IoUtil.getReader(getInputStream(path), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 获得文件的输入流
     *
     * @param path    文件
     * @param charset 编码
     * @return 输入流
     */
    public static BufferedReader getReader(final Path path, final Charset charset) {
        return IoUtil.getReader(getInputStream(path), charset);
    }

    /**
     * 获得输出流
     *
     * @param path     文件
     * @param isAppend 是否在末尾追加
     * @return 输出流
     */
    public static BufferedOutputStream getOutputStream(final Path path, final boolean isAppend) {
        try {
            return new BufferedOutputStream(Files.newOutputStream(touch(path), isAppend ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW));
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("get {}'s inputstream failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path     输出文件
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     */
    public static BufferedWriter getWriter(final Path path, final Charset charset, final boolean isAppend) {
        try {
            return Files.newBufferedWriter(touch(path), charset, isAppend ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("get {}'s bufferedwriter failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return 打印对象
     */
    public static PrintWriter getPrintWriter(final Path path, final Charset charset, final boolean isAppend) {
        return new PrintWriter(getWriter(path, charset, isAppend));
    }

    /**
     * 读取文件所有数据
     *
     * @param path 文件
     * @return 字节码
     */
    public static byte[] readBytes(final Path path) {
        try (InputStream inputStream = getInputStream(path)) {
            return IoUtil.readBytes(inputStream);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("red {}'s byte array failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 读取文件内容
     *
     * @param path 文件
     * @return 内容
     */
    public static String readUTF8String(final Path path) {
        return readString(path, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 读取文件内容
     *
     * @param path    文件
     * @param charset 字符集
     * @return 内容
     */
    public static String readString(final Path path, final Charset charset) {
        return StringUtil.str(readBytes(path), charset);
    }

    /**
     * 按行处理文件内容
     *
     * @param path        文件
     * @param lineHandler 行处理器
     */
    public static void readUTF8Lines(final Path path, final LineHandler lineHandler) {
        readLines(path, CharsetUtil.CHARSET_UTF_8, lineHandler);
    }

    /**
     * 按行处理文件内容
     *
     * @param path        文件
     * @param charset     编码
     * @param lineHandler 行处理器
     */
    public static void readLines(final Path path, final Charset charset, final LineHandler lineHandler) {
        try (InputStream inputStream = getInputStream(path)) {
            IoUtil.readLines(inputStream, charset, lineHandler);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("red {}'s line by line and handle it failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param <T>        集合类型
     * @param path       文件
     * @param collection 集合
     * @return 文件中的每行内容的集合
     */
    public static <T extends Collection<String>> T readUTF8Lines(final Path path, final T collection) {
        return readLines(path, CharsetUtil.CHARSET_UTF_8, collection);
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param <T>        集合类型
     * @param path       文件
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     */
    public static <T extends Collection<String>> T readLines(final Path path, final Charset charset, final T collection) {
        try (InputStream inputStream = getInputStream(path)) {
            return IoUtil.readLines(inputStream, charset, collection);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("red {}'s line by line failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path    文件
     * @param charset 字符集
     * @return 文件中的每行内容的集合
     */
    public static List<String> readLines(final Path path, final Charset charset) {
        return readLines(path, charset, new ArrayList<>());
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path 文件
     * @return 文件中的每行内容的集合
     */
    public static List<String> readUTF8Lines(final Path path) {
        return readLines(path, CharsetUtil.CHARSET_UTF_8, new ArrayList<>());
    }

    /**
     * 往文件中写字符串
     *
     * @param path    文件
     * @param content 内容
     */
    public static void writeUTF8String(final Path path, final String content) {
        IoUtil.write(getOutputStream(path, false), CharsetUtil.CHARSET_UTF_8, true, content);
    }

    /**
     * 往文件中写字符串
     *
     * @param path    文件
     * @param charset 编码
     * @param content 内容
     */
    public static void writeString(final Path path, final Charset charset, final String content) {
        IoUtil.write(getOutputStream(path, false), charset, true, content);
    }

    /**
     * 往文件中追加字符串
     *
     * @param path    文件
     * @param charset 编码
     * @param content 内容
     */
    public static void appendString(final Path path, final Charset charset, final String content) {
        try (BufferedWriter bufferedWriter = getWriter(path, charset, true)) {
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("append string {} to {} failure,the reason is: {}", content, path, e.getMessage()), e);
        }
    }

    /**
     * 往文件中追加字符串
     *
     * @param path    文件
     * @param content 内容
     */
    public static void appendUTF8String(final Path path, final String content) {
        try (BufferedWriter bufferedWriter = getWriter(path, CharsetUtil.CHARSET_UTF_8, true)) {
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("append string {} to {} failure,the reason is: {}", content, path, e.getMessage()), e);
        }
    }

    /**
     * 往文件中写入多行数据
     *
     * @param path     文件
     * @param charset  字符集
     * @param isAppend 是否在文件尾追加
     * @param elements 集合
     * @param <T>      范型
     */
    public static <T> void writeLines(final Path path, final Charset charset, final boolean isAppend, final Collection<T> elements) {
        try (BufferedWriter bufferedWriter = getWriter(path, charset, isAppend)) {
            for (final T element : elements) {
                bufferedWriter.write(StringUtil.str(element));
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("write collection to {} failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 往文件中写入多行数据
     *
     * @param path     文件
     * @param isAppend 是否在文件尾追加
     * @param elements 集合
     * @param <T>      范型
     */
    public static <T> void writeUTF8Lines(final Path path, final boolean isAppend, final Collection<T> elements) {
        writeLines(path, CharsetUtil.CHARSET_UTF_8, isAppend, elements);
    }

    /**
     * 写入数据到文件
     *
     * @param data     数据
     * @param path     路径
     * @param isAppend 是否追加模式
     */
    public static void writeBytes(final byte[] data, final Path path, final boolean isAppend) {
        writeBytes(data, path, 0, data.length, isAppend);
    }

    /**
     * 写入数据到文件
     *
     * @param data     数据
     * @param path     目标文件
     * @param off      数据开始位置
     * @param len      数据长度
     * @param isAppend 是否追加模式
     */
    public static void writeBytes(final byte[] data, final Path path, final int off, final int len, final boolean isAppend) {
        try (OutputStream outputStream = getOutputStream(path, isAppend)) {
            outputStream.write(data, off, len);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("write data to {} failure,the reason is: {}", path, e.getMessage()), e);
        }
    }

    /**
     * 文件大小可读
     *
     * @param size 文件大小
     * @return 类似于 1MB
     */
    public static String readableFileSize(final long size) {
        String result;
        if (size > 0) {
            final String[] units = new String[]{"B", "kB", "MB", "GB", "TB", "EB"};
            final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            result = new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " "
                    + units[digitGroups];
        } else {
            result = "0";
        }
        return result;
    }

    /**
     * 生成文件的下载流
     *
     * @param path        文件路径
     * @param contentType 下载类型
     * @param response    响应
     */
    public static void makeStreamFile(final Path path, final HttpServletResponse response, final String contentType) {
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        try {
            response.reset();
            response.setCharacterEncoding(CharsetUtil.UTF_8);

            response.setHeader("Content-disposition", "attachment; filename=" + path.getFileName());
            response.setContentType(contentType);
            response.setContentLength((int) Files.size(path));

            output = new BufferedOutputStream(response.getOutputStream());
            input = getInputStream(path);

            IoUtil.copy(input, output, 4096);

        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("make file's download stream failure,the reason is: {}", e.getMessage()), e);
        } finally {
            IoUtil.close(input);
            IoUtil.close(output);
        }
    }

    /**
     * 根据url定位文件
     *
     * @param url url
     * @return 文件
     */
    public static Path getPath(final URL url) {
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IORuntimeException(
                    StringUtil.format("url to path failure,the reason is: {}", e.getMessage()), e);
        }
    }
}
