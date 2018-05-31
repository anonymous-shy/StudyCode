package xyz.shy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Shy on 2018/1/29
 * jdk 8 Date Util
 */

public class DateUtils {
    /**
     * yyyyMMddHHmmss
     */
    private static final DateTimeFormatter formatter_DateTimestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter formatter_DateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * yyyy-MM-ddTHH:mm:ss
     */
    private static final DateTimeFormatter formatter_DateTimeT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter formatter_DateTimeTZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * @return 获取当前日期
     */
    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }

    /**
     * @return 获取当前时间
     */
    public static LocalTime getLocalTime() {
        return LocalTime.now();
    }

    /**
     * @return 获取当前日期时间
     */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * @return 获取当前的微秒数
     */
    public static long getClockMillis() {
        Clock clock = Clock.systemDefaultZone();
        return clock.millis();
    }

    /**
     * @return 返回当前时间yyyyMMddHHmmss
     */
    public static String getDateTimestamp() {
        return getLocalDateTime().format(formatter_DateTimestamp);
    }

    /**
     * @return 返回当前时间yyyy-MM-dd
     */
    public static String getDate() {
        return getLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * @return 返回当前系统时间 返回当前系统时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime() {
        return getLocalDateTime().format(formatter_DateTime);
    }

    /**
     * @return 返回当前系统时间 返回当前系统时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeT() {
        return getLocalDateTime().format(formatter_DateTimeT);
    }

    /**
     * @return 返回当前系统时间 返回当前系统时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeTZ() {
        return getLocalDateTime().format(formatter_DateTimeTZ);
    }

    /**
     * @return 获取当月第一天 yyyy-MM-dd
     */
    public static String getFirstDayOfMonth() {
        return getLocalDate().withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * @return 获取本月最后一天 yyyy-MM-dd
     */
    public static String getLastDayOfMonth() {
        LocalDate localDate = getLocalDate();
        return localDate.withDayOfMonth(localDate.lengthOfMonth()).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 将yyyyMMddHHmmss转为 yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimestamp yyyyMMddHHmmss
     * @return String yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTimestamp(String dateTimestamp) {
        return LocalDateTime.parse(dateTimestamp, formatter_DateTimestamp).format(formatter_DateTime);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss转为 yyyyMMddHHmmss
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return String yyyyMMddHHmmss
     */
    public static String formatDateTime(String dateTime) {
        return parseLocalDateTime(dateTime).format(formatter_DateTimestamp);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss转为 LocalDateTime
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, formatter_DateTime);
    }

    /**
     * 将yyyyMMddHHmmss转为 LocalDateTime
     *
     * @param dateTimestamp yyyyMMddHHmmss
     * @return LocalDateTime LocalDateTime
     */
    public static LocalDateTime parseLocalDateTimestamp(String dateTimestamp) {
        return LocalDateTime.parse(dateTimestamp, formatter_DateTimestamp);
    }

    /**
     * 将yyyyMMddHHmmss转为 LocalDateTime
     *
     * @param dateTimestamp yyyyMMddHHmmss
     * @return LocalDateTime LocalDateTime
     */
    public static LocalDateTime parseLocalDateTimestampTZ(String dateTimestamp) {
        return LocalDateTime.parse(dateTimestamp, formatter_DateTimeTZ);
    }

    /**
     * yyyy-MM-dd字符串转LocalDate
     *
     * @param dateString yyyy-MM-dd
     * @return LocalDate
     */
    public static LocalDate parseLocalDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * yyyy-MM-dd 增加日期
     *
     * @param date
     * @param days
     * @return
     */
    public static String plusDays(String date, int days) {
        LocalDate localDate = parseLocalDate(date);
        return localDate.plusDays(days).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate 较小的时间 yyyy-MM-dd
     * @param endDate   较大的时间 yyyy-MM-dd
     * @return 相差天数
     */
    public static int dateCompareToDays(String startDate, String endDate) {
        LocalDate startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        Period period = Period.between(startLocalDate, endLocalDate);
        return period.getDays();
    }

    //TODO
    public static int dateCompareToSeconds(String time1, String time2) {
        return 0;
    }

    /**
     * 将String字符串转换为java.sql.Timestamp格式日期,用于数据库保存
     *
     * @param strDate    表示日期的字符串
     * @param dateFormat 传入字符串的日期表示格式（如："yyyy-MM-dd HH:mm:ss"）
     * @return java.sql.Timestamp类型日期对象（如果转换失败则返回null）
     */
    public static java.sql.Timestamp strToSqlDate(String strDate, String dateFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        java.util.Date date = null;
        try {
            date = sf.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Timestamp dateSQL = new java.sql.Timestamp(date.getTime());
        return dateSQL;
    }

    public static Date strTojuDate(String strDate, String dateFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //TEST//
    public static void main(String[] args) {
//        System.out.println(getLocalDateTime());
//        System.out.println(getClockMillis());
//        System.out.println(getDateTimestamp());
//        System.out.println(getDateTime());
//        System.out.println(getDateTimeT());
//        System.out.println(getFirstDayOfMonth());
//        System.out.println(getLastDayOfMonth());
//        System.out.println(parseLocalDateTimestampTZ("2016-02-03T02:40:06.930Z"));
//        System.out.println(strToSqlDate("2016-02-03T02:40:06.930Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        System.out.println(getLocalDate().toString().replace("-", "").substring(0, 6));
    }
}
