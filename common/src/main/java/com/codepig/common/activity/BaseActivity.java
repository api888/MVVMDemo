package com.codepig.common.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.codepig.common.R;
import com.codepig.common.callback.BaseCB;
import com.codepig.common.util.KeyBoardUtil;
import com.codepig.common.util.PermissionCompat;
import com.codepig.common.util.StringUtil;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.view.PointOnlyLoadingDialog;
import com.codepig.common.view.TextOnlyLoadingDialog;
import com.codepig.common.viewmodel.BaseVM;

import static com.codepig.common.util.PermissionCompat.REQUEST_CALL_PHONE;
import static com.codepig.common.util.PermissionCompat.REQUEST_CAMERA;
import static com.codepig.common.util.PermissionCompat.REQUEST_EXTERNAL_STORAGE;
import static com.codepig.common.util.PermissionCompat.REQUEST_GALLERY;
import static com.codepig.common.util.PermissionCompat.REQUEST_RECODE;
import static com.codepig.common.util.WindowUtil.hasNavBar;

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
    private Context context;
    /**
     * 基础的view model
     */
    protected BaseVM vm;

    protected TextOnlyLoadingDialog dialog;
    protected PointOnlyLoadingDialog dialogP;

    private FrameLayout content;
    private boolean mLayoutComplete = false;
    private Handler handler = new Handler();
    private int BottomStatusHeight=0;
    private boolean BottomStatusHide=false;
    private boolean hideWait=false;

    //权限设置跳转提示框
    private AlertDialog permissionAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        if (StringUtil.isEmpty(getTAG())) {
            throw new NullPointerException();
        }
        baseBinding = DataBindingUtil.setContentView(this, setLayout());

        vm = initViewModel();

        //状态栏隐藏
//        setTopBarState(true);
        //状态栏透明
        setTopBarTranslate(true);

        //监听系统界面变化，主要是状态栏和虚拟按钮(华为无效)
//        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
//            setTopBarTranslate(true);
//        });

        permissionAlertDialog = new AlertDialog.Builder(this).create();
        permissionAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"去开启", alertListener);
        permissionAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", alertListener);

        initAll(savedInstanceState);

//        SoftHideKeyBoardUtil.assistActivity(this);//软键盘遮挡问题

        content = findViewById(android.R.id.content);
        content.post(() -> mLayoutComplete = true);
        //监听系统界面变化，主要是状态栏和虚拟按钮
        content.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!mLayoutComplete)
                return;
            if(hideWait)
                return;
            hideWait=true;
            handler.postDelayed(() -> {
                hideWait=false;
                setTopBarTranslate(true);
            }, 5000);
        });
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
     * 设置隐藏状态栏及虚拟按钮
     * @param isHide
     */
    public void setTopBarTranslate(boolean isHide){
        if(isHide && hasNavBar(this)) {
            //隐藏虚拟按键，并且全屏
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View v = this.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
                decorView.setSystemUiVisibility(uiOptions);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if(isHide) {
////
//                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
////            uiFlags |= 0x00001000;
//                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
//                getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            }else{
//                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
////            uiFlags |= 0x00001000;
//                getWindow().getDecorView().setSystemUiVisibility(uiFlags);
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
//                getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            }
//        }
    }

    public int getStatusBarColor(){
        return R.color.white;
    }

    public void showPermissionAlert(String msg){
        permissionAlertDialog.setMessage(msg);
        permissionAlertDialog.show();
    }

    public void hidePermissionAlert(){
        permissionAlertDialog.dismiss();
    }

    DialogInterface.OnClickListener alertListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                PermissionCompat.getInstance().goPermissionSet(this);
                hidePermissionAlert();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                hidePermissionAlert();
                break;
            default:
                break;
        }
    };

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean _permission=true;
        switch (requestCode){
            case REQUEST_RECODE:
            case REQUEST_CAMERA:
            case REQUEST_EXTERNAL_STORAGE:
            case REQUEST_CALL_PHONE:
            case REQUEST_GALLERY:
                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]!=0){
                        _permission=false;
                    }
                }
                if(_permission){
                    if(permissionAlertDialog.isShowing()) {
                        hidePermissionAlert();
                    }
                }
                break;
        }
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
//        TextOnlyLoadingDialog.Builder builder=new TextOnlyLoadingDialog.Builder(BaseActivity.this)
//                .setMessage("请等待...")
//                .setCancelable(false);
//        dialog=builder.create();
//        dialog.show();

        PointOnlyLoadingDialog.Builder builder=new PointOnlyLoadingDialog.Builder(BaseActivity.this)
                .setCancelable(false);
        dialogP=builder.create();
        dialogP.show();
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
        if(dialog!=null) {
            dialog.dismiss();
        }
        if(dialogP!=null){
            dialogP.dismiss();
        }
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
