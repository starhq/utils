package com.star.io;

import com.star.string.StringUtil;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 字符集工具类
 *
 * @author starhq
 */
public final class CharsetUtil {

    /**
     * ISO-8859-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * UTF-8
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * GBK
     */
    public static final String GBK = "GBK";

    /**
     * ISO-8859-1
     */
    public static final Charset CHARSET_8859 = Charset.forName(ISO_8859_1);
    /**
     * UTF-8
     */
    public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);
    /**
     * GBK
     */
    public static final Charset CHARSET_GBK = Charset.forName(GBK);
    /**
     * default
     */
    public static final Charset DEFAULT = Charset.defaultCharset();

    private CharsetUtil() {
    }

    /**
     * 转换为Charset对象
     */
    public static Charset charset(final String charset) {
        return StringUtil.isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset);
    }

    /**
     * 转换为Charset对象
     */
    public static Charset charset(final Charset charset) {
        return Objects.isNull(charset) ? DEFAULT : charset;
    }

    /**
     * @param source      字符串
     * @param srcCharset  源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    public static String convert(final String source, final Charset srcCharset, final Charset destCharset) {
        final Charset src = Objects.isNull(srcCharset) ? CHARSET_8859 : srcCharset;
        final Charset dest = Objects.isNull(destCharset) ? CHARSET_UTF_8 : destCharset;
        return StringUtil.isBlank(source) || src.equals(dest) ? source
                : StringUtil.str(source.getBytes(src), dest);
    }

    /**
     * @param source      字符串
     * @param srcCharset  源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    public static String convert(final String source, final String srcCharset, final String destCharset) {
        return convert(source, charset(srcCharset), charset(destCharset));
    }
}
