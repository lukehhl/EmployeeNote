package com.example.administrator.employeenote.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by GE11522 on 2016/10/20.
 */

public class DateUtils {
    public static String unixToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(time);
        return sf.format(d);
    }

    public static String unixToStringhour(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        Date d = new Date(time);
        return sf.format(d);
    }

    public static String unixToStringday(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(time);
        return sf.format(d);
    }

    public static String unixToStringyear(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        Date d = new Date(time);
        return sf.format(d);
    }
    public static String unixToStringmonth(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        Date d = new Date(time);
        return sf.format(d);
    }
    public static String unixToStringdate(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        Date d = new Date(time);
        return sf.format(d);
    }

    public static String unixToWeek(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }

    public static long date2unix(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTimestamp = date.getTime();
        return unixTimestamp;
    }

    public static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayForWeek = 0;
        dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        return dayForWeek;
    }
}
