package com.star.template.db.util;

import com.star.string.StringUtil;
import com.star.template.db.model.Column;

/**
 * 列工具类
 */
public final class ColumnUtil {

    private ColumnUtil() {
    }

    /**
     * 得到JSR303 bean validation的验证表达式
     *
     * @param column 列
     * @return 验证表达式
     */
    public static String getHibernateValidatorExpression(final Column column) {
        if (!column.getIsPK() && !column.getIsNullable()) {
            if (TypeUtil.isString(column.getJavaType())) {
                return "@NotBlank " + getNotRequiredHibernateValidatorExpression(column);
            } else {
                return "@NotNull " + getNotRequiredHibernateValidatorExpression(column);
            }
        } else {
            return getNotRequiredHibernateValidatorExpression(column);
        }
    }

    public static String getNotRequiredHibernateValidatorExpression(final Column column) {
        StringBuilder builder = StringUtil.builder();
        if (column.getSqlName().contains("mail")) {
            builder.append("@Email");
        }
        if (TypeUtil.isString(column.getJavaType()) && column.getSize() > 0) {
            builder.append(StringUtil.format("@Length(max={})", column.getSize()));
        }
        if (TypeUtil.isIntegerNumber(column.getJavaType())) {
            if (column.getJavaType().endsWith("short")) {
                builder.append(StringUtil.format("@Max({})", Short.MAX_VALUE));
            } else if (column.getJavaType().endsWith("byte")) {
                builder.append(StringUtil.format("@Max({})", Byte.MAX_VALUE));
            }
        }
        return builder.toString().trim();
    }
}
