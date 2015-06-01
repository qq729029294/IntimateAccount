
package com.nan.ia.app.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeUtils {
    public static String dateToWeek(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date currentDate = new Date();
        int b = currentDate.getDay();
        Date fdate;
        List<String> list = new ArrayList<String>();
        Long fTime = currentDate.getTime();
        for (int a = 0; a < 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(sdf.format(fdate));
        }
        return list.get(position);
    }

    /**
     * @return
     */
    public static String getCurrentTime() {
        return getFormatDateTime(new Date(), "yyyy年MM月");
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    public static String getLocalTime(Context context, String time) {
        // 取出年月日来，比较字符串即可
        String str_curTime = DateFormat.format("yyyy-MM-dd", new Date()).toString();
        int result = str_curTime.compareTo(time.substring(0, time.indexOf(" ")));
        if (result > 0) {
            return "昨天"
                    + time.substring(time.indexOf(" "), time.lastIndexOf(":"));
        } else if (result == 0) {
            return "今天"
                    + time.substring(time.indexOf(" "), time.lastIndexOf(":"));
        } else {
            return time;
        }
    }
    
    public static String getMMddhhmmTime(Date date) {
    	return DateFormat.format("MM-dd hh:mm", date).toString();
    }
    
    /**
     * 返回星期
     * @param date
     * @return 周x
     */
    public static String dateFormatWeekCN(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    	switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "周日";
		case Calendar.MONDAY:
			return "周一";
		case Calendar.TUESDAY:
			return "周二";
		case Calendar.WEDNESDAY:
			return "周三";
		case Calendar.THURSDAY:
			return "周四";
		case Calendar.FRIDAY:
			return "周五";
		case Calendar.SATURDAY:
			return "周六";

		default:
			break;
		}
    	
    	return "";
    }
    
    /**
     * 返回每月几号
     * @param date
     * @return 每月几号
     */
    public static String dateFormatdd(Date date) {
    	return DateFormat.format("dd", date).toString();
    }
    
    /**
     * 返回月份
     * @param date
     * @return 每月几号
     */
    public static String dateFormatMM(Date date) {
    	return DateFormat.format("MM", date).toString();
    }
    
    /**
     * 获得2015.05.01-05.31的月份范围格式
     * @param date
     * @return
     */
	public static String dateFormatMonthRangeyyMMdd_MMdd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return String.format("%s.01-%02d.%02d", DateFormat.format("yy.MM", date)
				.toString(), calendar.get(Calendar.MONTH) + 1, calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
}
