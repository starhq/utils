package com.star.math;

import com.star.collection.array.ArrayUtil;
import com.star.string.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

/**
 * 精确计算
 *
 * @author http://wdbk.iteye.com/blog/836072
 */
public final class ArithmeticUtil {

    /**
     * 默认除法运算精度
     */
    private static final int DEFAUT_DIV_SCALE = 10;

    private ArithmeticUtil() {
    }

    /**
     * 提供精确的加法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double add(final float value1, final float value2) {
        return add(0, Float.toString(value1), Float.toString(value2)).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double add(final double value1, final double value2) {
        return add(0, Double.toString(value1), Double.toString(value2)).doubleValue();
    }

    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param scale  精度
     * @param values 多个被加值
     * @return 和
     */
    public static BigDecimal add(final int scale, final String... values) {
        return ArrayUtil.isEmpty(values) ? BigDecimal.ZERO : Stream.of(values).filter(s -> !StringUtil.isBlank(s)).map(BigDecimal::new).reduce
                (BigDecimal.ZERO, (bigDecimal1, bigDecimal2) -> scale > 0 ? bigDecimal1.add(bigDecimal2).setScale(scale, BigDecimal.ROUND_HALF_UP)
                        : bigDecimal1.add(bigDecimal2));
    }

    /**
     * 提供精确的减法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double subtract(final float value1, final float value2) {
        return subtract(0, Float.toString(value1), Float.toString(value2)).doubleValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double subtract(final double value1, final double value2) {
        return subtract(0, Double.toString(value1), Double.toString(value2)).doubleValue();
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param scale  精度
     * @param values 多个被减值
     * @return 和
     */
    public static BigDecimal subtract(final int scale, final String... values) {
        return ArrayUtil.isEmpty(values) ? BigDecimal.ZERO : Stream.of(values).filter(s -> !StringUtil.isBlank(s)).map(BigDecimal::new).reduce
                (BigDecimal.ZERO, (bigDecimal1, bigDecimal2) -> scale > 0 ? bigDecimal1.subtract(bigDecimal2).setScale(scale, BigDecimal
                        .ROUND_HALF_UP) : bigDecimal1.subtract(bigDecimal2));
    }

    /**
     * 提供精确的乘法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double multiply(final float value1, final float value2) {
        return multiply(0, Float.toString(value1), Float.toString(value2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double multiply(final double value1, final double value2) {
        return multiply(0, Double.toString(value1), Double.toString(value2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param scale  精度
     * @param values 多个被乘值
     * @return 和
     */
    public static BigDecimal multiply(final int scale, final String... values) {
        return ArrayUtil.isEmpty(values) ? BigDecimal.ZERO : Stream.of(values).filter(s -> !StringUtil.isBlank(s)).map(BigDecimal::new).reduce
                (BigDecimal.ZERO, (bigDecimal1, bigDecimal2) -> scale > 0 ? bigDecimal1.multiply(bigDecimal2).setScale(scale, BigDecimal
                        .ROUND_HALF_UP) : bigDecimal1.multiply(bigDecimal2));
    }

    /**
     * 提供精确的除法运算
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double divide(final float value1, final float value2) {
        return divide(Float.toString(value1), Float.toString(value2), DEFAUT_DIV_SCALE).doubleValue();
    }

    /**
     * 提供精确的除法运算
     *
     * @param value1 被除数
     * @param value2 除数
     * @return 商
     */
    public static double divide(final double value1, final double value2) {
        return divide(Double.toString(value1), Double.toString(value2), DEFAUT_DIV_SCALE).doubleValue();
    }

    /**
     * 提供精确的除法运算
     * <p>
     * scale大于0，精确到sacle位小数，小于0不做处理
     */
    public static BigDecimal divide(final String value1, final String value2, final int scale) {
        final BigDecimal bigDecimal1 = new BigDecimal(value1);
        final BigDecimal bigDecimal2 = new BigDecimal(value2);
        return scale > 0 ? bigDecimal1.divide(bigDecimal2, scale, RoundingMode.HALF_UP)
                : bigDecimal1.divide(bigDecimal2);
    }

    /**
     * 提供精确的取余数
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 和
     */
    public static double remainder(final float value1, final float value2) {
        return remainder(Float.toString(value1), Float.toString(value2), DEFAUT_DIV_SCALE).doubleValue();
    }

    /**
     * 提供精确的取余数
     *
     * @param value1 被除数
     * @param value2 除数
     * @return 商
     */
    public static double remainder(final double value1, final double value2) {
        return remainder(Double.toString(value1), Double.toString(value2), DEFAUT_DIV_SCALE).doubleValue();
    }

    /**
     * 提供精确的取余数
     * <p>
     * scale大于0，精确到sacle位小数，小于0不做处理
     */
    public static BigDecimal remainder(final String value1, final String value2, final int scale) {
        final BigDecimal bigDecimal1 = new BigDecimal(value1);
        final BigDecimal bigDecimal2 = new BigDecimal(value2);
        return scale > 0 ? bigDecimal1.divide(bigDecimal2, scale, RoundingMode.HALF_UP)
                : bigDecimal1.remainder(bigDecimal2);
    }

    /**
     * 提供精确的四舍五入
     * <p>
     * scale大于0，精确到sacle位小数，小于0不做处理
     *
     * @param value1 四舍五入的输
     * @return 精确的值
     */
    public static double round(final float value1) {
        return round(Float.toString(value1), DEFAUT_DIV_SCALE).doubleValue();
    }

    /**
     * 提供精确的四舍五入
     * <p>
     * scale大于0，精确到sacle位小数，小于0不做处理
     *
     * @param value1 四舍五入的输
     * @return 精确的值
     */
    public static double round(final double value1) {
        return round(Double.toString(value1), DEFAUT_DIV_SCALE).doubleValue();
    }

    /**
     * 提供精确的四舍五入
     * <p>
     * scale大于0，精确到sacle位小数，小于0不做处理
     *
     * @param value1 四舍五入的输
     * @param scale  精度
     * @return 精确的值
     */
    public static BigDecimal round(final String value1, final int scale) {
        final BigDecimal bigDecimal1 = new BigDecimal(value1);
        return bigDecimal1.setScale(scale, RoundingMode.HALF_UP);
    }
}
