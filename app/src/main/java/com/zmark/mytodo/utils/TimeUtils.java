package com.zmark.mytodo.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class TimeUtils {

    public static Timestamp getTimestampFromStr(String timeStampStr) {
        return Timestamp.valueOf(timeStampStr);
    }

    public static Date getDateFromStr(String dateStr) {
        return Date.valueOf(dateStr);
    }

    public static Date today() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取dayNum天后的日期
     */
    public static Date afterDays(int dayNum) {
        return new Date(System.currentTimeMillis() + (long) dayNum * 24 * 60 * 60 * 1000);
    }

    /**
     * 如果是今天，返回今天
     * <p>
     * 如果是明天，返回明天
     * <p>
     * 否则返回日期
     */
    public static String getFormattedDateStr(Date date) {
        if (date.equals(today())) {
            return "今天";
        }
        if (date.equals(afterDays(1))) {
            return "明天";
        }
        return date.toString();
    }

    public static String getDayOfWeek(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "星期日";
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            default:
                return "未知";
        }
    }

    /**
     * 返回`日期 周几`
     */
    public static String getFormattedDateStrFromTimeStamp(String timeStamp) {
        return getFormattedDateStrFromTimeStamp(getTimestampFromStr(timeStamp));
    }

    /**
     * 返回`日期 周几`
     * <p>
     * 如果是今天，返回今天
     */
    public static String getFormattedDateStrFromTimeStamp(Timestamp timeStamp) {
        Date date = new Date(timeStamp.getTime());
        if (date.equals(today())) {
            return "今天";
        }
        String dayOfWeek = getDayOfWeek(timeStamp);
        return getFormattedDateStr(date) + " " + dayOfWeek;
    }
}
