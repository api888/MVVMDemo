package com.codepig.common.net;

import com.codepig.common.bean.Base;
import com.codepig.common.config.StringConfig;
import com.codepig.common.net.retrofit.HttpObserver;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.view.LoadingDialog.LoadingDialog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
* @description 网络请求封装
* @date 2018/6/15 0015 13:46
* @version v3.2.0
*/
public class HttpUtils {
    /**
    * @description 请求网络接口
    * @date 2018/6/15 0015 14:27
    * @version v3.2.0
    */
    public static <T extends Base> void doHttp(CompositeDisposable subscription, Observable<T> observable, final ServiceCallback<T> callback){
        subscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new HttpObserver<T>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LoadingDialog.dismiss();
                        callback.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        LoadingDialog.dismiss();
//                        System.out.println("HttpUtil code:"+t.getRespCode());
                        if ("401".equals(t.getRespCode())) {
                            ToastUtil.showToast("code:401");
                            callback.onDefeated(t);
                        } else {
                            callback.onSuccess(t);
//                            ToastUtil.showToast("HttpUtil:"+t.getRespMsg());
                        }
                    }
                }));
    }

    /**
    * @description 接口返回status_code判断
    * @date 2018/6/15 0015 14:27
    * @version v3.2.0
    */
    public static boolean isReturnOk(String statusCode){
        if (StringConfig.OK.equals(statusCode)) {
            return true;
        }else {
            return false;
        }
    }
}
