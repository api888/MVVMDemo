package com.everyone.net.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.codepig.common.config.DeviceConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * http相关工具
 */
public class HttpUtil {
    private static String device_num="",mobile_os_version="";

    /**
     * 拼接校验字符串
     * @param map
     * @return
     */
    public static Map getSign(Map<String, String> map){
        try {
            Map<String, String> signMap = new TreeMap<>();
            Map<String, String> pMap = new TreeMap<>();
            for (String o : map.keySet()) {
                signMap.put(o, URLEncoder.encode(map.get(o), "UTF-8"));
                pMap.put(o, map.get(o));
//                System.out.println("utf:"+URLEncoder.encode(map.get(o), "UTF-8"));
            }
            String nonce = getRandomString(11);
            //添加公共参数
            signMap.put("timestamp", getCurrentTime());
            pMap.put("timestamp", getCurrentTime());
            signMap.put("mobile_os_version",mobile_os_version);
            pMap.put("mobile_os_version",mobile_os_version);
            signMap.put("mobile_os","android");
            pMap.put("mobile_os","android");
            if(!TextUtils.isEmpty(DeviceConfig.getDevice_num())) {
                signMap.put("device_num", DeviceConfig.getDevice_num());
                pMap.put("device_num", DeviceConfig.getDevice_num());
            }

            //验签字符串
            String sign = "";
            for (String o : signMap.keySet()) {
                sign += "&" + o + "=" + signMap.get(o);
            }
            sign = "POST" + sign.substring(1);
//            System.out.println("param:" + sign);

            sign = HmacSha1(sign, nonce);
//            System.out.println("param:nonce=" + nonce);
//            System.out.println("param:sign=" + sign);
            pMap.put("nonce", nonce);
            pMap.put("sign", sign);

            return pMap;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * HMAC_SHA1加密，输出16进制字符串
     * @param value
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String HmacSha1(String value, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    /**
     * 转16进制字符串
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * HMAC_SHA1加密，输出字符串
     * @param datas
     * @param key
     * @return
     */
    private static String HmacSha12UTF8(String datas, String key){
        System.out.println("param:key=" + key);
        System.out.println("param:datas=" + datas);
        String reString = "";
        try
        {
            byte[] data = key.getBytes("UTF-8");
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);
            byte[] text = datas.getBytes("UTF-8");
            //完成 Mac 操作
            byte[] text1 = mac.doFinal(text);
            reString = Base64.encodeToString(text1, Base64.DEFAULT);

        } catch (Exception e)
        {
            // TODO: handle exception
        }

        return reString;
    }

    /**
     * 获取当前时间戳
     */
    public static String getCurrentTime(){
        Date date = new Date();
        String _time=date.getTime()+"";
        return _time;
    }

    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        //（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0; i<length; ++i){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获得系统版本号
     */
    public static void getOSVersion(Context context){
        mobile_os_version=Build.VERSION.RELEASE;
        System.out.println("Device info:"+mobile_os_version);
    }

    /**
     * String 转 RequestBody
     * @param value
     * @return
     */
    public static RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }
}
