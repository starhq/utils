package com.star.jdbc;

import com.star.exception.DbException;
import com.star.properties.Config;
import com.star.string.StringUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接管理类
 *
 * @author DbUtils
 */
public final class DbUtil {


    /**
     *
     */
    private DbUtil() {
    }

    /**
     * 根据传入的url动态获取连接
     *
     * @param url      地址
     * @param user     用户名
     * @param password 密码
     * @param driver   驱动
     * @return connection
     */
    public static Connection getConnection(final String url, final String user, final String password, final String driver) {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DbException(StringUtil.format("get connection failue,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 根据传入的url动态获取连接
     *
     * @return connection
     */
    public static Connection getConnection() {
        final Config config = new Config("config.properties");
        final String url = config.getString("jdbc.url");
        final String user = config.getString("jdbc.username");
        final String password = config.getString("jdbc.password");
        final String driver = config.getString("jdbc.driverClassName");

        return getConnection(url, user, password, driver);
    }


    /**
     * 关闭连接
     *
     * @param conn
     * @throws SQLException
     */
    public static void close(final Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * 关闭结果集
     *
     * @param resultSet 结果集
     * @throws SQLException
     */
    public static void close(final ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }

    /**
     * 关闭stmt
     *
     * @param stmt stmt
     * @throws SQLException
     */
    public static void close(final Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    /**
     * 静默关闭连接
     *
     * @param conn
     * @throws SQLException
     */
    public static void closeQuietly(final Connection conn) {
        try {
            close(conn);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * 禁播关闭所有连接
     *
     * @param conn      连接
     * @param stmt      stmt
     * @param resultSet 结果集
     */
    public static void closeQuietly(final Connection conn, final Statement stmt,
                                    final ResultSet resultSet) {

        try {
            closeQuietly(resultSet);
        } finally {
            try {
                closeQuietly(stmt);
            } finally {
                closeQuietly(conn);
            }
        }

    }

    /**
     * 禁播关闭结果集合
     *
     * @param resultSet 结果集
     */
    public static void closeQuietly(final ResultSet resultSet) {
        try {
            close(resultSet);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * 静默关闭stmt
     *
     * @param stmt stmt
     */
    public static void closeQuietly(final Statement stmt) {
        try {
            close(stmt);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * 提交并关闭连接
     *
     * @param conn 连接
     * @throws SQLException
     */
    public static void commitAndClose(final Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.commit();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * 静默提交并关闭连接
     *
     * @param conn 连接
     */
    public static void commitAndCloseQuietly(final Connection conn) {
        try {
            commitAndClose(conn);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }


    /**
     * 回滚
     *
     * @param conn 连接
     * @throws SQLException
     */
    public static void rollback(final Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    /**
     * 回滚并关闭
     *
     * @param conn 连接
     * @throws SQLException
     */
    public static void rollbackAndClose(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * 回滚并静默关闭
     *
     * @param conn 连接
     * @throws SQLException
     */
    public static void rollbackAndCloseQuietly(Connection conn) {
        try {
            rollbackAndClose(conn);
        } catch (SQLException e) { // NOPMD
            // quiet
        }
    }
}
