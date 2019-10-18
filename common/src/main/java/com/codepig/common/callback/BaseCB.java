package com.codepig.common.callback;

import android.content.res.Resources;
import android.os.Bundle;

/**
 * 最基础的ui接口,需要BaseActivity和baseFragment都实现
 */

public interface BaseCB {
    /**
    * @description 显示toast
    * @author nmssdmf
    * @date 2018/7/4 0004 11:47
    * @version v3.2.1
    */
    void showToast(String msg);
    /**
    * @description 显示菊花图标
    * @date 2018/7/4 0004 11:47
    * @version v3.2.1
    */
    void showLoaddingDialog();
    /**
    * @description 关闭菊花显示
    * @date 2018/7/4 0004 13:44
    * @version v3.2.1
    */
    void dismissLoaddingDialog();
    /**
    * @description 跳转
    * @author nmssdmf
    * @date 2018/7/4 0004 11:47
    * @version v3.2.1
    */
    void doIntent(Class<?> cls, Bundle bundle);

    /**
     * @description 跳转
     * @author nmssdmf
     * @date 2018/7/4 0004 11:47
     * @version v3.2.1
     */
    void doIntentClassName(String clsname, Bundle bundle);

    /**
    * @description result跳转
    * @author nmssdmf
    * @date 2018/7/4 0004 14:35
    * @version v3.2.1
    */
    void doIntentForResult(Class<?> cls, Bundle bundle, int requestCode);

    /**
     * @description result跳转
     * @author nmssdmf
     * @date 2018/7/4 0004 14:35
     * @version v3.2.1
     */
    void doInentClassNameForResult(String clsname, Bundle bundle, int requestCode);

    /**
    * @description 获取资源
    * @author nmssdmf
    * @date 2018/7/4 0004 11:50
    * @version v3.2.1
    */
    Resources getAppResource();

    /**
     * 关闭activity
     */
    void finishActivity();

    /**
     * 获取string
     * @param stringId
     * @return
     */
    String getStringFromId(int stringId);

    /**
     * 设置activity回调
     * @param resultCode
     * @param bundle
     */
    void setResultCode(int resultCode, Bundle bundle);

    /**
     * 获取跳转传递的数据
     * @return
     */
    Bundle getIntentData();

    /**
     * 关闭软键盘
     */
    void closeKeyBoard();
}
