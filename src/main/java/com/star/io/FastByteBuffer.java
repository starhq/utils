package com.star.io;

import java.util.Objects;

/**
 * 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组
 * <p>
 * 功能和bytebuffer差不多,测下来性能要好一点，可以考虑在实战中使用
 *
 * @author http://git.oschina.net/loolly/hutool
 */
public class FastByteBuffer {

    /**
     * 一个缓冲区的最小字节数
     */
    private final int minChunkLen;
    /**
     * 缓冲集
     */
    private byte[][] buffers = new byte[16][];
    /**
     * 缓冲数
     */
    private int buffersCount;
    /**
     * 当前缓冲索引
     */
    private int currentIndex = -1;
    /**
     * 当前缓冲
     */
    private byte[] currentBuffer;
    /**
     * 当前缓冲偏移量
     */
    private int offset;
    /**
     * 缓冲字节数
     */
    private int size;

    /**
     * 构造方法
     */
    public FastByteBuffer() {
        this(1024);
    }

    /**
     * 构造方法
     *
     * @param size 最小字节数
     */
    public FastByteBuffer(final int size) {
        this.minChunkLen = Math.abs(size);
    }

    /**
     * 分配下一个缓冲区，不会小于1024
     *
     * @param newSize 理想缓冲区字节数
     */
    private void needNewBuffer(final int newSize) {
        final int delta = newSize - size;
        final int newBufferSize = Math.max(minChunkLen, delta);
        currentIndex++;
        currentBuffer = new byte[newBufferSize];
        offset = 0;
        if (currentIndex >= buffers.length) {
            final int newLen = buffers.length << 1;
            final byte[][] newBuffers = new byte[newLen][];
            System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
            buffers = newBuffers;
        }
        buffers[currentIndex] = currentBuffer;
        buffersCount++;
    }

    /**
     * 向快速缓冲加入数据
     *
     * @param array 数据
     * @param off   偏移量
     * @param len   字节数
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final byte[] array, final int off, final int len) {
        final int end = off + len;
        if (off < 0 || len < 0 || end > array.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len != 0) {
            final int newSize = size + len;
            int remaining = len;

            if (currentBuffer != null) {
                // first try to fill current buffer
                final int part = Math.min(remaining, currentBuffer.length - offset);
                System.arraycopy(array, end - remaining, currentBuffer, offset, part);
                remaining -= part;
                offset += part;
                size += part;
            }

            if (remaining > 0) {
                // still some data left
                // ask for new buffer
                needNewBuffer(newSize);

                // then copy remaining
                // but this time we are sure that it will fit
                final int part = Math.min(remaining, currentBuffer.length - offset);
                System.arraycopy(array, end - remaining, currentBuffer, offset, part);
                offset += part;
                size += part;
            }
        }
        return this;
    }

    /**
     * 向快速缓冲加入数据
     *
     * @param array 数据
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final byte[] array) {
        return append(array, 0, array.length);
    }

    /**
     * 向快速缓冲加入数据
     *
     * @param element 数据
     * @return 快速缓冲自身
     */
    public FastByteBuffer append(final byte element) {
        if (Objects.isNull(currentBuffer) || offset == currentBuffer.length) {
            needNewBuffer(size + 1);
        }
        currentBuffer[offset] = element;
        offset++;
        size++;
        return this;
    }

    /**
     * 返回缓冲区大小
     *
     * @return 大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 缓冲区是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 返回缓冲区索引
     *
     * @return 索引
     */
    public int getIndex() {
        return currentIndex;
    }

    /**
     * 返回缓冲区偏移量
     *
     * @return 偏移量
     */
    public int getOffset() {
        return offset;
    }

    /**
     * 返回缓冲集中指定索引的缓冲
     *
     * @param index 索引
     * @return 缓冲
     */
    public byte[] getArray(final int index) {
        return buffers[index].clone();
    }

    /**
     * 重置缓冲集
     */
    public void reset() {
        size = 0;
        offset = 0;
        currentIndex = -1;
        currentBuffer = null;
        buffersCount = 0;
    }

    /**
     * 返回缓冲集中的数据
     *
     * @return 缓冲中的数据
     */
    public byte[] toArray() {
        final byte[] result = new byte[size];
        if (currentIndex != -1) {
            int pos = 0;
            for (int i = 0; i < currentIndex; i++) {
                final int len = buffers[i].length;
                System.arraycopy(buffers[i], 0, result, pos, len);
                pos += len;
            }
            System.arraycopy(buffers[currentIndex], 0, result, pos, offset);
        }
        return result;
    }

    /**
     * 返回缓冲集中的数据
     *
     * @param start 缓冲区起始位置
     * @param len   长度
     * @return 字节数组
     */
    public byte[] toArray(final int start, final int len) {
        final byte[] array = new byte[len];

        if (len != 0) {
            int flag = 0;
            int begin = start;
            while (start >= buffers[flag].length) {
                begin -= buffers[flag].length;
                flag++;
            }
            int remaining = len;
            int pos = 0;
            while (flag < buffersCount) {
                final byte[] buf = buffers[flag];
                final int last = Math.min(buf.length - begin, remaining);
                System.arraycopy(buf, start, array, pos, last);
                pos += last;
                remaining -= last;
                if (remaining == 0) {
                    break;
                }
                begin = 0;
                flag++;
            }
        }
        return array;
    }

    /**
     * 根据索引位返回一个字节
     *
     * @param index 索引位
     * @return 一个字节
     */
    public byte get(final int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int ndx = 0;
        int idx = index;
        while (true) {
            final byte[] bytes = buffers[ndx];
            if (idx < bytes.length) {
                return bytes[idx];
            }
            ndx++;
            idx -= bytes.length;
        }
    }
}
