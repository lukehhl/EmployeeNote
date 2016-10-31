package com.example.administrator.employeenote.utils;

import java.text.SimpleDateFormat;
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

    public static String unixToWeek(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }
}
