package com.codepig.common.app;

import android.app.Application;

import com.codepig.common.util.PreferenceUtil;

public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化PreferenceUtil
        PreferenceUtil.initialize(getApplicationContext());
//        WeChatPayUtil.getInstance().registerApp(this);
        //友盟分享
//        UMConfigure.init(this,"5c205fc4f1f55685f00001f3","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
//        PlatformConfig.setWeixin(BaseConfig.WXAPI_APPID, BaseConfig.WXAPI_APPSECRET);
    }
}
