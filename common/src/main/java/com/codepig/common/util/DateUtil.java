package com.codepig.common.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * 将毫秒转换为小时：分钟：秒格式
     * @param _ms
     * @return
     */
    public static String ms2HMS(int _ms){
        String HMStime;
        _ms/=1000;
        int hour=_ms/3600;
        int mint=(_ms%3600)/60;
        int sed=_ms%60;
        String hourStr=String.valueOf(hour);
        if(hour<10){
            hourStr="0"+hourStr;
        }
        String mintStr=String.valueOf(mint);
        if(mint<10){
            mintStr="0"+mintStr;
        }
        String sedStr=String.valueOf(sed);
        if(sed<10){
            sedStr="0"+sedStr;
        }
        if(_ms>3600000) {
            HMStime = hourStr + ":" + mintStr + ":" + sedStr;
        }else{
            HMStime = mintStr + ":" + sedStr;
        }
        return HMStime;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyyMMdd
     */
    public static Date strToDate(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    // 获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        // 可 以方便 地修改日期格式
        return dateFormat.format(now);
    }

    /**
     * 获取当前年月日
     */
    public static ArrayList<String> getNowDate(){
        ArrayList dateArr=new ArrayList();
        Calendar c = Calendar.getInstance();//
        dateArr.add(c.get(Calendar.YEAR)+"");
        dateArr.add(c.get(Calendar.MONTH)+1+"");
        dateArr.add(c.get(Calendar.DAY_OF_MONTH)+"");
        return dateArr;
    }

    /**
     * 按格式输出当前年月日
     * @return
     */
    public static String ms2DateYMD(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        return format.format(date);
    }

    //将毫秒转换为标准日期格式
    public static String ms2Date(long _ms){
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public static String ms2Date2(long _ms){
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        return format.format(date);
    }

    public static String ms2DateOnlyDay(long _ms){
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
        return format.format(date);
    }

    public static String ms2DateOnlyDay2(long _ms){
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        return format.format(date);
    }

    public static String ms2DateOnlyHM(long _ms){
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(date);
    }

    /**
     * 计算时间差
     * @param startDate
     * @param endDate
     * @return
     */
    public static String DateDistance(Date startDate,Date endDate){
        if(startDate == null ||endDate == null){
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if(timeLong<0){
            timeLong=0;
        }
        if (timeLong<60*1000)
            return timeLong/1000 + "秒前";
//            return "";
        else if (timeLong<60*60*1000){
            timeLong = timeLong/1000 /60;
            return timeLong + "分钟前";
//            return "";
        }
        else if (timeLong<60*60*24*1000){
            timeLong = timeLong/60/60/1000;
            return timeLong+"小时前";
//            return "";
        }
        else{
            timeLong = timeLong/1000/ 60 / 60 / 24;
            if(timeLong==1){
                return "昨天";
            }else {
                return timeLong + "天前";
            }
        }
//        else if ((timeLong/1000/60/60/24)<30){
//            timeLong = timeLong/1000/ 60 / 60 / 24/7;
//            return timeLong + "周前";
//        }
//        else if ((timeLong/1000/60/60/24/30)<12){
//            timeLong = timeLong/1000/ 60 / 60 / 24/30;
//            return timeLong + "月前";
//        }
//        else {
//            return timeLong/1000/60/60/24/30/12+"年前";
//        }
    }

    /**
     * 计算与当前的时间差
     * @param _ms
     * @return
     */
    public static String DateDistance2now(long _ms){
        String h_m=ms2DateOnlyHM(_ms);
        SimpleDateFormat DateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Long time=new Long(_ms);
            String d = DateF.format(time);
            Date startDate=DateF.parse(d);
            Date nowDate = Calendar.getInstance().getTime();
            return DateDistance(startDate, nowDate);
//            return DateDistance(startDate, nowDate,false)+" "+h_m;
        }catch (Exception e){
        }
        return null;
    }

    /**
     * 计算与当前的时间差,只保留时分秒
     * @param _ms
     * @return
     */
    public static String DateDistance2nowOnlyTime(long _ms){
        String h_m=ms2DateOnlyHM(_ms);
        SimpleDateFormat DateF = new SimpleDateFormat("HH:mm:ss");
        try {
            Long time=new Long(_ms);
            String d = DateF.format(time);
            Date startDate=DateF.parse(d);
            Date nowDate = Calendar.getInstance().getTime();
            return DateDistance(startDate, nowDate)+" "+h_m;
        }catch (Exception e){
        }
        return null;
    }
}
