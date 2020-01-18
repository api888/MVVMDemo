package com.codepig.common.bean;

/**
 * Create by huscarter@163.com on 11/14/17
 * <p>
 * 基础的数据实体
 */
public final class BaseData<T> extends Base {
    private T respData;

    public T getData() {
        return respData;
    }

    public void setData(T data) {
        this.respData = data;
    }

}
