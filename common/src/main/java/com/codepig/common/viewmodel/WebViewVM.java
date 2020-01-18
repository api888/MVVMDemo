package com.codepig.common.viewmodel;

import android.os.Bundle;

import com.codepig.common.callback.WebViewCB;
import com.codepig.common.config.IntentConfig;


public class WebViewVM extends BaseVM{
    private WebViewCB cb;
    private String link;
    /**
     * 不需要callback可以传null
     *
     * @param callBack
     */
    public WebViewVM(WebViewCB callBack) {
        super(callBack);
        cb = callBack;
    }

    public void getIntentData() {
        Bundle bundle = cb.getIntentData();
        if (bundle == null) return;
        link = bundle.getString(IntentConfig.LINK);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
