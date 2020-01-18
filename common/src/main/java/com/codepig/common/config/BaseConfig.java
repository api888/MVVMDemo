package com.codepig.common.config;

/**
 * 基础设置
 */
public class BaseConfig {
    public static String ROOTURL = "http://api.myhost.com";//服务器根地址,host
    public static final String VERSION_NAME = "1.0.1";
    public static final boolean DEBUG = false;//false:不打印log，true：打印log

    public static final int MAX_IMG = 10;
    public static final String APP_IMAGE_PATH = "everyone_image";//缓存图片目录
    public static final String APP_TEMP_PATH = "com.everyone";//缓存文件目录
    public static final String IMAGEUPLOADIP = "http://api.myhost.com";             //线上图片上传ip地址
}
