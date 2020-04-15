package com.codepig.common.example.activity;

import android.os.Bundle;

import com.codepig.common.R;
import com.codepig.common.activity.BaseTitleActivity;
import com.codepig.common.databinding.ActivityExampleBinding;
import com.codepig.common.example.callback.ExampleCB;
import com.codepig.common.example.viewmodle.ExampleVM;
import com.codepig.common.viewmodel.BaseVM;

/**
 * 小区公告
 */
public class ExampleActivity extends BaseTitleActivity implements ExampleCB {
    private final String TAG = ExampleActivity.class.getSimpleName();
    private ActivityExampleBinding binding;
    private ExampleVM vm;

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_example;
    }

    @Override
    public BaseVM initViewModel() {
        vm = new ExampleVM(this);
        return vm;
    }

    @Override
    public String setTitle() {
        return "范例";
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        binding = (ActivityExampleBinding) baseViewBinding;
        setISlenderLineGone();
    }

    @Override
    public void showData() {
    }
}
