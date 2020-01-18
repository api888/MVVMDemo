package com.codepig.customerview.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.codepig.common.activity.BaseTitleActivity;
import com.codepig.common.config.ActivityNameConfig;
import com.codepig.common.config.BaseConfig;
import com.codepig.common.config.IntentConfig;
import com.codepig.common.viewmodel.BaseVM;
import com.codepig.customerview.R;
import com.codepig.customerview.callback.TestCB;
import com.codepig.customerview.databinding.ActivityTestBinding;
import com.codepig.customerview.viewmodel.TestVM;
import com.everyone.net.data.UserConfig;
import com.everyone.net.utils.HttpUtil;

/**
 * 临时页面
 */
public class TestActivity extends BaseTitleActivity implements TestCB {
    private final String TAG = TestActivity.class.getSimpleName();
    private ActivityTestBinding binding;
    private TestVM vm;

    private String type="临时页面";

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    public BaseVM initViewModel() {
        vm = new TestVM(this);
        return vm;
    }

    @Override
    public String setTitle() {
        return "私聊";
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        binding = (ActivityTestBinding) baseViewBinding;
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

        Bundle bundle = getIntentData();

        binding.orderTestBtn.setOnClickListener(v -> {
            vm.startTest(Integer.parseInt(binding.requestCount.getText().toString()),true);
            BaseConfig.ROOTURL=binding.hostText.getText().toString();
        });
        binding.allInOneTestBtn.setOnClickListener(v -> {
            vm.startTest(Integer.parseInt(binding.requestCount.getText().toString()),false);
            BaseConfig.ROOTURL=binding.hostText.getText().toString();
        });
        binding.enterBtn.setOnClickListener(v -> {
            binding.enterBtn.setEnabled(false);
            if (TextUtils.isEmpty(UserConfig.getUserId())) {
                doIntentClassName(ActivityNameConfig.LOGIN_ACTIVITY, null);
            } else {
                doIntentClassName(ActivityNameConfig.MAIN_ACTIVITY, bundle);
            }
            finishActivity();
        });
        binding.signBtn.setOnClickListener(v -> {
//            String key=HttpUtil.getRandomString(11);
            String key=binding.nonceText.getText().toString();
            binding.nonceText.setText(key);
            try {
                String sign = HttpUtil.HmacSha1(binding.str.getText().toString(), key);
                binding.signText.setText(sign);
            }catch (Exception e){
            }
        });
    }

    @Override
    public void showData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showText(String msg){
        binding.infoText.setText(msg);
    }
}
