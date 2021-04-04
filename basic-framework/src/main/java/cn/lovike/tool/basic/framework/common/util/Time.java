package cn.lovike.tool.basic.framework.common.util;

import org.testng.annotations.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Java 时间类用法
 *
 * @author lovike
 * @since 2021/2/16
 */
public class Time {
    /**
     * 日期 如 2020-01-01
     */
    @Test
    public void localDate() {
        LocalDate localDate        = LocalDate.now();                                           // 初始化当前日期
        int       year             = localDate.getYear();                                       // 年份：2020
        int       month            = localDate.getMonthValue();                                 // 月份：1
        int       dayOfMonth       = localDate.getDayOfMonth();                                 // 月份中第几天：4
        int       days             = localDate.lengthOfMonth();                                 // 月份对应天数：31
        int       dayOfWeek        = localDate.getDayOfWeek().getValue();                       // 一周的第几天：1
        LocalDate specifyLocalDate = LocalDate.of(2020, Month.JANUARY, 1);     // 生成指定日期对象
    }

    /**
     * 时分秒 如 22:47:07.075
     */
    @Test
    public void localTime() {
        LocalTime localTime        = LocalTime.now();                                            // 初始化当前时分秒
        int       hour             = localTime.getHour();                                        // 获取当前小时数
        int       minute           = localTime.getMinute();                                      // 获取当前分钟
        int       second           = localTime.getSecond();                                      // 获取当前秒数
        int       nano             = localTime.getNano();                                        // 获取当前纳秒
        LocalTime specifyLocalTime = LocalTime.of(18, 18, 18);              // 生成指定日期对象
    }

    /**
     * 时间 如 2020-01-01T22:47:07.075
     */
    @Test
    public void localDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();                                      // 初始化当前时间
        int           year          = localDateTime.getYear();                                  // 年份：2020
        int           month         = localDateTime.getMonthValue();                            // 月份：1
        int           dayOfMonth    = localDateTime.getDayOfMonth();                            // 月份中第几天：4
        int           dayOfWeek     = localDateTime.getDayOfWeek().getValue();                  // 一周的第几天：1
        // 生成指定日期+时间对象
        LocalDateTime specifyLocalDateTime = LocalDateTime.of(2020, Month.JANUARY, 1, 18, 18, 18);
    }

    /**
     * 转换
     */
    @Test
    public void convert() {
        LocalDateTime localDateTime  = LocalDateTime.now();                                     // 初始化当前时分秒
        LocalDate     localDate      = localDateTime.toLocalDate();                             // LocalDateTime 转 LocalDate
        LocalTime     localTime      = localDateTime.toLocalTime();                             // LocalDateTime 转 LocalTime
        LocalDateTime localDateTime2 = localDate.atTime(localTime);                             // LocalDate     转 LocalDateTime
    }

    @Test
    public void timeStampConvert() {
        // LocalDate 转时间戳
        LocalDate localDate  = LocalDate.now();
        long      timeStamp  = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long      timeStamp1 = localDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
        long      timeStamp2 = localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        long      timeStamp3 = localDate.atStartOfDay(ZoneOffset.of("+08:00")).toInstant().toEpochMilli();

        // LocalDateTime转时间戳
        LocalDateTime localDateTime = LocalDateTime.now();
        long          timeStamp4    = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long          timeStamp5    = localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
        long          timeStamp6    = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        // 时间戳转LocalDateTime(LocalDate)

        long          nowTimeStamp     = System.currentTimeMillis();
        LocalDate     toLocalDate      = Instant.ofEpochMilli(nowTimeStamp).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate     toLocalDate1     = Instant.ofEpochMilli(nowTimeStamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        LocalDateTime toLocalDateTime  = Instant.ofEpochMilli(nowTimeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toLocalDateTime1 = Instant.ofEpochMilli(nowTimeStamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        // 注：Instant 可以精确到纳秒（Nano-Second），System.currentTimeMillis() 方法只精确到毫秒（Milli-Second）
    }

    @Test
    public void dateConvert() {
        // LocalDate 转 Date
        LocalDate localDate = LocalDate.now();
        Date      date1     = Date.from(localDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant());

        // LocalDateTime 转 Date
        LocalDateTime localDateTime = LocalDateTime.now();
        Date          date2         = Date.from(localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant());

        // Date 转 LocalDateTime(LocalDate)
        Date          date           = new Date();
        LocalDate     localDate1     = date.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        LocalDateTime localDateTime1 = date.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }

    /**
     * 转换
     */
    @Test
    public void format() {
        // 将时间类格式化为字符串（与格式化器有关）
        LocalDateTime dateTime = LocalDateTime.now();
        String        strDate1 = dateTime.format(DateTimeFormatter.BASIC_ISO_DATE);             // 20200101
        String        strDate2 = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);             // 2020-01-01
        String        strDate3 = dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);             // 14:20:16.998
        String        strDate4 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));    // 2020-01-01
        // 今天是：2020年 一月 01日 星期四
        String strDate5 = dateTime.format(DateTimeFormatter.ofPattern("今天是：YYYY 年 MMMM DD 日 E", Locale.CHINESE));

        // 将日期格式化为对象
        String strDate6 = "2017-01-05";
        String strDate7 = "2017-01-05 12:30:05";

        LocalDate     date      = LocalDate.parse(strDate6, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime dateTime1 = LocalDateTime.parse(strDate7, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 日期差
     */
    @Test
    public void diffDay() {
        Duration duration = Duration.between(LocalDateTime.now(),
                                             LocalDateTime.now().plusYears(1));
        long days    = duration.toDays();                                           // 获取相差天数
        long hours   = duration.toHours();                                          // 获取相差小时数
        long minutes = duration.toMinutes();                                        // 获取相差分钟数
    }
    
    // // 设置今日零点
    // LocalDateTime todayZero = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    // // 设置明日零点
    // LocalDateTime tomorrowZero = todayZero.plusDays(1);
    // now = now.with(WeekFields.of(Locale.CHINA).dayOfWeek(), 1);
    // LocalDateTime mondayZero = LocalDateTime.of(now, LocalTime.MIN);
    // // 设置下周一零点
    // LocalDateTime nextMondayZero = mondayZero.plusDays(7);
    //
    // startDate = mondayZero.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    // endDate = nextMondayZero.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    //         break;
    //     case MONTH:
    // // 设置本月初零点
    // LocalDate firstDay = LocalDate.of(now.getYear(), now.getMonth(), 1);
    // LocalDateTime monthZero = LocalDateTime.of(firstDay, LocalTime.MIN);
    // // 设置下月初零点
    // LocalDateTime lastDay = monthZero.plusMonths(1);
    //
    // startDate = monthZero.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    // endDate = lastDay.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    //         break;
    //     case YEAR:
    // now = LocalDate.of(now.getYear(), 1, 1);
    // // 设置本年初零点
    // LocalDateTime yearZero = LocalDateTime.of(now, LocalTime.MIN);
    // // 设置明年初零点
    // LocalDateTime nextYearZero = yearZero.plusYears(1);
    // startDate = yearZero.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    // endDate = nextYearZero.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
}
