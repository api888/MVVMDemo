package com.codepig.common.activity;

import android.os.Bundle;
import android.os.Handler;

import com.codepig.common.R;
import com.codepig.common.callback.BaseTitleRecyclerViewCB;
import com.codepig.common.databinding.ActivityBaseTitleRecyclerviewBinding;
import com.codepig.common.databindingbase.BaseDataBindingAdapter;
import com.codepig.common.view.OnDataChangeListener;
import com.codepig.common.viewmodel.BaseTitleRecyclerViewVM;

import java.util.List;

public abstract class BaseTitleRecyclerViewActivity extends BaseTitleActivity implements BaseTitleRecyclerViewCB {
    protected ActivityBaseTitleRecyclerviewBinding binding;
    protected BaseDataBindingAdapter adapter;
    protected BaseTitleRecyclerViewVM vm;

    @Override
    public int getContentViewId() {
        return R.layout.activity_base_title_recyclerview;
    }

    @Override
    public BaseTitleRecyclerViewVM initViewModel() {
        vm = initTitleRecyclerViewViewModel();
        return vm;
    }


    @Override
    public void initContent(Bundle savedInstanceState) {
        binding = (ActivityBaseTitleRecyclerviewBinding) baseViewBinding;
        adapter = initAdapter(vm.getList());
        binding.crv.setAdapter(adapter);

        binding.crv.setOnDataChangeListener(new OnDataChangeListener() {
            @Override
            public void onRefresh() {
                vm.initData(true);
            }

            @Override
            public void onLoadMore() {
                vm.initData(false);
            }
        });

        vm.initData(false);
    }

    public abstract BaseTitleRecyclerViewVM initTitleRecyclerViewViewModel();
    public abstract BaseDataBindingAdapter initAdapter(List list);

    @Override
    public void refreshAdapter(final boolean isRefresh,final  List dataList) {
        if (isRefresh)
            binding.crv.setRefreshing(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataChangedAfterLoadMore(isRefresh, dataList);
            }
        });
    }
}
