package com.codepig.common.config;

/**
 * 基础设置
 */
public class BaseConfig {
    public static final String ROOTURL = "http://nwapi.mobilekoudai.com";
    public static final String VERSION_NAME = "1.0.0";
    public static final boolean DEBUG = false;//false:不打印log，true：打印log

    public static final int MAX_IMG = 10;
    public static final String APP_IMAGE_PATH = "trading_image";//缓存图片目录
    public static final String APP_TEMP_PATH = "trading_temp";//缓存文件目录
    public static final String IMAGEUPLOADIP = "http://s2.mobilekoudai.com";             //线上图片上传ip地址
}
