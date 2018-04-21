package com.star.jdbc;

import com.star.collection.array.ArrayUtil;
import com.star.exception.DbException;
import com.star.lang.ResultSetHandler;
import com.star.string.StringUtil;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Objects;

/**
 * sql执行器
 *
 * @author starhq
 */
public class SqlRunner {

    /**
     * 查询
     *
     * @param conn 连接
     * @param sql  sql
     * @param rsh  结果处理器
     * @param <T>  泛型
     * @return 查询对象
     */
    public <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rsh) {
        return query(conn, sql, rsh, false, (Object[]) null);
    }

    /**
     * 查询
     *
     * @param conn   连接
     * @param sql    sql
     * @param rsh    结果处理器
     * @param params 参数
     * @param <T>    泛型
     * @return 查询对象
     */
    public <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final Object... params) {
        return query(conn, sql, rsh, false, params);
    }

    /**
     * 查询
     *
     * @param conn      连接
     * @param sql       sql
     * @param rsh       结果处理器
     * @param closeConn 是否关闭连接
     * @param params    参数
     * @param <T>       泛型
     * @return 查询对象
     */
    private <T> T query(final Connection conn, final String sql, final ResultSetHandler<T> rsh, final boolean closeConn, final Object...
            params) {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = conn.prepareStatement(sql);
            fillParams(stmt, params);
            resultSet = stmt.executeQuery();
            return rsh.handle(resultSet);
        } catch (SQLException e) {
            throw new DbException(
                    StringUtil.format("run query sql: {} failure,the reason is: {}", sql, e.getMessage()), e);
        } finally {
            DbUtil.closeQuietly(null, stmt, resultSet);
            if (closeConn) {
                DbUtil.closeQuietly(conn);
            }
        }
    }

    /**
     * 插入更新删除
     *
     * @param conn 连接
     * @param sql  sql
     * @return 更新的行数
     */
    public int update(final Connection conn, final String sql) {
        return update(conn, sql, false, (Object[]) null);
    }

    /**
     * 插入更新删除
     *
     * @param conn   连接
     * @param sql    sql
     * @param params 参数
     * @return 更新的行数
     */
    public int update(final Connection conn, final String sql, final Object... params) {
        return update(conn, sql, false, params);
    }

    /**
     * 插入更新删除
     *
     * @param conn      连接
     * @param sql       sql
     * @param closeConn 是否关闭连接
     * @param params    参数
     * @return 更新的行数
     */
    public int update(final Connection conn, final String sql, final boolean closeConn, final Object... params) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            fillParams(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(
                    StringUtil.format("run update sql: {} failure,the reason is: {}", sql, e.getMessage()), e);
        } finally {
            DbUtil.closeQuietly(stmt);
            if (closeConn) {
                DbUtil.closeQuietly(conn);
            }
        }
    }


    /**
     * 插入数据返回id
     *
     * @param conn 连接
     * @param sql  sql
     * @param rsh  结果集处理器
     * @param <T>  泛型
     * @return 主键
     */
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh) {
        return insert(conn, false, sql, rsh);
    }

    /**
     * 插入数据返回id
     *
     * @param conn   连接
     * @param sql    sql
     * @param rsh    结果集处理器
     * @param params 参数
     * @param <T>    泛型
     * @return 主键
     */
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) {
        return insert(conn, false, sql, rsh, params);
    }

    /**
     * 插入数据返回id
     *
     * @param conn      连接
     * @param closeConn 是否关闭连接
     * @param sql       sql
     * @param rsh       结果集处理器
     * @param params    参数
     * @param <T>       泛型
     * @return 主键
     */
    public <T> T insert(final Connection conn, final boolean closeConn, final String sql, final ResultSetHandler<T> rsh, final Object... params) {
        PreparedStatement stmt = null;
        T generatedKeys = null;
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            fillParams(stmt, params);
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(resultSet);
        } catch (SQLException e) {
            throw new DbException(StringUtil
                    .format("insert data and get id failure,the reason is: {}", e.getMessage()), e);
        } finally {
            DbUtil.closeQuietly(stmt);
            if (closeConn) {
                DbUtil.closeQuietly(conn);
            }
        }

        return generatedKeys;
    }

    /**
     * 批处理
     *
     * @param conn   连接
     * @param sql    sql
     * @param params 参数
     * @return 影响的行数
     */
    public int[] batch(Connection conn, String sql, Object[][] params) {
        return this.batch(conn, false, sql, params);
    }

    /**
     * 批处理
     *
     * @param conn      连接
     * @param closeConn 是否关闭连接
     * @param sql       sql
     * @param params    参数
     * @return 影响的行数
     */
    public int[] batch(final Connection conn, final boolean closeConn, final String sql, final Object[]... params) {
        PreparedStatement stmt = null;
        int[] rows;
        try {
            stmt = conn.prepareStatement(sql);
            for (final Object[] param : params) {
                this.fillParams(stmt, param);
                stmt.addBatch();
            }
            rows = stmt.executeBatch();

        } catch (SQLException e) {
            throw new DbException(StringUtil
                    .format("batch execure sql {} failure,the reason is: {}", sql, e.getMessage()), e);
        } finally {
            DbUtil.closeQuietly(stmt);
            if (closeConn) {
                DbUtil.closeQuietly(conn);
            }
        }
        return rows;
    }

    /**
     * 批插入，返回主键
     *
     * @param conn   连接
     * @param sql    sql
     * @param rsh 结果集处理器
     * @param params 参数
     * @param <T>    范型
     * @return 影响的行数
     */
    public <T> T insertBatch(Connection conn, String sql, ResultSetHandler<T> rsh, Object[][] params) {
        return insertBatch(conn, false, sql, rsh, params);
    }

    /**
     * 批插入，返回主键
     *
     * @param conn      连接
     * @param closeConn 是否关闭连接
     * @param rsh 结果集处理器
     * @param sql       sql
     * @param params    参数
     * @param <T>       范型
     * @return 影响的行数
     */
    public <T> T insertBatch(final Connection conn, final boolean closeConn, final String sql, final ResultSetHandler<T> rsh, final Object[]...
            params) {
        PreparedStatement stmt = null;
        T generatedKeys = null;
        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (Object[] param : params) {
                this.fillParams(stmt, param);
                stmt.addBatch();
            }
            stmt.executeBatch();
            ResultSet rs = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(rs);

        } catch (SQLException e) {
            throw new DbException(StringUtil
                    .format("batch insert data failure,the reason is: {}", e.getMessage()), e);
        } finally {
            DbUtil.closeQuietly(stmt);
            if (closeConn) {
                DbUtil.closeQuietly(conn);
            }
        }

        return generatedKeys;
    }

    /**
     * 给PreparedStatement设置参数
     *
     * @param stmt   stmt
     * @param params 参数
     */
    private void fillParams(final PreparedStatement stmt, final Object... params) {
        if (ArrayUtil.isEmpty(params)) {
            return;
        }
        for (int i = 1; i <= params.length; i++) {
            final Object parameter = params[i - 1];
            if (Objects.isNull(parameter)) {
                int sqlType;
                try {
                    final ParameterMetaData pmd = stmt.getParameterMetaData();
                    sqlType = pmd.getParameterType(i);
                } catch (SQLException e) {
                    sqlType = Types.VARCHAR;
                }
                try {
                    stmt.setNull(i, sqlType);
                } catch (SQLException e) {
                    throw new DbException(StringUtil
                            .format("preparedStatement set parameter failue,the reason is: {}", e.getMessage()), e);
                }
            } else {
                try {
                    stmt.setObject(i, parameter);
                } catch (SQLException e) {
                    throw new DbException(StringUtil
                            .format("preparedStatement set parameter failue,the reason is: {}", e.getMessage()), e);
                }
            }
        }
    }
}
