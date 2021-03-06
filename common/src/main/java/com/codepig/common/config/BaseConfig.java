package com.codepig.common.config;

/**
 * 基础设置
 */
public class BaseConfig {
//    public static String ROOTURL = "http://139.199.23.162:8888";//服务器根地址,host
    public static final String MY_HOST="dajiaunion.com";
    public static String ROOTURL = "http://api."+MY_HOST;//接口根地址
    public static String VERSION_NAME = "1.0.3";
    public static final boolean DEBUG = false;//false:不打印log，true：打印log

    public static final int MAX_IMG = 10;
    public static final String APP_IMAGE_PATH = "everyone_image";//缓存图片目录
    public static final String APP_TEMP_PATH = "com.everyone";//缓存文件目录
    public static final String IMAGEUPLOADIP = "http://api."+MY_HOST;             //线上图片上传ip地址
//    public static final String IMAGEUPLOADIP = "http://139.199.23.162:8888";             //线上图片上传ip地址
}
