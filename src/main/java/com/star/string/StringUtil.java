package com.star.string;

import com.star.collection.ArrayUtil;
import com.star.io.CharsetUtil;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 字符串操作工具类
 * <p>
 * Created by starhq on 2017/5/7.
 */
public final class StringUtil {

    /**
     * 空格char
     */
    public static final char C_SPACE = ' ';
    /**
     * tabchar
     */
    public static final char C_TAB = '	';
    /**
     * 点char
     */
    public static final char C_DOT = '.';
    /**
     * 斜杠char
     */
    public static final char C_SLASH = '/';
    /**
     * 反斜杠char
     */
    public static final char C_BACKSLASH = '\\';
    /**
     * 换行char
     */
    public static final char C_CR = '\r';
    /**
     * 新行char
     */
    public static final char C_LF = '\n';
    /**
     * 下划线char
     */
    public static final char C_UNDERLINE = '_';
    /**
     * 破折号char
     */
    public static final char C_DASH = '-';
    /**
     * 逗号char
     */
    public static final char C_COMMA = ',';
    /**
     * 花括号开始char
     */
    public static final char C_DELIM_START = '{';
    /**
     * 花括号结束char
     */
    public static final char C_DELIM_END = '}';
    /**
     * 大括号开始char
     */
    public static final char C_BRACE_START = '[';
    /**
     * 大括号结束char
     */
    public static final char C_BRACE_END = ']';
    /**
     * 小括号开始char
     */
    public static final char C_BRAKETS_START = '(';
    /**
     * 小括号结束char
     */
    public static final char C_BRAKETS_END = ')';
    /**
     * 冒号char
     */
    public static final char C_COLON = ':';
    /**
     * 空格
     */
    public static final String SPACE = " ";
    /**
     * tab
     */
    public static final String TAB = "	";
    /**
     * 点
     */
    public static final String DOT = ".";
    /**
     * 两个点
     */
    public static final String DOUBLE_DOT = "..";
    /**
     * 斜杠
     */
    public static final String SLASH = "/";
    /**
     * 反斜杠
     */
    public static final String BACKSLASH = "\\";
    /**
     * 空字符串
     */
    public static final String EMPTY = "";
    /**
     * 回车
     */
    public static final String NEWLINE = "\r";
    /**
     * 换行
     */
    public static final String LINEFEED = "\n";
    /**
     * 回车加换行
     */
    public static final String CRLF = NEWLINE + LINEFEED;
    /**
     * 下滑线
     */
    public static final String UNDERLINE = "_";
    /**
     * 破折号
     */
    public static final String DASH = "-";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 大括号开始
     */
    public static final String DELIM_START = "{";
    /**
     * 大括号结束
     */
    public static final String DELIM_END = "}";
    /**
     * 大括号开始
     */
    public static final String BRACE_START = "[";
    /**
     * 大括号结束
     */
    public static final String BRACE_END = "]";
    /**
     * 小括号开始
     */
    public static final String BRAKETS_START = "(";
    /**
     * 小括号结束
     */
    public static final String BRAKETS_END = ")";
    /**
     * 冒号
     */
    public static final String COLON = ":";
    /**
     * 空格转义
     */
    public static final String HTML_NBSP = "&nbsp;";
    /**
     * 分号转义
     */
    public static final String HTML_AMP = "&amp";
    /**
     * 引号转义
     */
    public static final String HTML_QUOTE = "&quot;";
    /**
     * 小于转义
     */
    public static final String HTML_LT = "&lt;";
    /**
     * 大于转义
     */
    public static final String HTML_GT = "&gt;";
    /**
     * 分页符
     */
    public static final String PAGEBREAKS = "\f";
    /**
     * 空json
     */
    public static final String EMPTY_JSON = DELIM_START + DELIM_END;

    private StringUtil() {
    }

    /**
     * 判断字符串是否为空白
     *
     * @param sequence 需要判断的字符串
     * @return 是否为空白
     */
    public static boolean isBlank(final CharSequence sequence) {
        boolean result = true;
        if (!isEmpty(sequence)) {
            final int length = sequence.length();
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(sequence.charAt(i))) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 是否为空字符串
     *
     * @param sequence 要判断的字符串
     * @return 是否为空字符串
     */
    public static boolean isEmpty(final CharSequence sequence) {
        return Objects.isNull(sequence) || sequence.length() == 0;
    }

    /**
     * 字符串是否以前缀开头
     *
     * @param str          字符串
     * @param prefix       前缀
     * @param isIgnoreCase 忽视大小写
     * @return 是否前缀开头
     */
    public static boolean startWith(final String str, final String prefix, final boolean isIgnoreCase) {
        return !isBlank(str) && !isBlank(prefix) && str.regionMatches(isIgnoreCase, 0, prefix, 0, prefix.length());
    }

    /**
     * 字符串是否以后缀结尾
     *
     * @param str          字符串
     * @param suffix       后缀
     * @param isIgnoreCase 忽视大小写
     * @return 是否后缀结尾
     */
    public static boolean endWith(final String str, final String suffix, final boolean isIgnoreCase) {
        return !isBlank(str) && !isBlank(suffix) && str.regionMatches(isIgnoreCase, str.length() - suffix.length(),
                suffix, 0, suffix.length());
    }

    /**
     * 大小写首字母
     *
     * @param str   给定字符串
     * @param upper 是否大小写
     * @return 修改后的字符串
     */
    public static String upperOrLowerFirst(final String str, final boolean upper) {
        return isBlank(str) ? EMPTY : upper ? Character.toUpperCase(str.charAt(0)) + str.substring(1)
                : Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 删除前缀
     *
     * @param str    指定字符串
     * @param prefix 前缀
     * @return 修改后的字符串
     */
    public static String removePrefix(final String str, final String prefix, final boolean isIgnoreCase) {
        return startWith(str, prefix, isIgnoreCase) ? sub(str, prefix.length(), str.length()) : str;
    }

    /**
     * 删除后缀
     *
     * @param str    指定字符串
     * @param suffix 后缀
     * @return 修改后的字符串
     */
    public static String removeSuffix(final String str, final String suffix, final boolean isIgnorCase) {
        return endWith(str, suffix, isIgnorCase) ? sub(str, str.length() - suffix.length(), str.length()) : str;
    }

    /**
     * 如果str为空，用默认值defaultstr来替代
     *
     * @param str        需要判断的字符串
     * @param defaultStr 默认值字符串
     * @return 字符串
     */
    public static String defaultIfEmpty(final String str, final String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 切分字符串的plus版本
     *
     * @param str       原始字符串
     * @param delimiter 分隔符
     * @return 分割后的数组
     */
    public static String[] split(final String str, final String delimiter) {
        String[] result;
        if (isBlank(str)) {
            result = new String[0];
        } else if (isBlank(delimiter)) {
            result = new String[]{str};
        } else {
            final int dellen = delimiter.length();
            final int maxparts = str.length() / dellen + 2;

            int[] positions = new int[maxparts];
            Arrays.fill(positions, -dellen);

            int start;
            int end = 0;
            int count = 0;
            while ((start = str.indexOf(delimiter, end)) != -1) {
                count++;
                positions[count] = start;
                end = start + dellen;
            }


            count++;
            positions[count] = str.length();

            result = new String[count];

            for (start = 0; start < count; start++) {
                final int left = positions[start] + dellen;
                final int right = positions[start + 1];
                result[start] = sub(str, left, right);
            }
        }
        return result;
    }

    /**
     * 按定长切割字符串
     *
     * @param str 字符串
     * @param len 长度
     * @return 字符串数组
     */
    public static String[] split(final String str, final int len) {
        String[] strs;
        if (isBlank(str) || len <= 0) {
            strs = new String[0];
        } else {
            final int strlen = str.length();
            final int last = str.length() % len;
            final int length = last == 0 ? strlen / len : strlen / len + 1;
            strs = new String[length];
            for (int i = 0; i < length; i++) {
                final int left = i * len;
                final int right = left + (i == length - 1 && last != 0 ? last : len);
                strs[i] = sub(str, left, right);
            }
        }
        return strs;
    }


    /**
     * substring的plus版本
     *
     * @param string    需要截取的字符串
     * @param fromIndex 开始索引
     * @param toIndex   结束索引
     * @return 截取后的字符串
     */
    public static String sub(final String string, final int fromIndex, final int toIndex) {
        String result;
        if (isBlank(string)) {
            result = EMPTY;
        } else {
            int start = fromIndex < 0 ? string.length() + fromIndex : fromIndex;
            int end = toIndex < 0 ? string.length() + toIndex : toIndex == 0 && fromIndex < 0 ? string.length() :
                    toIndex;

            if (end < start) {
                start = start ^ end;
                end = start ^ end;
                start = start ^ end;
            }

            result = string.substring(start, end);
        }
        return result;
    }

    /**
     * substring的plus版本
     *
     * @param str       需要截取的字符串
     * @param fromIndex 开始索引
     * @return 截取后的字符串
     */
    public static String sub(final String str, final int fromIndex) {
        return sub(str, fromIndex, str.length());
    }


    /**
     * 格式化文本
     *
     * @param template 格式化模板
     * @param values   要设置的参数
     * @return 格式化好的字符串
     */
    public static String format(final String template, final Object... values) {
        String result;
        if (isBlank(template)) {
            result = EMPTY;
        } else if (ArrayUtil.isEmpty(values)) {
            result = template;
        } else {
            final StringBuilder stringBuilder = new StringBuilder();
            final int length = template.length();

            int valueIndex = 0;
            char currentChar;
            for (int i = 0; i < length; i++) {
                if (valueIndex >= values.length) {
                    stringBuilder.append(sub(template, i, length));
                    break;
                }

                currentChar = template.charAt(i);
                if (currentChar == C_DELIM_START) {
                    final char nextChar = template.charAt(++i);
                    if (nextChar == C_DELIM_END) {
                        stringBuilder.append(values[valueIndex++]);
                    } else {
                        stringBuilder.append(C_DELIM_START).append(nextChar);
                    }
                } else {
                    stringBuilder.append(currentChar);
                }

            }

            result = stringBuilder.toString();
        }
        return result;
    }


    /**
     * 格式化文本
     *
     * @param template 要格式化的模板
     * @param map      键值对,键要和模板中的参数对饮起来
     * @return 格式化好的字符串
     */
    public static String format(final String template, final Map<?, ?> map) {
        String str;
        if (isBlank(template)) {
            str = EMPTY;
        } else {
            str = template;
            for (final Map.Entry<?, ?> entry : map.entrySet()) {
                str = str.replace(DELIM_START + entry.getKey() + DELIM_END, str(entry.getValue(), CharsetUtil.UTF_8));
            }
        }
        return str;
    }

    /**
     * java命名改成带下划线的命名方式
     * <p>
     * HelloWorld-&gt;hello_world
     *
     * @param camelCaseStr 驼峰名
     * @return 带下划线的名
     */
    public static String toUnderlineCase(final String camelCaseStr) {
        String result;
        if (isBlank(camelCaseStr)) {
            result = EMPTY;
        } else {
            final StringBuilder builder = builder();
            final char[] chars = camelCaseStr.toCharArray();
            final int len = chars.length;
            for (int i = 0; i < len; i++) {
                builder.append(toUpperOrLowwer(chars[i], false));
                if (i < len - 1 && Character.isUpperCase(chars[i + 1])) {
                    builder.append(C_UNDERLINE);
                }
            }
            result = builder.toString();
        }
        return result;
    }


    /**
     * 下划线命名改成驼峰命名
     * <p>
     * hello_world-&gt;helloWorld
     *
     * @param name 下划线命名
     * @return 驼峰
     */
    public static String toCamelCase(final String name) {
        Objects.requireNonNull(name, "input string can not be null");
        String result;
        if (name.contains(UNDERLINE)) {
            final StringBuilder builder = builder(name.length());
            final char[] chars = name.toCharArray();
            final int len = chars.length;
            for (int i = 0; i < len; i++) {
                if (chars[i] == C_UNDERLINE) {
                    continue;
                }

                builder.append(toUpperOrLowwer(chars[i], i != 0 && chars[i - 1] == C_UNDERLINE));
            }
            result = builder.toString();
        } else {
            result = name;
        }
        return result;
    }

    private static char toUpperOrLowwer(final char temp, final boolean upper) {
        return upper ? Character.toUpperCase(temp) : Character.toLowerCase(temp);
    }


    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(final String str, final String charset) {
        return bytes(str, CharsetUtil.charset(charset));
    }


    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(final String str, final Charset charset) {
        byte[] bytes;
        if (isBlank(str)) {
            bytes = new byte[0];
        } else {
            bytes = Objects.isNull(charset) ? str.getBytes() : str.getBytes(charset);
        }
        return bytes;
    }


    /**
     * 对象转字符串
     *
     * @param obj     对象
     * @param charset 编码
     * @return 字符串
     */
    public static String str(final Object obj, final String charset) {
        return str(obj, CharsetUtil.charset(charset));
    }

    /**
     * 对象转字符串
     *
     * @param obj     对象
     * @param charset 编码
     * @return 字符串
     */
    public static String str(final Object obj, final Charset charset) {
        String result;
        if (Objects.isNull(obj)) {
            result = EMPTY;
        } else if (obj instanceof String) {
            result = (String) obj;
        } else if (obj instanceof byte[]) {
            result = str((byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            result = str((ByteBuffer) obj, charset);
        } else if (ArrayUtil.isArray(obj)) {
            result = ArrayUtil.toString(obj);
        } else {
            result = obj.toString();
        }
        return result;
    }

    /**
     * byte转string
     */
    public static String str(final byte[] data, final String charset) {
        return str(data, CharsetUtil.charset(charset));
    }

    /**
     * byte转string
     */
    public static String str(final byte[] data, final Charset charset) {
        String result;
        if (ArrayUtil.isEmpty(data)) {
            result = EMPTY;
        } else {
            result = Objects.isNull(charset) ? new String(data) : new String(data, charset);
        }
        return result;
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(final ByteBuffer data, final String charset) {
        return str(data, CharsetUtil.charset(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(final ByteBuffer data, final Charset charset) {
        String result;
        if (Objects.isNull(data)) {
            result = EMPTY;
        } else {
            result = CharsetUtil.charset(charset).decode(data).toString();
        }
        return result;
    }

    /**
     * 字符串转换为byteBuffer
     *
     * @param str     字符串
     * @param charset 编码
     * @return 包装好的bytebufffer
     */
    public static ByteBuffer byteBuffer(final String str, final String charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }

    /**
     * 按指定格式组装string
     *
     * @param start     开始符
     * @param end       结尾符
     * @param delimiter 分隔符
     * @param charset   编码
     * @param objects   需要拼接的对象
     * @return 组装好的字符串
     */
    public static String join(final String start, final String end, final String delimiter, final Charset charset,
                              final Object... objects) {
        final StringJoiner joiner = joiner(start, end, delimiter);
        return getString(joiner, objects, charset);
    }

    /**
     * 按指定格式组装string
     *
     * @param start     开始符
     * @param end       结尾符
     * @param delimiter 分隔符
     * @param objects   需要拼接的对象
     * @return 组装好的字符串
     */
    public static String join(final String start, final String end, final String delimiter, final Object... objects) {
        final StringJoiner joiner = joiner(start, end, delimiter);
        return getString(joiner, objects, CharsetUtil.charset(CharsetUtil.UTF_8));
    }

    private static String getString(final StringJoiner joiner, final Object[] objects, final Charset charset) {
        for (final Object obj : objects) {
            joiner.add(str(obj, charset));
        }
        return str(joiner, charset);
    }


    /**
     * 按指定格式组装string
     *
     * @param delimiter 分隔符
     * @param objects   需要拼接的对象
     * @return 组装好的字符串
     */
    public static String join(final String delimiter, final Objects... objects) {
        return join(EMPTY, EMPTY, delimiter, objects);
    }

    private static StringJoiner joiner(final String start, final String end, final String delimiter) {
        return new StringJoiner(defaultIfEmpty(delimiter, EMPTY), defaultIfEmpty(start, EMPTY), defaultIfEmpty(end,
                EMPTY));
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder(final int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 动态创建stringbuilder
     *
     * @param strings 字符串
     * @return builder
     */
    public static StringBuilder builder(final String... strings) {
        final StringBuilder builder = builder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i]);
        }
        return builder;
    }


    /**
     * 获得StringReader
     *
     * @param str 字符串
     * @return StringReader
     */
    public static StringReader getReader(final String str) {
        return new StringReader(Objects.requireNonNull(str, "str can not be null"));
    }

    /**
     * 获得StringWriter
     *
     * @return StringWriter
     */
    public static StringWriter getWriter() {
        return new StringWriter();
    }

}