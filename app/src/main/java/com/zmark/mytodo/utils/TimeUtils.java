package com.zmark.mytodo.utils;

import java.sql.Date;

public class TimeUtils {

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
}
