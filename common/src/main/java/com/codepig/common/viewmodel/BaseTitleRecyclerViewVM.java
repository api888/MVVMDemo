package com.codepig.common.viewmodel;

import com.codepig.common.callback.BaseTitleRecyclerViewCB;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTitleRecyclerViewVM extends BaseVM {

    protected List list = new ArrayList<>();
    protected BaseTitleRecyclerViewCB baseTitleRecyclerViewCB;
    /**
     * 不需要callback可以传null
     *
     * @param callBack
     */
    public BaseTitleRecyclerViewVM(BaseTitleRecyclerViewCB callBack) {
        super(callBack);
        baseTitleRecyclerViewCB = callBack;
    }

    public abstract void initData(boolean isRefresh);

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
