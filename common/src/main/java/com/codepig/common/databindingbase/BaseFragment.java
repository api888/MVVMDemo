package com.codepig.common.databindingbase;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepig.common.R;
import com.codepig.common.activity.BaseActivity;
import com.codepig.common.callback.BaseCB;
import com.codepig.common.util.KeyBoardUtil;
import com.codepig.common.util.StringUtil;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.view.LoadingDialog.LoadingDialog;
import com.codepig.common.view.TextOnlyLoadingDialog;
import com.codepig.common.viewmodel.BaseVM;


/**
 * @author nmssdmf
 * @version v3.2.1
 * @descriptionb 作为最基本的databinding的framgment, 在基层BaseNewLibFragment的基础上, 新增了datadinding的对象和vm的对象
 * 添加了databinding的动画供子类调用,同时提供了最基本的接口,供子类实现
 * @date 2018/7/4 0004 15:23
 */
public abstract class BaseFragment extends Fragment implements BaseCB {
    /**
     * 基础view model
     */
    protected BaseVM vm;
    /**
     * 基础binding view
     */
    protected ViewDataBinding baseBinding;

    protected TextOnlyLoadingDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (baseBinding != null) {
            ViewGroup parent = (ViewGroup) baseBinding.getRoot().getParent();
            if (parent != null) {
                parent.removeView(baseBinding.getRoot());
            }
            return baseBinding.getRoot();
        }

        vm = initViewModel();

        baseBinding = DataBindingUtil.inflate(inflater, setLayout(), container, false);

        initAll(baseBinding.getRoot(), savedInstanceState);

        if (StringUtil.isEmpty(getTAG())) {
            throw new NullPointerException();
        }
        return baseBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 添加data binding动画
     */
    protected void addAnimation() {
        baseBinding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public boolean onPreBind(ViewDataBinding binding) {
                ViewGroup sceneRoot = (ViewGroup) binding.getRoot();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(sceneRoot);
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        if (vm != null) {
            vm.unregisterRx();
            vm.unRegisterRxBus();
        }
        super.onDetach();
    }

    /**
     * 子类实例化View Model，并返回.
     *
     * @return View Model
     */
    public abstract BaseVM initViewModel();

    /**
     * 设置activity对应的布局文件
     *
     * @return 布局文件的id
     */
    public abstract int setLayout();

    /**
     * 初始化
     * @param view
     * @param savedInstanceState
     */
    public abstract void initAll(View view, Bundle savedInstanceState);

    public abstract String getTAG();

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void showLoaddingDialog() {
//        LoadingDialog.show(getActivity(), R.string.wait);
        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(getActivity())
                .setMessage("请等待...")
                .setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    @Override
    public void showLoaddingDialog(int resid) {
//        LoadingDialog.show(getActivity(), resid);
        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(getActivity())
                .setMessage(getActivity().getString(resid))
                .setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    @Override
    public void showLoaddingDialog(String msg) {
//        LoadingDialog.show(getActivity(), msg);
        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(getActivity())
                .setMessage(msg)
                .setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    @Override
    public void dismissLoaddingDialog() {
//        LoadingDialog.dismiss();
        dialog.dismiss();
    }

    @Override
    public void doIntent(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }

    @Override
    public void doIntentClassName(String clsname, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(getActivity(), clsname);
        if ( bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }

    @Override
    public void doIntentForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
       startActivityForResult(intent, requestCode);
    }

    @Override
    public void doInentClassNameForResult(String clsname, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClassName(getActivity(), clsname);
        if ( bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivityForResult(intent, requestCode);
    }

    @Override
    public Resources getAppResource() {
        return getResources();
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void setResultCode(int resultCode, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().setResult(resultCode, intent);
    }


    @Override
    public String getStringFromId(int stringId) {
        return getString(stringId);
    }

    @Override
    public Bundle getIntentData() {
        Bundle bundle = null;
        if (getActivity().getIntent() != null) {
            bundle = getActivity().getIntent().getExtras();
        }
        return bundle;
    }

    @Override
    public void closeKeyBoard() {
        KeyBoardUtil.closeKeyWords(getActivity());
    }
}
