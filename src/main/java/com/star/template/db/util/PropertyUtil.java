package com.star.template.db.util;

import com.star.properties.Config;
import com.star.string.StringUtil;
import com.star.template.db.constant.ConfigEnum;
import com.star.template.db.constant.ConnectionEnum;

/**
 * 风向下读取属性
 * <p>
 * Created by win7 on 2017/3/10.
 */
public final class PropertyUtil {

    private PropertyUtil() {
    }

    /**
     * 从属性文件中读取catalog
     *
     * @return catalog，如果没有返回空字符串
     */
    public static String getCatalog() {
        Config config = new Config("config.properties");
        return config.getString(ConfigEnum.JDBC_CATALOG.code, StringUtil.EMPTY);
    }

    /**
     * 从属性文件读取schema
     *
     * @return schema，如果没有返回空字符串
     */
    public static String getSchema() {
        Config config = new Config("config.properties");
        return config.getString(ConfigEnum.JDBC_SCHEMA.code, StringUtil.EMPTY);
    }

    /**
     * 数据库连接的用户名
     *
     * @return username 数据库用户名
     */
    public static String getUser() {
        Config config = new Config("config.properties");
        return config.getString(ConnectionEnum.USERNAME.code, "starhq");
    }

    /**
     * 数据库连接的用户名
     *
     * @return password 数据库密码
     */
    public static String getPassword() {
        Config config = new Config("config.properties");
        return config.getString(ConnectionEnum.PASSWORD.code, "12345678");
    }

    /**
     * 数据库连接的地址
     *
     * @return url 数据库地址
     */
    public static String getUrl() {
        Config config = new Config("config.properties");
        return config.getString(ConnectionEnum.URL.code, StringUtil.EMPTY);
    }

    /**
     * 数据库连接的地址
     *
     * @return url 数据库地址
     */
    public static String getDriver() {
        Config config = new Config("config.properties");
        return config.getString(ConnectionEnum.DIRVER.code, StringUtil.EMPTY);
    }

    /**
     * 数据库连接的地址
     *
     * @return prefix 前缀
     */
    public static String getPrefixes() {
        Config config = new Config("config.properties");
        return config.getString(ConfigEnum.TABLE_REMOVE_PREFIXES.code, StringUtil.EMPTY);
    }

    /**
     * 获得模板目录
     *
     * @return template 模板地址
     */
    public static String getTemplatePath() {
        Config config = new Config("config.properties");
        return config.getString(ConfigEnum.TEMPLATE.code, StringUtil.EMPTY);
    }

    /**
     * 获得输出目录
     *
     * @return template 模板地址
     */
    public static String getOutPath() {
        Config config = new Config("config.properties");
        return config.getString(ConfigEnum.OUT.code, StringUtil.EMPTY);
    }

    /**
     * 获得基本包
     *
     * @return template 模板地址
     */
    public static String getPackage() {
        Config config = new Config("config.properties");
        return config.getString(ConfigEnum.PACKAGE.code, StringUtil.EMPTY);
    }
}
