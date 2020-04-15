package com.codepig.common.config;

import android.content.Context;

/**
 * 设备相关基础设置
 */
public class DeviceConfig {
    //原型尺寸
    public static final int appWidth=375;
    public static final int appHeight=667;
    //当前尺寸
    public static int curWidth=1080;
    public static int curHeight=1920;
    //缩放比例
    public static Double appScale=2.0;
    public static Double appScaleH=2.0;

    private static String device_num;
    private static String device_token;

    private static Context context;

    public static String getDevice_num() {
        return device_num;
    }

    public static void setDevice_num(String device_num) {
        DeviceConfig.device_num = device_num;
    }

    public static String getDevice_token() {
        return device_token;
    }

    public static void setDevice_token(String device_token) {
        DeviceConfig.device_token = device_token;
    }

    public static void setContext(Context context) {
        DeviceConfig.context = context;
    }

    public static Context getContext() {
        return context;
    }
}
