package com.anima.multiplefiltersearchbar.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jianjianhong on 19-4-23
 */
public class DateUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static int compareDate(String dateStr1, String dateStr2) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date1 = df.parse(dateStr1);
            Date date2 = df.parse(dateStr2);
            if(date1.getTime() > date2.getTime()) {
                return 1;
            }else if(date1.getTime() == date2.getTime()) {
                return 0;
            }else if(date1.getTime() < date2.getTime()) {
                return -1;
            }
            return 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getTodayString() {
        return formatDate(new Date());
    }

    public static String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static String formatDate(String dateStr) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date date = df.parse(dateStr);
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(int year, int month, int day) {
        String dateStr = String.format("%d-%d-%d", year, month+1, day);
        return formatDate(dateStr);
    }

    public static String formatDate(Calendar calendar) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(calendar.getTime());
    }
}
