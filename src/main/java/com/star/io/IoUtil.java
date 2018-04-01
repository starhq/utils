package com.star.io;

import com.star.exception.IORuntimeException;
import com.star.lang.LineHandler;
import com.star.string.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Objects;

/**
 * io工具类
 *
 * @author starhq
 */
public final class IoUtil {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /**
     * 默认中等缓存大小
     */
    public static final int DEFAULT_MIDDLE_BUFFER_SIZE = DEFAULT_BUFFER_SIZE << 2;
    /**
     * 默认大缓存大小
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = DEFAULT_BUFFER_SIZE << 3;

    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    private IoUtil() {
    }

    /**
     * Reader中的内容复制到Writer中
     *
     * @param reader Reader
     * @param writer Writer
     * @return 复制了多少字节数
     */
    public static long copy(final Reader reader, final Writer writer) {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }


    /**
     * Reader中的内容复制到Writer中
     *
     * @param reader     Reader
     * @param writer     Writer
     * @param bufferSize 缓冲区大小
     * @return 复制了多少字节数
     */
    public static long copy(final Reader reader, final Writer writer, final int bufferSize) {
        final int size = bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize;
        final char[] buffer = new char[size];
        int count = 0;
        int readSize;
        try {
            while ((readSize = reader.read(buffer, 0, size)) != EOF) {
                writer.write(buffer, 0, readSize);
                count += readSize;
                writer.flush();
            }
            return count;
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("reader copy to writer failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 将InputStream中的内容复制到outputStream中
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return 复制了多少字节数
     */
    public static long copy(final InputStream inputStream, final OutputStream outputStream) {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 将InputStream中的内容复制到outputStream中
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param bufferSize   缓存大小
     * @return 复制了多少字节数
     */
    public static long copy(final InputStream inputStream, final OutputStream outputStream, final int bufferSize) {
        final int size = bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize;
        final byte[] buffer = new byte[size];
        int count = 0;
        int readSize;
        try {
            while ((readSize = inputStream.read(buffer)) != EOF) {
                outputStream.write(buffer, 0, readSize);
                count += readSize;
                outputStream.flush();
            }
            return count;
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("inputStream copy to outputStream failure the reason is: {}", e.getMessage()),
                    e);
        }
    }

    /**
     * 拷贝文件流（nio）
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return 复制了多少字节数
     */
    public static long copy(final FileInputStream inputStream, final FileOutputStream outputStream) {
        final FileChannel inChannel = inputStream.getChannel();
        final FileChannel outChannel = outputStream.getChannel();

        try {
            return inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("fileinputStream copy to fileoutputStream failure the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 拷贝流，使用NIO
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param bufferSize   缓冲数
     * @return 复制了多少字节
     */
    public static long copyByNIO(final InputStream inputStream, final OutputStream outputStream, final int bufferSize) throws IORuntimeException {
        return copy(Channels.newChannel(inputStream), Channels.newChannel(outputStream), bufferSize);
    }

    /**
     * 拷贝流，使用NIO
     *
     * @param srcChannel  输入流
     * @param descChannel 输出流
     * @param bufferSize  缓冲数
     * @return 复制了多少字节
     */
    public static long copy(final ReadableByteChannel srcChannel, final WritableByteChannel descChannel, final int bufferSize) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
        long size = 0;
        try {
            while (srcChannel.read(byteBuffer) != EOF) {
                byteBuffer.flip();
                size += descChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            return size;
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("readablebytechannel copy to writablebytechannel failure the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 输入流包装成BufferedReader
     *
     * @param inputStream 输入流
     * @param charset     字符集
     * @return BufferedReader
     */
    public static BufferedReader getReader(final InputStream inputStream, final String charset) {
        return getReader(inputStream, CharsetUtil.charset(charset));
    }

    /**
     * 输入流包装成BufferedReader
     *
     * @param inputStream 输入流
     * @param charset     字符集
     * @return BufferedReader
     */
    public static BufferedReader getReader(final InputStream inputStream, final Charset charset) {
        final InputStreamReader reader = Objects.isNull(charset) ? new InputStreamReader(inputStream, CharsetUtil.DEFAULT)
                : new InputStreamReader(inputStream, charset);
        return new BufferedReader(reader);
    }

    /**
     * reader装成BufferedReader
     *
     * @param reader reader
     * @return BufferedReader
     */
    public static BufferedReader getReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }


    /**
     * 输入流包装成BufferedWriter
     *
     * @param outputStream 输入流
     * @param charset      字符集
     * @return BufferedWriter
     */
    public static BufferedWriter getWriter(final OutputStream outputStream, final String charset) {
        return getWriter(outputStream, CharsetUtil.charset(charset));
    }

    /**
     * 输入流包装成BufferedWriter
     *
     * @param outputStream 输入流
     * @param charset      字符集
     * @return BufferedWriter
     */
    public static BufferedWriter getWriter(final OutputStream outputStream, final Charset charset) {
        final OutputStreamWriter writer = Objects.isNull(charset) ? new OutputStreamWriter(outputStream, CharsetUtil.DEFAULT)
                : new OutputStreamWriter(outputStream, charset);
        return new BufferedWriter(writer);
    }

    /**
     * writer包装成BufferedWriter
     *
     * @param writer writer
     * @return BufferedWriter
     */
    public static BufferedWriter getWriter(final Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    /**
     * 从留中读取字符串
     *
     * @param input   输入流
     * @param charset 编码
     * @return 字符串
     */
    public static String read(final InputStream input, final String charset) {
        final FastByteArrayOutputStream outputStream = read(input);
        return StringUtil.isBlank(charset) ? outputStream.toString() : StringUtil.str(outputStream.toByteArray(), charset);
    }

    /**
     * 从留中读取字符串
     *
     * @param input   输入流
     * @param charset 编码
     * @return 字符串
     */
    public static String read(final InputStream input, final Charset charset) {
        final FastByteArrayOutputStream outputStream = read(input);
        return Objects.isNull(charset) ? outputStream.toString() : StringUtil.str(outputStream.toByteArray(), charset);
    }

    /**
     * 从流中读取内容
     *
     * @param input 输入流
     * @return 输出流
     */
    public static FastByteArrayOutputStream read(final InputStream input) {
        final FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        copy(input, outputStream);
        return outputStream;
    }

    /**
     * 从reader中读取内容
     *
     * @param reader reader
     * @return 字符串
     */
    public static String read(final Reader reader) {
        final StringBuilder stringBuilder = StringUtil.builder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            while (-1 != reader.read(buffer)) {
                stringBuilder.append(buffer.flip().toString());
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("get string from reader failure,the reasone is: {}", e.getMessage()), e);
        }
    }

    /**
     * 从fileChannel中读取内容
     *
     * @param fileChannel fileChannel
     * @return 字符串
     */
    public static String readUTF8(final FileChannel fileChannel) {
        return read(fileChannel, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 从fileChannel中读取内容
     *
     * @param fileChannel fileChannel
     * @param charset     编码
     * @return 字符串
     */
    public static String read(final FileChannel fileChannel, final String charset) {
        return read(fileChannel, CharsetUtil.charset(charset));
    }

    /**
     * 从fileChannel中读取内容
     *
     * @param fileChannel fileChannel
     * @param charset     编码
     * @return 字符串
     */
    public static String read(final FileChannel fileChannel, final Charset charset) {
        try {
            final MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
            return StringUtil.str(byteBuffer, charset);
        } catch (IOException e) {
            throw new IORuntimeException(
                    StringUtil.format("get string from fileChannel failure,the reasone is: {}", e.getMessage()), e);
        }
    }

    /**
     * 从流中读取字节数组
     *
     * @param inputStream 流
     * @return 字节数组
     */
    public static byte[] readBytes(final InputStream inputStream) {
        final FastByteArrayOutputStream outputStream = read(inputStream);
        return outputStream.toByteArray();
    }

    /**
     * string转化为流
     *
     * @param content 字符串
     * @param charset 字符集
     * @return 流
     */
    public static ByteArrayInputStream toStream(final String content, final String charset) {
        return toStream(content, CharsetUtil.charset(charset));
    }

    /**
     * string转化为流
     *
     * @param content 字符串
     * @param charset 字符集
     * @return 流
     */
    public static ByteArrayInputStream toStream(final String content, final Charset charset) {
        final byte[] data = StringUtil.bytes(content, charset);
        return new ByteArrayInputStream(data);
    }

    /**
     * 将byte[]写到流中
     *
     * @param outputStream 输出流
     * @param isCloseOut   写入完毕是否关闭输出流
     * @param content      写入的内容
     */
    public static void write(final OutputStream outputStream, final boolean isCloseOut, final byte[] content) {
        try {
            outputStream.write(content);
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil.format("write byte array to outputstream failue,the reason is: {}", e.getMessage()), e);
        } finally {
            if (isCloseOut) {
                close(outputStream);
            }
        }
    }

    /**
     * 将多部分内容写到流中，自动转换为字符串
     *
     * @param outputStream 输出流
     * @param isClosed     是否关闭流
     * @param contents     内容
     */
    public static void writeUTF8(final OutputStream outputStream, final boolean isClosed, final Object... contents) {
        write(outputStream, CharsetUtil.CHARSET_UTF_8, isClosed, contents);
    }

    /**
     * 将多部分内容写到流中，自动转换为字符串
     *
     * @param outputStream 输出流
     * @param charset      字符集
     * @param isClosed     是否关闭流
     * @param contents     内容
     */
    public static void write(final OutputStream outputStream, final String charset, final boolean isClosed, final Object... contents) {
        write(outputStream, CharsetUtil.charset(charset), isClosed, contents);
    }

    /**
     * 将多部分内容写到流中，自动转换为字符串
     *
     * @param outputStream 输出流
     * @param charset      字符集
     * @param isClosed     是否关闭流
     * @param contents     内容
     */
    public static void write(final OutputStream outputStream, final Charset charset, final boolean isClosed, final Object... contents) {
        final BufferedWriter bufferedWriter = getWriter(outputStream, charset);
        try {
            for (final Object content : contents) {
                if (Objects.isNull(content)) {
                    continue;
                }
                bufferedWriter.write(StringUtil.str(content));
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil.format("write objects to outputstream failue,the reason is: {}", e.getMessage()), e);
        } finally {
            if (isClosed) {
                close(bufferedWriter);
            }
        }
    }

    /**
     * 关闭流
     *
     * @param closeable 流
     */
    public static void close(final Closeable closeable) {
        if (!Objects.isNull(closeable)) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new IORuntimeException(StringUtil.format("close stream failue,the reason is: {}", e.getMessage()), e);
            }
        }
    }

    /**
     * 关闭流
     *
     * @param closeable 流
     */
    public static void close(final AutoCloseable closeable) {
        if (!Objects.isNull(closeable)) {
            try {
                closeable.close();
            } catch (Exception e) {
                throw new IORuntimeException(StringUtil.format("close stream failue,the reason is: {}", e.getMessage()), e);
            }
        }
    }

    /**
     * 按行读取数据
     *
     * @param inputStream 输入流
     * @param collection  返回集合
     * @return 内容
     */
    public static <T extends Collection<String>> T readUTF8Lines(final InputStream inputStream, final T collection) {
        return readLines(getReader(getReader(inputStream, CharsetUtil.CHARSET_UTF_8)), collection);
    }

    /**
     * 按行读取数据
     *
     * @param inputStream 输入流
     * @param charset     字符集
     * @param collection  返回集合
     * @return 内容
     */
    public static <T extends Collection<String>> T readLines(final InputStream inputStream, final String charset, final T collection) {
        return readLines(getReader(getReader(inputStream, CharsetUtil.charset(charset))), collection);
    }

    /**
     * 按行读取数据
     *
     * @param inputStream 输入流
     * @param charset     字符集
     * @param collection  返回集合
     * @return 内容
     */
    public static <T extends Collection<String>> T readLines(final InputStream inputStream, final Charset charset, final T collection) {
        return readLines(getReader(getReader(inputStream, charset)), collection);
    }

    /**
     * 按行读取数据
     *
     * @param reader     reader
     * @param collection 返回集合
     * @return 内容
     */
    public static <T extends Collection<String>> T readLines(final Reader reader, final T collection) {
        readLines(reader, (LineHandler) line -> collection.add(line));
        return collection;
    }

    /**
     * 按行读取数据，针对每行的数据做处理
     *
     * @param inputStream   输入流
     * @param itemProcessor 处理器
     */
    public static void readUTF8Lines(final InputStream inputStream, final LineHandler itemProcessor) {
        readLines(getReader(inputStream, CharsetUtil.CHARSET_UTF_8), itemProcessor);
    }

    /**
     * 按行读取数据，针对每行的数据做处理
     *
     * @param inputStream   输入流
     * @param charset       字符集
     * @param itemProcessor 处理器
     */
    public static void readLines(final InputStream inputStream, final Charset charset, final LineHandler itemProcessor) {
        readLines(getReader(inputStream, charset), itemProcessor);
    }

    /**
     * 按行读取数据，针对每行的数据做处理
     *
     * @param reader        reader
     * @param itemProcessor 处理器
     */
    public static void readLines(final Reader reader, final LineHandler itemProcessor) {
        final BufferedReader bufferedReader = getReader(reader);
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                itemProcessor.handle(line);
            }
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil.format("read lines from reader and handle it failure,the reason is {}", e.getMessage()), e);
        }
    }


}
