package com.codepig.common.callback;

import java.util.List;

public interface BaseTitleRecyclerViewCB extends BaseCB {

    void refreshAdapter(boolean isRefresh, List dataList);
}
