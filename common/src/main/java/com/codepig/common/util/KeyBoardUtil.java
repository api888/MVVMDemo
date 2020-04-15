package com.codepig.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

import static com.codepig.common.util.DensityUtil.getScreenHeight;

/**
 * 软键盘工具
 */

public class KeyBoardUtil {
    private static final String TAG = KeyBoardUtil.class.getSimpleName();

    public static final String SHARE_PREFERENCE_NAME = "com.codepig.keyboardUtil";
    public static final String SHARE_PREFERENCE_TAG = "soft_input_height";

    public static int keyboardHeight = 0;
    private static boolean isVisiableForLast = false;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = null;

    /**
    * @description 打开软键盘
    * @author nmssdmf
    * @date 2018/6/5 0005 14:42
    * @version v3.2.0
    */
    public static void openKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
    * @description
    * @date 2018/6/5 0005 14:44
    * @version v3.2.0
    */
    public static void closeKeyWords(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive() && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void closeKeyWords(EditText mEditText){
        if (isSoftInputShow((Activity) mEditText.getContext())) {
            InputMethodManager imm = (InputMethodManager) mEditText.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
    }

    /**
     * 判断当前软键盘是否打开
     * @param activity
     * @return
     */
    public static boolean isSoftInputShow(Activity activity) {
        final View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    public static int getSupportSoftInputHeight(Activity mActivity) {
        SharedPreferences sp = mActivity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Rect r = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight(mActivity);
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
            return 0;
        }
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_TAG, softInputHeight).apply();
        }
        return softInputHeight;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity mActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    //获取底部导航的高度
    public static int getBottomStatusHeight(Context context) {
        if (checkNavigationBarShow(context)) {
            int totalHeight = getDpi(context);
            int contentHeight = getScreenHeight(context);
//            System.out.println("----显示虚拟导航了--");
            return totalHeight - contentHeight;
        } else {
//            System.out.println("----没有虚拟导航 或者虚拟导航隐藏--");
            return 0;
        }
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     * @param context
     * @return
     */
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 判断虚拟导航栏是否显示
     * @param context 上下文对象
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean checkNavigationBarShow(@NonNull Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            //判断是否隐藏了底部虚拟导航
            int navigationBarIsMin = 0;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                navigationBarIsMin = Settings.System.getInt(context.getContentResolver(),
                        "navigationbar_is_min", 0);
            } else {
                navigationBarIsMin = Settings.Global.getInt(context.getContentResolver(),
                        "navigationbar_is_min", 0);
            }
            if ("1".equals(navBarOverride) || 1 == navigationBarIsMin) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }
}
