package com.codepig.customerview.activity;

import android.os.Bundle;

import com.codepig.common.activity.BaseTitleActivity;
import com.codepig.common.config.IntentConfig;
import com.codepig.common.viewmodel.BaseVM;
import com.codepig.customerview.R;
import com.codepig.customerview.callback.TempCB;
import com.codepig.customerview.databinding.ActivityTempBinding;
import com.codepig.customerview.viewmodel.TempVM;

/**
 * 临时页面
 */
public class TempActivity extends BaseTitleActivity implements TempCB {
    private final String TAG = TempActivity.class.getSimpleName();
    private ActivityTempBinding binding;
    private TempVM vm;

    private String type="临时页面";

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_temp;
    }

    @Override
    public BaseVM initViewModel() {
        vm = new TempVM(this);
        return vm;
    }

    @Override
    public String setTitle() {
        return "私聊";
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        binding = (ActivityTempBinding) baseViewBinding;
//        setISlenderLineGone();
//        hideNavigation();
//        hideTitle();

        try {
            Bundle bundle = getIntent().getExtras();
            type=bundle.getString(IntentConfig.TYPE, "消息");
            setTitle(type);
        }catch (Exception e){
        }

        //消息
        switch (type){
        }

        vm.getList(true);
    }

    @Override
    public void showData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
