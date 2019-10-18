package com.codepig.common.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

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
     * @param _ms
     * @return
     */
    public static String ms2DateYMD(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        return format.format(date);
    }
}
