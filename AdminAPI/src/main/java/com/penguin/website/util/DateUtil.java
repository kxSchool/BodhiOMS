package com.penguin.website.util;

import com.penguin.website.bean.req.REQPenguinsOrder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 将Date转为时间戳
     * @param date
     * @return
     */
    public static Long getTimestampOfDate(Date date) {
        return date.getTime();
    }

    /**
     * 获取日期指定天数的00:00:00.
     *
     * @param date
     * @param days
     * @return
     */
    public static Date minusDayFirst(Date date, Integer days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        Date time = c.getTime();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 两个时间差分钟
     * @param start
     * @param end
     * @return
     */
    public static Long getSubDate(Date start, Date end) {
        return (end.getTime() - start.getTime())/1000/60;
    }


    //下面是把时间减8小时的方法，我是把方法写在了一个MyDate类里面，
    public static Date formatTimeEight(Date time) throws Exception {
        //把当前得到的时间用date.getTime()的方法写成时间戳的形式，再加上8小时对应的毫秒数
        long rightTime = (long) (time.getTime() - 8 * 60 * 60 * 1000);
        return new Date(rightTime);
    }

    public static void formatTimeEight(REQPenguinsOrder req) throws Exception {
        if(req.getStartOrderTime()!=null){
            req.setStartOrderTime(formatTimeEight(req.getStartOrderTime()));
        }
        if(req.getEndOrderTime()!=null){
            req.setEndOrderTime(formatTimeEight(req.getEndOrderTime()));
        }
        if(req.getStartConfirmTime()!=null){
            req.setStartConfirmTime(formatTimeEight(req.getStartConfirmTime()));
        }
        if(req.getEndConfirmTime()!=null){
            req.setEndConfirmTime(formatTimeEight(req.getEndConfirmTime()));
        }
    }

}
