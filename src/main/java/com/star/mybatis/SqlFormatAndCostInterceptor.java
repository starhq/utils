package com.star.mybatis;

import com.star.collection.CollectionUtil;
import com.star.log.Log;
import com.star.log.LogFactory;
import com.star.reflect.FieldUtil;
import com.star.string.StringUtil;
import com.star.time.DateTimeUtil;
import com.star.time.InstantUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 格式化sql，并输出耗时
 *
 * @author http://www.importnew.com/25166.html
 */
public class SqlFormatAndCostInterceptor implements Interceptor {
    /**
     * 引号
     */
    private static final String QUOTATION = "\"";
    /**
     * 问号
     */
    private static final String QUESTION = "\\?";
    /**
     * 集合
     */
    private static final String LIST = "list";
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.get(SqlFormatAndCostInterceptor.class);

    /**
     * 是否基本数据类型或者基本数据类型的包装类
     *
     * @param clazz 要判断的类
     * @return 是否基本类型
     */
    private static boolean isPrimitiveOrPrimitiveWrapper(final Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(Short.class) ||
                clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Long.class) ||
                clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(Float.class) ||
                clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(Boolean.class);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object target = invocation.getTarget();

        final long startTime = System.currentTimeMillis();
        final StatementHandler statementHandler = (StatementHandler) target;
        try {
            return invocation.proceed();
        } finally {
            final BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            final Object parameterObject = boundSql.getParameterObject();
            final List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

            // 格式化Sql语句，去除换行符，替换参数
            sql = formatSql(sql, parameterObject, parameterMappings);

            if (LOG.isDebugEnabled()) {
                LOG.debug("SQL: [{}],cost [{}] ms", sql, InstantUtil.getDiff(startTime));
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // nothing
    }

    @SuppressWarnings("unchecked")
    private String formatSql(final String sql, final Object obj, final List<ParameterMapping> parameterMappings) {
        String result = sql;
        if (!StringUtil.isBlank(result)) {
            result = beautifySql(result);
            if (!Objects.isNull(obj) && !CollectionUtil.isEmpty(parameterMappings)) {
                try {
                    if (obj instanceof DefaultSqlSession.StrictMap) {
                        final DefaultSqlSession.StrictMap<Collection<?>> strictMap = (DefaultSqlSession.StrictMap<Collection<?>>) obj;

                        if (strictMap.get(LIST) instanceof List) {
                            result = handleListParameter(result, strictMap.get(LIST));
                        }
                    } else if (obj instanceof Map) {
                        final Map<?, ?> paramMap = (Map<?, ?>) obj;
                        result = handleMapParameter(result, paramMap, parameterMappings);
                    } else {
                        result = handleCommonParameter(result, parameterMappings, obj);
                    }
                } catch (Exception e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("format sql failure,the reason is: {} ,use original sql: {}", e.getMessage(), result);
                    }
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 美化Sql
     *
     * @param sql 需要格式化的sql
     * @return 格式化好的sql
     */
    private String beautifySql(final String sql) {
        String result = sql;
        result = result.replace("\n", "").replace("\t", "").replace("  ", " ").replace("( ", "(").replace(" )", ")").replace(" ,", ",");
        return result;
    }

    /**
     * 处理参数为Map的场景的sql
     *
     * @param sql               要处理的sql
     * @param paramMap          参数
     * @param parameterMappings 参数映射
     * @return 格式化好的sql
     */
    private String handleMapParameter(final String sql, final Map<?, ?> paramMap, final List<ParameterMapping> parameterMappings) {
        String result = sql;
        for (final ParameterMapping parameterMapping : parameterMappings) {
            final Object propertyName = parameterMapping.getProperty();
            final Object propertyValue = paramMap.get(propertyName);
            if (!Objects.isNull(propertyValue)) {
                final String value = str(propertyValue);
                result = result.replaceFirst(QUESTION, value);
            }
        }

        return result;
    }

    /**
     * 处理参数为List的场景
     *
     * @param sql 要处理的sql
     * @param col 集合
     * @return 格式化好后的sql
     */
    private String handleListParameter(final String sql, final Collection<?> col) {
        String result = sql;
        if (!CollectionUtil.isEmpty(col)) {
            for (final Object obj : col) {
                final String value = str(obj);
                result = result.replaceFirst(QUESTION, value);
            }
        }

        return result;
    }

    /**
     * 处理通用的场景，处理sql
     *
     * @param sql               需要处理的sql
     * @param parameterMappings 参数映射
     * @param obj               参数值
     * @return 格式化好的sql
     */
    private String handleCommonParameter(final String sql, final List<ParameterMapping> parameterMappings, final Object obj) {
        String result = sql;
        String value;
        for (final ParameterMapping parameterMapping : parameterMappings) {
            value = str(obj);
            if (StringUtil.isBlank(value)) {
                final String propertyName = parameterMapping.getProperty();

                final Object val = FieldUtil.getFieldValue(obj, propertyName);
                value = str(val);
            }
            result = result.replaceFirst(QUESTION, value);
        }
        return result;
    }

    /**
     * 参数转字符串
     *
     * @param obj 参数
     * @return 字符串
     */
    private String str(final Object obj) {
        String propertyValue = null;
        if (isPrimitiveOrPrimitiveWrapper(obj.getClass())) {
            propertyValue = obj.toString();
        } else if (obj instanceof String) {
            propertyValue = QUOTATION + obj.toString() + QUOTATION;
        } else if (obj instanceof Date) {
            final LocalDateTime ldt = DateTimeUtil.getDateTime(InstantUtil.getInstant((Date) obj));
            propertyValue = QUOTATION + DateTimeUtil.format(ldt, DateTimeUtil.YMDHMS) + QUOTATION;
        } else if (obj instanceof LocalDateTime) {
            propertyValue = QUOTATION + DateTimeUtil.format((LocalDateTime) obj, DateTimeUtil.YMDHMS) + QUOTATION;
        } else if (obj instanceof Boolean) {
            propertyValue = (boolean) obj ? "1" : "0";
        }
        return propertyValue;
    }

}
