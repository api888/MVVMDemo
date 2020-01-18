package com.codepig.customerview.viewmodel;

import com.codepig.common.bean.BaseData;
import com.codepig.common.bean.BaseListData;
import com.codepig.common.net.HttpUtils;
import com.codepig.common.net.RxRequest;
import com.codepig.common.net.ServiceCallback;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.viewmodel.BaseVM;
import com.codepig.customerview.callback.TestCB;
import com.everyone.net.bean.GroupListBean;
import com.everyone.net.bean.VideoInfoBean;
import com.everyone.net.data.UserConfig;
import com.everyone.net.service.MainApi;
import com.everyone.net.utils.HttpUtil;
import com.everyone.net.utils.HttpVersionConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestVM extends BaseVM {
    private TestCB callback;

    private boolean inOrder=false;

    private List<String> errorList=new ArrayList<>();

    private int count=0,gotCount=0,signErrorCount=0,errorCount=0,max=100;

    /**
     * 不需要callback可以传null
     *
     * @param callBack
     */
    public TestVM(TestCB callBack) {
        super(callBack);
        this.callback = callBack;
//        getVideoInfo();
    }

    public void getList(boolean isRef){
        callback.showData();
    }


    public void startTest(int maxCount,boolean _inOrder){
        max=maxCount;
        count=0;
        gotCount=0;
        signErrorCount=0;
        errorCount=0;
        inOrder=_inOrder;
        errorList=new ArrayList<>();
        if(inOrder){
            getVideoInfo();
        }else{
            for(int i=0;i<=max;i++){
                getVideoInfo();
            }
        }
    }

    private void gotTotal(){
        if(count>=max){
            System.out.println("log count:"+count);
            System.out.println("log gotCount:"+gotCount);
            System.out.println("log errorCount:"+errorCount);
            System.out.println("log signErrorCount:"+signErrorCount);
            String info="总计："+count+";错误："+errorCount+";其中签名错误："+signErrorCount+"\n";
            if(errorList.size()>0){
                info+="错误编号为：";
                for(int n=0;n<errorList.size();n++){
                    info+=errorList.get(n)+";";
                }
            }
            callback.showText(info);
            inOrder=false;
            return;
        }
        count++;
        callback.showText(count+"");
    }

    /**
     * 获取视频详情
     */
    public void getVideoInfo() {
//        System.out.println("log count:"+count);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", UserConfig.getUserId());
        map.put("video_id","2");

        Map paramMap= HttpUtil.getSign(map);
        if(paramMap==null)
            return;
        HttpUtils.doHttp(subscription,
                RxRequest.create(MainApi.class, HttpVersionConfig.API_DEFAULT).getVideoInfo(paramMap),
                new ServiceCallback<BaseData<VideoInfoBean>>() {
                    @Override
                    public void onError(Throwable error) {
                        gotCount++;
//                        System.out.println("log gotCount:"+gotCount);
                        errorCount++;
                        gotTotal();
                        if(inOrder) {
                            getVideoInfo();
                        }
                    }

                    @Override
                    public void onSuccess(BaseData<VideoInfoBean> data) {
                        gotCount++;
//                        System.out.println("log gotCount:"+gotCount);
                        if(inOrder) {
                            getVideoInfo();
                        }
                        if(data.getRespCode().equals("0")){
                        }else{
                            errorCount++;
                            errorList.add(count+"");
                            if(data.getRespMsg().equals("签名错误")){
                                signErrorCount++;
                            }
                            ToastUtil.showToast(data.getRespMsg());
                        }
                        gotTotal();
                    }

                    @Override
                    public void onDefeated(BaseData<VideoInfoBean> data) {
                        gotCount++;
//                        System.out.println("log gotCount:"+gotCount);
                        errorCount++;
                        gotTotal();
                        if(inOrder) {
                            getVideoInfo();
                        }
                    }
                });
    }

    /**
     * 获取关注群组
     */
    public void getGroupList() {
//        cb.showLoaddingDialog("加载中");
        Map<String, String> map = new HashMap<>();
        map.put("user_id", UserConfig.getUserId());
        map.put("pageNum","0");
        map.put("pageSize","10");

        Map paramMap= HttpUtil.getSign(map);
        if(paramMap==null)
            return;
        HttpUtils.doHttp(subscription,
                RxRequest.create(MainApi.class, HttpVersionConfig.API_DEFAULT).getGroupList(paramMap),
                new ServiceCallback<BaseListData<GroupListBean>>() {
                    @Override
                    public void onError(Throwable error) {
//                        cb.dismissLoaddingDialog();
                        gotCount++;
//                        System.out.println("log gotCount:"+gotCount);
                        errorCount++;
                        gotTotal();
                        if(inOrder) {
                            getGroupList();
                        }
                    }

                    @Override
                    public void onSuccess(BaseListData<GroupListBean> data) {
//                        cb.dismissLoaddingDialog();
                        gotCount++;
//                        System.out.println("log gotCount:"+gotCount);
                        if(inOrder) {
                            getGroupList();
                        }
                        if(data.getRespCode().equals("0")){
                        }else{
                            errorCount++;
                            if(data.getRespMsg().equals("签名错误")){
                                signErrorCount++;
                            }
                            ToastUtil.showToast(data.getRespMsg());
                        }
                        gotTotal();
                    }

                    @Override
                    public void onDefeated(BaseListData<GroupListBean> data) {
//                        cb.dismissLoaddingDialog();
                        gotCount++;
//                        System.out.println("log gotCount:"+gotCount);
                        errorCount++;
                        gotTotal();
                        if(inOrder) {
                            getGroupList();
                        }
                    }
                });
    }
}
