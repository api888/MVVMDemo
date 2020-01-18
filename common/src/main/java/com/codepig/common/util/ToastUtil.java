package com.codepig.common.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codepig.common.R;
import com.codepig.common.app.MyApp;

public class ToastUtil {
    private static Toast toast;
    private static final boolean showTest=false;
    public static void showToast(String text) {
        //常规Toast
//        if (toast == null) {
//            toast = Toast.makeText(MyApp.getInstance(), text, Toast.LENGTH_SHORT);
//        } else {
//            toast.setText(text);
//        }
//        toast.show();

        //自定义Toast
        if(!text.equals("签名错误") && !text.equals("出现系统错误，请稍后再试！")) {//签名错误暂时不显示
            showMyToast(text);
        }else {
            System.out.println("hided error:"+text);
        }
    }

    public static void showToast(String text,boolean show_signError) {
        //自定义Toast
        if(!text.equals("签名错误")) {//签名错误暂时不显示
            showMyToast(text);
        }else {
            if(show_signError){
                showMyToast("请求失败");
            }else {
                System.out.println("hided error:" + text);
            }
        }
    }

    public static void showMyToast(String text) {
        LayoutInflater inflater = LayoutInflater.from(MyApp.getInstance());
        View toastView = inflater.inflate(R.layout.toast_text_only, null);   // 用布局解析器来解析一个布局去适配Toast的setView方法
        TextView mText = toastView.findViewById(R.id.toast_tv);
        mText.setText(text);
        toast = new Toast(MyApp.getInstance());
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showTestToast(String text) {
        if(!showTest){
            return;
        }
        //自定义Toast
        showMyToast(text);
    }
}
