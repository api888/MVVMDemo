package com.codepig.common.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
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
import com.codepig.common.view.LoadingDialog.LoadingDialog;
import com.codepig.common.view.TextOnlyLoadingDialog;
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

    protected TextOnlyLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtil.isEmpty(getTAG())) {
            throw new NullPointerException();
        }
        baseBinding = DataBindingUtil.setContentView(this, setLayout());

        vm = initViewModel();

        //状态栏隐藏
//        setTopBarState(true);
        //状态栏透明
        setTopBarTranslate(true);

        initAll(savedInstanceState);
    }

    /**
     * 设置状态栏是否隐藏
     * @param isHide
     */
    public void setTopBarHide(boolean isHide){
        if(isHide) {
//            try {
//                WindowUtil.setStatusbarColor(BaseActivity.this, getStatusBarColor());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }else{
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    /**
     * 设置状态栏是否透明
     * @param isHide
     */
    public void setTopBarTranslate(boolean isHide){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(isHide) {
//            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
//            }
//
                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            uiFlags |= 0x00001000;
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }else{
                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            uiFlags |= 0x00001000;
                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
                getWindow().setStatusBarColor(Color.BLACK);
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
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
        setTopBarTranslate(true);
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
//        LoadingDialog.show(BaseActivity.this, R.string.wait);
        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(BaseActivity.this)
                .setMessage("请等待...")
                .setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    @Override
    public void showLoaddingDialog(int resid) {
//        LoadingDialog.show(BaseActivity.this, resid);//带转圈圈的Dialog
        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(BaseActivity.this)
                .setMessage(BaseActivity.this.getString(resid))
                .setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    @Override
    public void showLoaddingDialog(String msg) {
        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(BaseActivity.this)
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
