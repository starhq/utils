package com.star.regex;

import com.star.string.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理html的工具类
 *
 * @author starhq
 */
public final class HtmlUtil {

    /**
     * html正则
     */
    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";
    /**
     * js的正则
     */
    public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

    /**
     * html标签的正则
     */
    private final static String HTML = "<{}[^<>]*?\\s{}=['\"]?(.*?)['\"]?\\s.*?>";

    /**
     * html转义字符长度
     */
    private static final int TEXT_LENGTH = 64;

    /**
     * html转义字符
     */
    private static final char[][] TEXT = new char[TEXT_LENGTH][];

    static {
        for (int i = 0; i < TEXT_LENGTH; i++) {
            TEXT[i] = new char[]{(char) i};
        }

        // special HTML characters
        // 单引号 ('&apos;' doesn't work - it is not by the w3 specs)
        TEXT['\''] = "&#039;".toCharArray();
        // 双引号
        TEXT['"'] = StringUtil.HTML_QUOTE.toCharArray();
        // &符
        TEXT['&'] = StringUtil.HTML_AMP.toCharArray();
        // 小于号
        TEXT['<'] = StringUtil.HTML_LT.toCharArray();
        // 大于号
        TEXT['>'] = StringUtil.HTML_GT.toCharArray();

    }

    private HtmlUtil() {
    }

    /**
     * 还原被转义的HTML特殊字符
     *
     * @param htmlStr 包含转义符的HTML内容
     * @return 转换后的字符串
     */
    public static String restoreEscaped(final String htmlStr) {
        return htmlStr.replace("&#39;", "'").replace(StringUtil.HTML_LT, "<").replace(StringUtil.HTML_GT, ">")
                .replace(StringUtil.HTML_AMP, "&").replace(StringUtil.HTML_QUOTE, "\"")
                .replace(StringUtil.HTML_NBSP, " ");
    }

    /**
     * 转义文本中的HTML字符为安全的字符
     * <p>
     * &amp; with &amp;amp;(有点变扭)
     */
    public static String encode(final String text) {
        return encode(text, TEXT);
    }

    /**
     * 清除所有HTML标签
     *
     * @param content 文本
     * @return 清除标签后的文本
     */
    public static String cleanHtmlTag(final String content) {
        return content.replaceAll(RE_HTML_MARK, StringUtil.EMPTY);
    }

    /**
     * 清除指定HTML标签和被标签包围的内容<br>
     * 不区分大小写
     *
     * @param content  文本
     * @param tagNames 要清除的标签
     * @return 去除标签后的文本
     */
    public static String removeHtmlTag(final String content, final boolean withTagContent, final String... tagNames) {
        String regex1;
        String regex2;
        String result = content;
        for (final String tagName : tagNames) {
            if (StringUtil.isBlank(tagName)) {
                continue;
            }
            regex1 = StringUtil.format("(?i)<{}\\s?[^>]*?/>", tagName);
            if (withTagContent) {
                regex2 = StringUtil.format("(?i)(?s)<{}\\s*?[^>]*?>.*?</{}>", tagName, tagName);
            } else {
                regex2 = StringUtil.format("(?i)<{}\\s*?[^>]*?>|</{}>", tagName, tagName);
            }
            result = content.replaceAll(regex1, StringUtil.EMPTY).replaceAll(regex2, StringUtil.EMPTY);
        }
        return result;
    }

    /**
     * 文本转html
     *
     * @param txt 文本
     * @return html
     */
    public static String txt2htm(final String txt) {
        String result = txt;
        if (!StringUtil.isBlank(txt)) {
            final StringBuilder builder = StringUtil.builder((int) (txt.length() * 1.2));
            char cha;
            boolean doub = false;
            for (int i = 0; i < txt.length(); i++) {
                cha = txt.charAt(i);
                if (cha == StringUtil.C_SPACE) {
                    if (doub) {
                        builder.append(StringUtil.C_SPACE);
                        doub = false;
                    } else {
                        builder.append(StringUtil.HTML_NBSP);
                        doub = true;
                    }
                } else {
                    doub = false;
                    switch (cha) {
                        case '&':
                            builder.append("&amp;");
                            break;
                        case '<':
                            builder.append("&lt;");
                            break;
                        case '>':
                            builder.append("&gt;");
                            break;
                        case '"':
                            builder.append("&quot;");
                            break;
                        case '\n':
                            builder.append("<br/>");
                            break;
                        default:
                            builder.append(cha);
                            break;
                    }
                }
            }
            result = builder.toString();
        }
        return result;
    }

    /**
     * 去除HTML标签中的属性
     *
     * @param content 文本
     * @param attrs   属性名（不区分大小写）
     * @return 处理后的文本
     */
    public static String removeHtmlAttr(final String content, final String... attrs) {
        String result = content;
        String regex;
        for (final String attr : attrs) {
            regex = StringUtil.format("(?i)\\s*{}=([\"']).*?\\1", attr);
            result = result.replaceAll(regex, StringUtil.EMPTY);
        }
        return result;
    }

    /**
     * 去除指定标签的所有属性
     *
     * @param content  内容
     * @param tagNames 指定标签
     * @return 处理后的文本
     */
    public static String removeAllHtmlAttr(final String content, final String... tagNames) {
        String regex;
        String result = content;
        for (final String tagName : tagNames) {
            regex = StringUtil.format("(?i)<{}[^>]*?>", tagName);
            result = result.replaceAll(regex, StringUtil.format("<{}>", tagName));
        }
        return result;
    }

    /**
     * Encoder
     *
     * @param text  被编码的文本
     * @param array 特殊字符集合
     * @return 编码后的字符
     */
    private static String encode(final String text, final char[][] array) {
        StringBuilder builder;
        if (StringUtil.isBlank(text)) {
            builder = new StringBuilder();
        } else {
            final int len = text.length();
            builder = new StringBuilder(len + (len >> 2));
            for (int i = 0; i < len; i++) {
                final char current = text.charAt(i);
                if (current < TEXT_LENGTH) {
                    builder.append(array[current]);
                } else {
                    builder.append(current);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取指定HTML标签的指定属性的值
     *
     * @param source  html
     * @param element 标签
     * @param attr    属性
     * @return 值
     */
    public static List<String> match(final String source, final String element, final String attr) {
        final List<String> result = new ArrayList<>();
        final String reg = StringUtil.format(HTML, element, attr);
        final Matcher matcher = Pattern.compile(reg).matcher(source);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }
}
