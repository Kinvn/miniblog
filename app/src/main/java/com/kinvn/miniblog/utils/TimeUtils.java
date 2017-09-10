package com.kinvn.miniblog.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kinvn on 2017/9/10.
 */

public class TimeUtils {
    public static String getTimeByWbApiTime(String createdAt, String source) throws ParseException {
        Date date = new SimpleDateFormat("EE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(createdAt);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar currentCalendar = Calendar.getInstance();
        long l = System.currentTimeMillis() - date.getTime();
        if (l < 0) {
            l = 1000;
        }
        if (l < 60 * 1000) {
            return (l / 1000) + "秒前";
        } else if (l < 3600 * 1000) {
            return (l / 1000 / 60) + "分钟前";
        } else if ((calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) &&
                (calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR))) {
            return new SimpleDateFormat("今天 HH:mm", Locale.US).format(date);
        }
        return new SimpleDateFormat("yyyy.MM.dd  HH:mm", Locale.US).format(date);
    }
}
