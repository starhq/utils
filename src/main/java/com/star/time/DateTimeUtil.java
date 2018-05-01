package com.star.time;

import com.star.string.StringUtil;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * jdk包时间工具类
 * <p>
 * java.time
 *
 * @author starhq
 */
public final class DateTimeUtil {


    /**
     * 毫秒
     */
    public final static long MILLESECOND = 1;
    /**
     * 每秒钟的毫秒数
     */
    public final static long SECOND_MS = MILLESECOND * 1000;
    /**
     * 每分钟的毫秒数
     */
    public final static long MINUTE_MS = SECOND_MS * 60;
    /**
     * 每小时的毫秒数
     */
    public final static long HOUR_MS = MINUTE_MS * 60;
    /**
     * 每天的毫秒数
     */
    public final static long DAY_MS = HOUR_MS * 24;

    /**
     * 标准日期时间格式，精确到分
     */
    public final static String YMD = "yyyy-MM-dd";
    /**
     * 标准日期时间格式，精确到分
     */
    public final static String YMDHM = "yyyy-MM-dd HH:mm";
    /**
     * 标准日期时间格式，精确到秒
     */
    public final static String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 标准日期时间格式，精确到毫秒
     */
    public final static String YMDHMSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * HTTP头中日期时间格式
     */
    public final static String HEADER = "EEE, dd MMM yyyy HH:mm:ss z";
    /**
     * 标准时间格式
     */
    public final static String HMS = "HH:mm:ss";

    private DateTimeUtil() {
    }

    /**
     * instant转localdatetime
     *
     * @param instant 瞬时时间
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getDateTime(final Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * localdatetime转localdate
     *
     * @param ldt 时间
     * @return {@link LocalDate}
     */
    public static LocalDate getLocalDate(final LocalDateTime ldt) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        return date.toLocalDate();
    }

    /**
     * localdatetime转localtime
     *
     * @param ldt 时间
     * @return {@link LocalTime}
     */
    public static LocalTime getLocalTime(final LocalDateTime ldt) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        return date.toLocalTime();
    }

    //=================================convert==============================================

    /**
     * 获得年
     *
     * @param ldt 时间
     * @return 年
     */
    public static int getYear(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().getYear() : ldt.getYear();
    }

    /**
     * 获得月份
     *
     * @param ldt 时间
     * @return 月份
     */
    public static Month getMonth(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().getMonth() : ldt.getMonth();
    }

    /**
     * 获得季度
     *
     * @param ldt 时间
     * @return 月份
     */
    public static int getSeason(final LocalDateTime ldt) {
        final Month month = getMonth(ldt);
        int season = 0;
        if (month.compareTo(Month.MARCH) <= 0) {
            season = 1;
        } else if (month.compareTo(Month.JUNE) <= 0) {
            season = 2;
        } else if (month.compareTo(Month.SEPTEMBER) <= 0) {
            season = 3;
        } else {
            season = 4;
        }
        return season;
    }

    /**
     * 获得指定日期是所在年份的第几周<br>
     *
     * @param ldt 时间
     * @return 周
     */
    public static int getWeekOfYear(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR) : ldt.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    /**
     * 获得指定日期是所在月份的第几周<br>
     *
     * @param ldt 时间
     * @return 周
     */
    public static int getWeekOfMonth(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().get(ChronoField.ALIGNED_WEEK_OF_MONTH) : ldt.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
    }

    /**
     * 获得指定日期是所在月份的第几周<br>
     *
     * @param ldt 时间
     * @return 周
     */
    public static int getDayOfMonth(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().getDayOfMonth() : ldt.getDayOfMonth();
    }

    /**
     * 获得指定日期是星期几，1表示周日，2表示周一
     *
     * @param ldt 时间
     * @return 周
     */
    public static DayOfWeek getDayOfWeek(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().getDayOfWeek() : ldt.getDayOfWeek();
    }

    /**
     * 获得指定日期的小时数部分<br>
     *
     * @param ldt           日期
     * @param is24HourClock 是否24小时制
     * @return 小时数
     */
    public static int getHour(final LocalDateTime ldt, final boolean is24HourClock) {
        return Objects.isNull(ldt) ? LocalDateTime.now().get(is24HourClock ? ChronoField.HOUR_OF_DAY : ChronoField.HOUR_OF_AMPM) : ldt.get
                (is24HourClock ? ChronoField
                        .HOUR_OF_DAY : ChronoField.HOUR_OF_AMPM);
    }

    /**
     * 获得指定日期的分钟数部分<br>
     * 例如：10:04:15.250 =》 4
     *
     * @param ldt 日期
     * @return 分钟数
     */
    public static int getMinute(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().getMinute() : ldt.getMinute();
    }

    /**
     * 获得指定日期的秒数部分<br>
     *
     * @param ldt 日期
     * @return 秒数
     */
    public static int getSsecond(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().getSecond() : ldt.getSecond();
    }

    /**
     * 获得指定日期的毫秒数部分<br>
     *
     * @param ldt 日期
     * @return 毫秒数
     */
    public static int getMillsecond(final LocalDateTime ldt) {
        return Objects.isNull(ldt) ? LocalDateTime.now().get(ChronoField.MILLI_OF_SECOND) : ldt.get(ChronoField.MILLI_OF_SECOND);
    }

    //=================================statistics==============================================

    /**
     * 格式化时间
     * ldt为空采用默认当前时间，dtf为空采用yyyy-MM-ddTHH:mm:ss
     *
     * @param ldt     时间
     * @param pattern 格式化字符串
     * @return 格式化好的字符串
     */
    public static String format(final LocalDateTime ldt, final String pattern) {
        return format(ldt, StringUtil.isBlank(pattern) ? DateTimeFormatter.ofPattern(YMDHMS) : DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param ldt 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDateTime(final LocalDateTime ldt) {
        return format(ldt, YMDHMS);
    }


    /**
     * 格式化为Http的标准日期格式
     *
     * @param ldt 被格式化的日期
     * @return HTTP标准形式日期字符串
     */
    public static String formatHttpDate(final LocalDateTime ldt) {
        return format(ldt, HEADER);
    }

    /**
     * 格式化时间
     * ldt为空采用默认当前时间，dtf为空采用yyyy-MM-dd HH:mm:ss
     *
     * @param ldt 时间
     * @param dtf 格式化
     * @return 格式化好的字符串
     */
    public static String format(final LocalDateTime ldt, final DateTimeFormatter dtf) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        final DateTimeFormatter format = Objects.isNull(dtf) ? DateTimeFormatter.ofPattern(YMDHMS) : dtf;
        return date.format(format);
    }
    //=================================format==============================================

    /**
     * 解析时间字符串
     * <p>
     * dtf为空采用yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 时间字符串
     * @param dtf        格式化
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(final String dateString, final String dtf) {
        return parse(dateString, Objects.isNull(dtf) ? DateTimeFormatter.ofPattern(YMDHMS) : DateTimeFormatter.ofPattern(dtf));
    }

    /**
     * 格式yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 标准形式的时间字符串
     * @return 日期对象
     */
    public static LocalDateTime parseDateTime(final String dateString) {
        return parse(dateString, YMDHMS);
    }

    /**
     * 格式yyyy-MM-dd
     *
     * @param dateString 标准形式的日期字符串
     * @return 日期对象
     */
    public static LocalDateTime parseDate(final String dateString) {
        return parse(dateString, YMD);
    }


    /**
     * 解析时间字符串
     * <p>
     * dtf为空采用yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 时间字符串
     * @param dtf        格式化
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(final String dateString, final DateTimeFormatter dtf) {
        final DateTimeFormatter format = Objects.isNull(dtf) ? DateTimeFormatter.ofPattern(YMDHMS) : dtf;
        return LocalDateTime.parse(Objects.requireNonNull(dateString, "dateString can not be null"), format);
    }
    //=================================parse==============================================


    /**
     * 午夜
     * <p>
     * ldt为空采用默认当前时间
     *
     * @param ldt 时间
     * @return 指定时间的00：00：00
     */
    public static LocalDateTime getMidnight(final LocalDateTime ldt) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        return date.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * 23:59:59
     * <p>
     * ldt为空采用默认当前时间
     *
     * @param ldt 时间
     * @return 指定时间的23：59：59
     */
    public static LocalDateTime getEndOfDay(final LocalDateTime ldt) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        return date.withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 获取某周的开始时间
     *
     * @param ldt                日期
     * @param isMondayAsFirstDay 周一是否第一天
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getBeginOfWeek(final LocalDateTime ldt, final boolean isMondayAsFirstDay) {
        return getMidnight(ldt).with(isMondayAsFirstDay ? DayOfWeek.MONDAY : DayOfWeek.SUNDAY);
    }

    /**
     * 获取某周的结束时间
     *
     * @param ldt                日期
     * @param isMondayAsFirstDay 周一是否第一天
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getEndOfWeek(final LocalDateTime ldt, final boolean isMondayAsFirstDay) {
        return getEndOfDay(ldt).with(isMondayAsFirstDay ? DayOfWeek.SUNDAY : DayOfWeek.SATURDAY);
    }

    /**
     * 获取某月的开始时间
     *
     * @param ldt 日期
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getBeginOfMonth(final LocalDateTime ldt) {
        return getMidnight(ldt).withDayOfMonth(1);
    }

    /**
     * 获取某月的结束时间
     *
     * @param ldt 日期
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getEndOfMonth(final LocalDateTime ldt) {
        return getEndOfDay(ldt).withDayOfMonth(getLocalDate(ldt).lengthOfMonth());
    }

    /**
     * 获取某年的开始时间
     *
     * @param ldt 日期
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getBeginOfYear(final LocalDateTime ldt) {
        return getMidnight(ldt).withDayOfYear(1);
    }

    /**
     * 获取某年的结束时间
     *
     * @param ldt 日期
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getEndOfYear(final LocalDateTime ldt) {
        return getEndOfDay(ldt).withDayOfYear(getLocalDate(ldt).lengthOfYear());
    }
    //=================================begin & end==============================================

    /**
     * 指定时间增加一定偏移量
     * <p>
     * ldt为空采用默认当前时间
     *
     * @param ldt         时间
     * @param unit        计算单位
     * @param amountToAdd 数量
     * @return 计算后的时间
     */
    public static LocalDateTime plus(final LocalDateTime ldt, final ChronoUnit unit, final long amountToAdd) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        return date.plus(amountToAdd, unit);
    }

    /**
     * 指定时间减少一定偏移量
     * <p>
     * ldt为空采用默认当前时间
     *
     * @param ldt         时间
     * @param unit        计算单位
     * @param amountToAdd 数量
     * @return 计算后的时间
     */
    public static LocalDateTime minus(final LocalDateTime ldt, final ChronoUnit unit, final long amountToAdd) {
        final LocalDateTime date = Objects.isNull(ldt) ? LocalDateTime.now() : ldt;
        return date.minus(amountToAdd, unit);
    }

    /**
     * 两个时间点相差多少时间单位
     * <p>
     * 开始时间为空采用当前时间,截至时间为空抛异常
     *
     * @param start 开始
     * @param end   结束
     * @param unit  计算单位
     * @return 时间差
     */
    public static long dateDiff(final LocalDateTime start, final LocalDateTime end, final ChronoUnit unit) {
        final LocalDateTime date = Objects.isNull(start) ? LocalDateTime.now() : start;
        return date.until(end, unit);
    }

}
