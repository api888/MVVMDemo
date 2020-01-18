package com.codepig.customerview.viewmodel;

import com.codepig.common.viewmodel.BaseVM;
import com.codepig.customerview.callback.TempCB;

public class TempVM extends BaseVM {
    private TempCB callback;

    /**
     * 不需要callback可以传null
     *
     * @param callBack
     */
    public TempVM(TempCB callBack) {
        super(callBack);
        this.callback = callBack;
    }

    public void getList(boolean isRef){
        callback.showData();
    }
}
