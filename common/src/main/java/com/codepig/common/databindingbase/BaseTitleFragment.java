package com.codepig.common.databindingbase;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.codepig.common.R;
import com.codepig.common.databinding.FragmentBaseTitleBinding;


/**
* @description 作为最基本的titlefragment,在继承BaseNewLibBindingFragment的基础上,新增了toolbar,
 * 并实现了返回键的监听事件和ui设置
* @author nmssdmf
* @date 2018/7/4 0004 15:23
* @version v3.2.1
*/
public abstract class BaseTitleFragment extends BaseFragment {
    protected FragmentBaseTitleBinding baseTitleBinding;
    protected ViewDataBinding baseViewBinding;

    @Override
    public void initAll(View view, Bundle savedInstanceState) {
        baseTitleBinding = (FragmentBaseTitleBinding) baseBinding;
        baseViewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getContentViewId(), null, false);
        baseTitleBinding.llRootView.addView(baseViewBinding.getRoot(), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initTitleView();
        initContent(savedInstanceState);
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_base_title;
    }

    private void initTitleView(){
        setTitle(setTitle());
        baseTitleBinding.tTitle.setNavigationIcon(getDefaultNavigationIcon());
        setNavigationClickListener();
    }

    /**
     * <p>设置顶部的标题，之所以单独提供抽象方法，是考虑到程序有可能不使用toolbar设置标题（标题需要居中）。</p>
     * <p>toolbar.refreshTitle(refreshTitle()); 靠左标题</p>
     * <p>tvTitle.setText(refreshTitle()); 剧中标题/默认</p>
     *
     * @return title
     */
    public abstract String setTitle();

    public void setTitle(String title) {
        baseTitleBinding.tvTitle.setText(title);
    }

    /**
     * <p>设置toolbar navigation icon，默认使用v7的drawable，此方法不建议重写。</p>
     *
     * @return
     */
    public int getDefaultNavigationIcon() {
        return R.drawable.title_arrow;
    }


    /**
     * @description 去除Navigation
     * @author nmssdmf
     * @date 2018/10/15 0015 17:59
     * @params
     * @return
     */
    protected void hideNavigation(){
        baseTitleBinding.tTitle.setNavigationIcon(null);
    }

    /**
     * 设置事件
     */
    private void setNavigationClickListener() {
        baseTitleBinding.tTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    /**
     * @description 设置子类视图view
     * @author nmssdmf
     * @date 2018/10/16 0016 9:50
     */
    public abstract int getContentViewId();


    /**
     * 除了title之外的view初始化
     */
    public abstract void initContent(Bundle savedInstanceState);

}
