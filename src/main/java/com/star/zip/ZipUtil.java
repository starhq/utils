package com.star.zip;

import com.star.exception.ToolException;
import com.star.io.FastByteArrayOutputStream;
import com.star.io.IoUtil;
import com.star.io.file.PathUtil;
import com.star.lang.Assert;
import com.star.string.StringUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类
 */
public final class ZipUtil {

    private ZipUtil() {
    }

    // ======================zip unzip================================

    /**
     * 压缩文件
     *
     * @param src        源文件
     * @param dest       目标文件
     * @param withSrcDir 包含源目录
     */
    public static void zip(final Path src, final Path dest, final boolean withSrcDir) {
        validateFile(src, dest);
        final ZipOutputStream zipOutputStream = new ZipOutputStream(
                new CheckedOutputStream(PathUtil.getOutputStream(dest, false), new CRC32()));

        try {
            Files.walkFileTree(src, new SimpleFileVisitor<Path>() {

                /**
                 * 遍历文件并压缩
                 */
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
                    final String subPath = PathUtil.subPath(withSrcDir ? src.getParent() : src.toAbsolutePath(), file)
                            .toString();
                    BufferedInputStream bis = null;
                    try {
                        zipOutputStream.putNextEntry(new ZipEntry(subPath));
                        bis = PathUtil.getInputStream(file);
                        IoUtil.copy(bis, zipOutputStream, 0);
                    } catch (IOException e) {
                        throw new ToolException(
                                StringUtil.format("loop zip file failure,the reason is: {}", e.getMessage()), e);
                    } finally {
                        IoUtil.close(bis);
                        closeEntry(zipOutputStream);
                    }

                    return FileVisitResult.CONTINUE;
                }

            });
            zipOutputStream.flush();
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("walk directory {} zip file to {} failure,the reason is: {}", src,
                    dest, e.getMessage()), e);
        } finally {
            IoUtil.close(zipOutputStream);
        }
    }

    /**
     * 解压文件
     *
     * @param zip  源文件
     * @param dest 目标文件
     */
    @SuppressWarnings("unchecked")
    public static void unzip(final Path zip, final Path dest) {
        ZipFile zipFileObj = null;
        try {
            zipFileObj = new ZipFile(zip.toFile());
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("extract file failure,get zipfile error: {}", e.getMessage()), e);
        }
        final Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFileObj.entries();
        ZipEntry zipEntry = null;
        Path outItemPath = null;
        while (enu.hasMoreElements()) {
            zipEntry = enu.nextElement();
            outItemPath = Paths.get(dest.toAbsolutePath().toString(), zipEntry.getName());
            if (zipEntry.isDirectory()) {
                PathUtil.mkDirs(outItemPath);
            } else {
                PathUtil.touch(outItemPath);
                copy(zipFileObj, zipEntry, outItemPath);
            }
        }
    }

    // ======================zip unzip================================

    // ======================gzip ungzip=============================

    /**
     * gzip压缩字节数数组
     *
     * @param val 字节数组
     * @return 压缩的数组
     */
    public static byte[] gzipArray(final byte[] val) {
        final FastByteArrayOutputStream bos = new FastByteArrayOutputStream(val.length);
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(bos);
            gos.write(val, 0, val.length);
            gos.finish();
            gos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("gzip array byte failure,the reason is: {}", e.getMessage()), e);
        } finally {
            IoUtil.close(gos);
        }
    }

    /**
     * gzip压缩文件
     *
     * @param path 文件
     * @return 压缩后的字节数组
     */
    public static byte[] gzipFile(final Path path) {
        Assert.isTrue(PathUtil.exist(path), "gzip array byte failure,the input path is not exists");
        GZIPOutputStream gos = null;
        BufferedInputStream inputStream;
        try {
            final FastByteArrayOutputStream bos = new FastByteArrayOutputStream((int) Files.size(path));
            gos = new GZIPOutputStream(bos);
            inputStream = PathUtil.getInputStream(path);
            IoUtil.copy(inputStream, gos, 0);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("gzip array byte failure,the reason is: {}", e.getMessage()), e);
        } finally {
            IoUtil.close(gos);
        }
    }

    /**
     * gzip解压
     *
     * @param buf 压缩过的数组
     * @return 字节数组
     */
    public static byte[] unGzip(final byte[] buf) {
        GZIPInputStream gzi = null;
        ByteArrayOutputStream bos = null;
        try {
            gzi = new GZIPInputStream(new ByteArrayInputStream(buf));
            bos = new ByteArrayOutputStream(buf.length);
            IoUtil.copy(gzi, bos, 0);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("ungzip array byte failure,the reasone is: {}", e.getMessage()),
                    e);
        } finally {
            IoUtil.close(gzi);
        }
    }
    // ======================gzip ungzip=============================

    /**
     * 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常
     *
     * @param src  源文件
     * @param dest 目标文件
     */
    private static void validateFile(final Path src, final Path dest) {
        if (Files.isDirectory(src) && dest.startsWith(src)) {
            throw new ToolException("validate file failure,the dest path can't include in src path");
        }
        if (!Files.exists(dest)) {
            PathUtil.touch(dest);
        }
    }

    /**
     * 关闭当前Entry，继续下一个Entry
     *
     * @param zipOutStream 压缩输出流额
     */
    private static void closeEntry(final ZipOutputStream zipOutStream) {
        try {
            zipOutStream.closeEntry();
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("close entry failue,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 从压缩包中拷贝子文件到指定文件中
     *
     * @param zipFile  压缩文件
     * @param zipEntry 压缩实体
     * @param path     文件
     */
    private static void copy(final ZipFile zipFile, final ZipEntry zipEntry, final Path path) {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = zipFile.getInputStream(zipEntry);
            output = PathUtil.getOutputStream(path, false);
            IoUtil.copy(input, output, 0);
        } catch (IOException e) {
            throw new ToolException(StringUtil.format("extract file {} from zip file{} to {} failure,the reason is: {}",
                    zipEntry.getName(), zipFile.getName(), path.toAbsolutePath(), e.getMessage()), e);
        } finally {
            IoUtil.close(output);
            IoUtil.close(input);
        }
    }

}
