package com.star.properties;

import com.star.exception.IORuntimeException;
import com.star.io.resource.ResourceUtil;
import com.star.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

/**
 * 属性文件读取工具类
 *
 * @sstarhq
 */
public class Config {

    /**
     * 私有属性，存储属性文件
     */
    private Properties props;

    /**
     * 构造器
     *
     * @param path 资源文件路径
     */
    public Config(String path) {
        final URL url = ResourceUtil.getResource(path);
        init(url);
    }

    /**
     * 构造器
     *
     * @param file 资源文件
     */
    public Config(File file) {
        if (Objects.requireNonNull(file).exists()) {
            try {
                final URL url = file.toURI().toURL();
                init(url);
            } catch (MalformedURLException e) {
                throw new IORuntimeException(StringUtil.format("read properties file failure: {}", e.getMessage()), e);
            }
        }
    }

    /**
     * 构造器
     *
     * @param path  路径
     * @param clazz 基准类
     */
    public Config(String path, Class<?> clazz) {
        final URL url = ResourceUtil.getResource(path, clazz);
        init(url);
    }

    /**
     * 初始化配置文件
     *
     * @param url 配置文件url
     */
    public void init(final URL url) {
        if (!Objects.isNull(url)) {
            props = new Properties();
            try (InputStream inputStream = url.openStream()) {
                props.load(inputStream);
            } catch (IOException e) {
                throw new IORuntimeException(StringUtil.format("read properties file failure: {}", e.getMessage()), e);
            }
        }
    }

    /**
     * 读取属性文件中字符串
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 字符串
     */
    public String getString(final String key, final String defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : value;
    }

    /**
     * 读取属性文件中字符串
     *
     * @param key 键
     * @return 字符串
     */
    public String getString(final String key) {
        return getString(key, StringUtil.EMPTY);
    }

    /**
     * 读取属性文件中的数字
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 数字
     */
    public int getInt(final String key, final int defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * 读取属性文件中的数字
     *
     * @param key 键
     * @return 数字
     */
    public int getInt(final String key) {
        return getInt(key, -1);
    }

    /**
     * 读取属性文件中的布尔值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public boolean getBool(final String key, final boolean defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    /**
     * 获取波尔型属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    public boolean getBool(final String key) {
        return getBool(key, false);
    }

    /**
     * 读取属性文件中的long值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public long getLong(final String key, final long defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Long.parseLong(value);
    }

    /**
     * 读取属性文件中的long值
     *
     * @param key 属性名
     * @return 属性值
     */
    public long getLong(final String key) {
        return getLong(key, -1L);
    }

    /**
     * 读取属性文件中的float值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public float getFloat(final String key, final float defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Float.parseFloat(value);
    }

    /**
     * 读取属性文件中的float值
     *
     * @param key 属性名
     * @return 属性值
     */
    public float getFloat(final String key) {
        return getFloat(key, -1f);
    }

    /**
     * 读取属性文件中的double值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public double getDouble(final String key, final double defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Double.parseDouble(value);
    }

    /**
     * 读取属性文件中的double值
     *
     * @param key 属性名
     * @return 属性值
     */
    public double getDouble(final String key) {
        return getDouble(key, -1D);
    }

    /**
     * 读取属性文件中的char值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public char getChar(final String key, final char defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : value.charAt(0);
    }

    /**
     * 读取属性文件中的double值
     *
     * @param key 属性名
     * @return 属性值
     */
    public double getChar(final String key) {
        return getChar(key, (char) 0);
    }

    /**
     * 读取属性文件中的short值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public short getShort(final String key, final short defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Short.parseShort(value);
    }

    /**
     * 读取属性文件中的double值
     *
     * @param key 属性名
     * @return 属性值
     */
    public short getShort(final String key) {
        return getShort(key, (short) 0);
    }

    /**
     * 读取属性文件中的byte值
     *
     * @param key          属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public byte getByte(final String key, final byte defaultValue) {
        final String value = props.getProperty(key);
        return StringUtil.isBlank(value) ? defaultValue : Byte.parseByte(value);
    }

    /**
     * 读取属性文件中的double值
     *
     * @param key 属性名
     * @return 属性值
     */
    public byte getByte(final String key) {
        return getByte(key, (byte) 0);
    }

    /**
     * 设置值，无给定键创建之。设置后未持久化
     *
     * @param key   属性键
     * @param value 属性值
     */
    public void setProperty(final String key, final Object value) {
        props.setProperty(key, value.toString());
    }

}
