package com.star.io;

import com.star.exception.IORuntimeException;
import com.star.string.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区
 * <p>
 * 可以通过{@link #toByteArray()}和 {@link #toString()}来获取数据
 * <p>
 * {@link #close()}方法无任何效果，当流被关闭后不会抛出IOException
 * <p>
 * 这种设计避免重新分配内存块而是分配新增的缓冲区，缓冲区不会被GC，数据也不会被拷贝到其他缓冲区。
 *
 * @author biezhi
 */
public class FastByteArrayOutputStream extends OutputStream {

    /**
     * 快于缓冲区
     */
    private final FastByteBuffer buffer;

    /**
     * 构造方法,快速缓冲区大小设置为1k
     */
    public FastByteArrayOutputStream() {
        this(1024);
    }

    /**
     * 构造方法
     *
     * @param size 缓冲区大小
     */
    public FastByteArrayOutputStream(int size) {
        super();
        buffer = new FastByteBuffer(size);
    }

    /**
     * 流中写入数组
     *
     * @param b   字节数组
     * @param off 便宜量
     * @param len 长度
     * @throws IOException
     */
    @Override
    public void write(byte[] b, int off, int len) {
        buffer.append(b, off, len);
    }

    /**
     * 流中写入数据
     *
     * @param b 数据
     * @throws IOException
     */
    @Override
    public void write(int b) {
        buffer.append((byte) b);
    }

    /**
     * 获取流大小
     *
     * @return 流大小
     */
    public int getSize() {
        return buffer.getSize();
    }

    /**
     * 关闭流
     *
     * @throws IOException
     */
    @Override
    public void close() {
        // nop
    }

    /**
     * 重置流
     */
    public void reset() {
        buffer.reset();
    }

    /**
     * 将快速缓冲区内容写入输出流
     *
     * @param ops 输出流
     */
    public void writeTo(OutputStream ops) {
        final int idx = buffer.getIndex();
        byte[] buf;
        try {
            for (int i = 0; i < idx; i++) {
                buf = buffer.getArray(idx);
                ops.write(buf);
            }
            ops.write(buffer.getArray(idx), 0, buffer.getOffset());
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil
                    .format("fast byte buffer write to output stream failue,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 获得输出流中的字节数组
     *
     * @return 字节数组
     */
    public byte[] toByteArray() {
        return buffer.toArray();
    }

    /**
     * 输出流转字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return toString(Charset.defaultCharset());
    }

    /**
     * 输出流转字符串
     *
     * @param charset 编码
     * @return 字符串
     */
    public String toString(final Charset charset) {
        return StringUtil.str(toByteArray(), charset);
    }

    /**
     * 输出流转字符串
     *
     * @param charset 编码
     * @return 字符串
     */
    public String toString(final String charset) {
        return StringUtil.str(toByteArray(), charset);
    }
}
