package com.codepig.common.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepig.common.R;
import com.codepig.common.callback.BaseCB;
import com.codepig.common.util.KeyBoardUtil;
import com.codepig.common.util.StringUtil;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.util.WindowUtil;
import com.codepig.common.view.LoadingDialog.LoadingDialog;
import com.codepig.common.viewmodel.BaseVM;

/**
 * Create by huscarter@163.com on 7/11/17
 * <p>
 * activity dataBinding基类
 * <p>
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseCB {
    /**
     * 基础binding view
     */
    protected ViewDataBinding baseBinding;
    /**
     * 基础的view model
     */
    protected BaseVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtil.isEmpty(getTAG())) {
            throw new NullPointerException();
        }
        baseBinding = DataBindingUtil.setContentView(this, setLayout());
        try {
            WindowUtil.setStatusbarColor(BaseActivity.this, getStatusBarColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        vm = initViewModel();

        initAll(savedInstanceState);
    }

    public int getStatusBarColor(){
        return R.color.white;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeKeyBoard();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (vm !=  null) {
            vm.unRegisterRxBus();
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 点击空白处，隐藏软键盘
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();

            if (view instanceof EditText) {
                //
            } else {
                KeyBoardUtil.closeKeyWords(BaseActivity.this);
            }
        }
        return super.dispatchTouchEvent(event);
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
    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void showLoaddingDialog() {
        LoadingDialog.show(BaseActivity.this, R.string.wait);
    }

    @Override
    public void dismissLoaddingDialog() {
        LoadingDialog.dismiss();
    }

    @Override
    public void doIntent(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if ( bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void doIntentClassName(String clsname, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(BaseActivity.this, clsname);
        if ( bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void doIntentForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if ( bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void doInentClassNameForResult(String clsname, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClassName(BaseActivity.this, clsname);
        if ( bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public Resources getAppResource() {
        return getResources();
    }

    @Override
    public void finishActivity(){
        finish();
    }

    @Override
    public String getStringFromId(int stringId) {
        return getString(stringId);
    }

    @Override
    public void setResultCode(int resultValue, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(resultValue, intent);
    }

    @Override
    public Bundle getIntentData() {
        Bundle bundle = null;
        if (getIntent() != null) {
            bundle = getIntent().getExtras();
        }
        return bundle;
    }

    @Override
    public void closeKeyBoard() {
        KeyBoardUtil.closeKeyWords(this);
    }

    /**
     * 设置log打印,方便调试
     * @return
     */
    public abstract String getTAG();

    /**
     * 设置activity的布局
     *
     * @return
     */
    public abstract int setLayout();

    /**
     * 子类实例化View Model，并返回.
     *
     * @return View Model
     */
    public abstract BaseVM initViewModel();

    /**
     * 初始化数据
     *
     * @param savedInstanceState data state
     */
    protected abstract void initAll(Bundle savedInstanceState);
}
