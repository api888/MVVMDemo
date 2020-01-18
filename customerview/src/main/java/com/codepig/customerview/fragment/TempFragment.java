package com.codepig.customerview.fragment;

import android.os.Bundle;
import android.view.View;

import com.codepig.common.databindingbase.BaseFragment;
import com.codepig.common.viewmodel.BaseVM;
import com.codepig.customerview.R;
import com.codepig.customerview.callback.TempFragmentCB;
import com.codepig.customerview.databinding.FragmentTempBinding;
import com.codepig.customerview.viewmodel.TempFragmentVM;

/**
 * 暂时还没做的占位fragment
 */
public class TempFragment extends BaseFragment implements TempFragmentCB {
    private final String TAG = TempFragment.class.getSimpleName();
    private FragmentTempBinding binding;
    private TempFragmentVM vm;

    @Override
    public BaseVM initViewModel() {
        vm = new TempFragmentVM(this);
        return vm;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_temp;
    }

    @Override
    public void initAll(View view, Bundle savedInstanceState) {
        binding = (FragmentTempBinding) baseBinding;

        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setListener() {
    }

    @Override
    public String getTAG() {
        return TAG;
    }

}
