package com.example.administrator.employeenote.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by GE11522 on 2016/10/20.
 */

public class DataUtils {
    public static String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(time);
        return sf.format(d);
    }
}
