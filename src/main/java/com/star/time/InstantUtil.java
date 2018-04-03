package com.star.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * jdk包时间工具类
 * <p>
 * java.time
 *
 * @author starhq
 */
public final class InstantUtil {


    private InstantUtil() {
    }

    /**
     * long类型转Instant
     *
     * @param millis 时间
     * @return {@link Instant}
     */
    public static Instant getInstantByMills(final long millis) {
        return Instant.ofEpochMilli(millis);
    }

    /**
     * 时间戳转Instant
     *
     * @param timestamp 时间戳
     * @return {@link Instant}
     */
    public static Instant getInstantByTimstamp(final long timestamp) {
        return Instant.ofEpochSecond(timestamp);
    }

    /**
     * date转Instant
     *
     * @param calendar 日期
     * @return {@link Instant}
     */
    public static Instant getInstant(final Calendar calendar) {
        return getInstant(calendar.getTime());
    }

    /**
     * date转Instant
     *
     * @param date 日期
     * @return {@link Instant}
     */
    public static Instant getInstant(final Date date) {
        return getInstantByMills(date.getTime());
    }

    /**
     * localDateTime转Instant
     *
     * @param localDateTime 日期
     * @return {@link Instant}
     */
    public static Instant getInstant(final LocalDateTime localDateTime) {
        return Objects.isNull(localDateTime) ? LocalDateTime.now().toInstant(ZoneOffset.UTC) : localDateTime.toInstant(ZoneOffset.UTC);
    }

    //=================================instant==============================================

    /**
     * instant转日期
     *
     * @param instant instant
     * @return {@link Date}
     */
    public static Date getDate(final Instant instant) {
        return Date.from(Objects.isNull(instant) ? Instant.now() : instant);
    }

    /**
     * instant转日期
     *
     * @param instant instant
     * @return {@link Calendar}
     */
    public static Calendar getCalendar(final Instant instant) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(instant));
        return calendar;
    }

    //=================================date & calendar==============================================

    /**
     * 获得当前的时间long
     *
     * @return 当前时间
     */
    public static long getNow() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获得当前的时间戳
     *
     * @return 当前时间
     */
    public static long getTimstamp() {
        return Instant.now().getEpochSecond();
    }


    /**
     * 获得时间差
     *
     * @param time 指定时间
     * @return 和当前时间差
     */
    public static long getDiff(final long time) {
        return getNow() - time;
    }
    //=================================long==============================================

}
