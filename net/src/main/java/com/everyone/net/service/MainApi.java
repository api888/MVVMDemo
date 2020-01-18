package com.everyone.net.service;

import com.codepig.common.bean.BaseData;
import com.codepig.common.bean.BaseListData;
import com.everyone.net.bean.GroupListBean;
import com.everyone.net.bean.UploadFile;
import com.everyone.net.bean.VersionStatusBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface MainApi {
    /**
     * 获取版本信息
     */
    @FormUrlEncoded
    @POST("/api/v1/get_version_status")
    Observable<BaseData<VersionStatusBean>> getVersionStatus(@FieldMap Map<String, String> map);

    /**
     * 关注列表
     */
    @FormUrlEncoded
    @POST("/api/v1/get_subscribe_community")
    Observable<BaseListData<GroupListBean>> getGroupList(@FieldMap Map<String, String> map);

    /**
     * 上传文件
     */
    @Multipart
    @POST("/api/v1/upload")
    Observable<BaseData<UploadFile>> uploadFile(@Part MultipartBody.Part body, @PartMap Map<String, RequestBody> map);

}
