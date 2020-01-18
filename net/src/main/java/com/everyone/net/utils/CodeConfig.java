package com.everyone.net.utils;

import java.util.HashMap;

/**
 * 返回代码含义
 */
public class CodeConfig {
    //错误代码
    public static final HashMap<String,String> codeMap=new HashMap<String,String>(){
        {
            codeMap.put("0","操作成功");
            codeMap.put("10001","手机号码不正确");
            codeMap.put("10002","缺少参数");
            codeMap.put("10003","用户不存在");
            codeMap.put("10004","用户提交的手机号码已经被注册");
            codeMap.put("10005","用户注册失败");
            codeMap.put("10006","用户提交的手机号码尚未被注册");
            codeMap.put("10007","请先登录后再操作");
            codeMap.put("10008","身份证号码错误");
            codeMap.put("10009","身份认证失败");
            codeMap.put("20001","密码必须包含8位以上16位以下的字母与数字");
            codeMap.put("30001","文件上传失败");
            codeMap.put("90001","请求已过期");
            codeMap.put("90002","签名错误");
        }
    };
}
