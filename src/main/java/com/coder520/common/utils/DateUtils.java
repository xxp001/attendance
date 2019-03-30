package com.coder520.common.utils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xxp[www.aiprogram.top] 2018/9/21.
 */
public class DateUtils {


    private static Calendar calendar = Calendar.getInstance();
    /**
     *@Author xxp [www.coder520.com]
     *@Date 2018/9/21 21:45
     *@Description 得到今天是周几
     */

    public static int getTodayWeek(Date date){

       calendar.setTime(date);
       int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
       if(week<0) week=7;
       return week;
   }

    /**
     *@Author xxp [www.coder520.com]
     *@Date 2018/9/21 21:46
     *@Description 计算时间差 分钟数
     */

    public static  int getMunite(Date startDate,Date endDate){

          long start = startDate.getTime();
          long end = endDate.getTime();
          int munite =  (int)(end-start)/(1000*60);
          return munite;
      }


    /**
     *@Author xxp [www.coder520.com]
     *@Date 2018/9/21 22:28
     *@Description 获取当天某个时间
     */

    public static Date getDate(int hour,int minute,int second){

          calendar.set(Calendar.HOUR_OF_DAY,hour);
          calendar.set(Calendar.MINUTE,minute);
          calendar.set(Calendar.SECOND,second);
          return  calendar.getTime();
      }
    public static Date getDate1(int hour,int minute){

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        return  calendar.getTime();
    }

}
