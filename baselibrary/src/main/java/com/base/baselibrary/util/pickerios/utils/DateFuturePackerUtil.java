package com.base.baselibrary.util.pickerios.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by baiyuliang on 2015-11-23.
 */
public class DateFuturePackerUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");


    /**
     * 获取当前时间向后一周内的日期列表
     *
     * @return
     */
    public static List<String> getDateList() {
        List<String> months = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        long current_time = System.currentTimeMillis();
        long day_ms = 24 * 60 * 60 * 1000;
        for (int i = 0; i < 7; i++) {
            c.setTimeInMillis(current_time + day_ms * i);
            if (i == 0) {
                months.add(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日(今天)");
            } else if (i == 1) {
                months.add(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日(明天)");
            } else {
                months.add(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日(" + getWeek(c.get(Calendar.DAY_OF_WEEK) - 1) + ")");
            }
        }
        return months;
    }

    /**
     * 根据传入的时间获取该天可预约的时间列表
     *
     * @param str
     * @return
     */
    public static List<String> getTimeList(String str) {
        Date date = null;
        if (TextUtils.isEmpty(str)) {
            date = new Date();
        } else {
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
        }
        Calendar c = Calendar.getInstance();//当前时间
        Calendar _c = Calendar.getInstance();//传进来的时间
        _c.setTime(date);
        //如果当前月<传入月，或者当前月与传入月相同但当前日<传入日，并且
        if (c.get(Calendar.MONTH) < _c.get(Calendar.MONTH) || (c.get(Calendar.MONTH) == _c.get(Calendar.MONTH) && c.get(Calendar.DAY_OF_MONTH) < _c.get(Calendar.DAY_OF_MONTH))) {
            return getTimeAllList();
        } else {
            if (c.get(Calendar.HOUR_OF_DAY) > 12) {
                return getTimePMList();
            } else {
                return getTimeAllList();
            }
        }
    }

    /**
     * 获取该天可预约的时间列表（全天）
     *
     * @return
     */
    public static List<String> getTimeAllList() {
        List<String> timeList = new ArrayList<String>();
        int hour = 5;
        for (int i = 0; i < 25; i++) {
            String sec;
            if (i % 2 == 0) {
                sec = "00";
                hour++;
            } else {
                sec = "30";
            }
            timeList.add(hour + ":" + sec);
        }
        return timeList;
    }

    /**
     * 获取该天可预约的时间列表（下午）
     *
     * @return
     */
    public static List<String> getTimePMList() {
        List<String> timeList = new ArrayList<String>();
        int hour = 12;
        for (int i = 0; i < 11; i++) {
            String sec;
            if (i % 2 == 0) {
                sec = "00";
                hour++;
            } else {
                sec = "30";
            }
            timeList.add(hour + ":" + sec);
        }
        return timeList;
    }


    /**
     * 获取星期几
     */
    public static String getWeek(int week) {
        String[] _weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        switch (week) {
            case 0:
                return _weeks[0];
            case 1:
                return _weeks[1];
            case 2:
                return _weeks[2];
            case 3:
                return _weeks[3];
            case 4:
                return _weeks[4];
            case 5:
                return _weeks[5];
            case 6:
                return _weeks[6];
        }
        return "";
    }

    public static int getPosition(List<String> list, String str) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i))) {
                position = i;
            }
        }
        return position;
    }

    public static List<String> getBirthYearList() {
        List<String> birthYearList = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 0; i < 2; i++) {
            birthYearList.add((year + i) + "年");
        }
        return birthYearList;
    }

    public static List<String> getBirthMonthList(String... start) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        List<String> birthMonthList = new ArrayList<String>();
        if(start!=null&&start.length>0&&(year+"").equals( start[0])){
            int i= c.get(Calendar.MONTH)+1;
            for (; i <= 12; i++) {
                if (i<10) {
                    birthMonthList.add("0"+i + "月");
                }else {
                    birthMonthList.add(i + "月");
                }
            }

        }else{
            for (int i=1; i <= 12; i++) {
                if (i<10) {
                    birthMonthList.add("0"+i + "月");
                }else {
                    birthMonthList.add(i + "月");
                }
            }
        }


        return birthMonthList;
    }

    public static List<String> getBirthDay28List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 28; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }
        }
        return birthDayList;
    }

    public static List<String> getBirthDay29List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 29; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }

        }
        return birthDayList;
    }


    public static List<String> getBirthDay30List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 30; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }
        }
        return birthDayList;
    }

    public static List<String> getBirthDay31List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }
        }
        return birthDayList;
    }



    public static List<String> getBirthDay28AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 28; i++) {
            if (i<10) {
                birthDayList.add("0"+i+getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i+getWeekOfDate(date+"-"+i));
            }
        }
        return birthDayList;
    }

    public static List<String> getBirthDay29AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 29; i++) {
            if (i<10) {
                birthDayList.add("0"+i+getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i +getWeekOfDate(date+"-"+i));
            }

        }
        return birthDayList;
    }






    public static List<String> getBirthDay30AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 30; i++) {
            if (i<10) {
                birthDayList.add("0"+i +getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i+getWeekOfDate(date+"-"+i));
            }
        }
        return birthDayList;
    }


    public static List<String> getBirthDay31AndWeekList(String date, int i) {
        Log.e("DateList", date) ;
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 31; i++) {
            if (i<10) {
                birthDayList.add("0"+i +getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i +getWeekOfDate(date+"-"+i));
            }
        }
        return birthDayList;
    }

    /**
     * 判断是否是闰年
     *
     * @return
     */
    public static boolean isRunYear(String year) {
        try {
            int _year = Integer.parseInt(year);
            if (_year % 4 == 0 && _year % 100 != 0 || _year % 400 == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 根据日期获得星期
     * @param
     * @return
     */
    public static String getWeekOfDate(String time) {
        Log.e("Date", time) ;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
        Date date = null;
        try {
            date = format.parse(time);// 将字符串转换为日期
        } catch (ParseException e) {
            System.out.println("输入的日期格式不合理！");
        }

        String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }



    /**
     * 获取日期时间24小时制
     *
     * @return
     */
    public static List<String> get24HourTimeList(int i) {
        List<String> timeList = new ArrayList<String>();
        for (; i < 25; i++) {
            timeList.add(i + "时");
        }
        return timeList;
    }

}
