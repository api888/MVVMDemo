package com.codepig.common.callback;

import java.util.List;

public interface BaseRecyclerViewFragmentCB extends BaseCB {
    void refreshAdapter(boolean isRefresh, List dataList);
    void stopRefreshAnim();
}
