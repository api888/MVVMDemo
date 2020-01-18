package com.codepig.customerview.viewmodel;

import com.codepig.common.viewmodel.BaseVM;
import com.codepig.customerview.callback.TempFragmentCB;

public class TempFragmentVM extends BaseVM {
    private TempFragmentCB cb;

    /**
     * 不需要callback可以传null
     *
     * @param callBack
     */
    public TempFragmentVM(TempFragmentCB callBack) {
        super(callBack);
        this.cb = callBack;
    }

    public void getList(){
    }
}
