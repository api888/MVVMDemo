package com.codepig.customerview.windows;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import com.codepig.common.bean.BaseData;
import com.codepig.common.config.IntentConfig;
import com.codepig.common.net.HttpUtils;
import com.codepig.common.net.RxRequest;
import com.codepig.common.net.ServiceCallback;
import com.codepig.common.net.ThreadPoolUtils;
import com.codepig.common.util.DensityUtil;
import com.codepig.common.util.MediaUtil;
import com.codepig.common.util.ScreenShotUtils;
import com.codepig.common.util.ShareUtils;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.util.glide.GlideUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.WindowShareBinding;
import com.everyone.net.bean.ShareBean;
import com.everyone.net.bean.UpperInfoBean;
import com.everyone.net.bean.UserInfoBean;
import com.everyone.net.bean.VideoInfoBean;
import com.everyone.net.data.UserConfig;
import com.everyone.net.service.MainApi;
import com.everyone.net.utils.HttpUtil;
import com.everyone.net.utils.HttpVersionConfig;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 视频分享
 */
public class ShareWindow extends PopupWindow {
    private Context context;
    private WindowShareBinding binding;
    private ShareWindowListener listener;
    private VideoInfoBean vInfo;

    private AlertDialog alertDialog;
    private File shareFile;
    private int shareType=1;

    private Bitmap bitmap;
    private Bitmap layoutBitmap;
    private VideoInfoBean videoInfoBean;

    private String target;//1:视频;2:话题;3:社群
    private String target_id;

    /**
     * 没有图片版
     * @param context
     * @param shareWindowListener
     */
    public ShareWindow(final Context context, ShareWindowListener shareWindowListener) {
        this.context=context;
        this.listener = shareWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_share, null, false);
        init();
    }

    /**
     * 页面截图版
     * @param context
     * @param shareWindowListener
     * @param _bitmap
     */
    public ShareWindow(final Context context, ShareWindowListener shareWindowListener,Bitmap _bitmap) {
        bitmap=_bitmap;
        this.context=context;
        this.listener = shareWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_share, null, false);

        init();
    }

    /**
     * 视频截图版
     * @param context
     * @param shareWindowListener
     */
    public ShareWindow(final Context context, ShareWindowListener shareWindowListener,VideoInfoBean videoInfo,String _target,String _target_id) {
        this.context=context;
        target=_target;
        target_id=_target_id;
        videoInfoBean=videoInfo;
        if(videoInfoBean==null){
        }
        this.listener = shareWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_share, null, false);

        init();
    }

    private void init(){
        setContentView(binding.getRoot());
        setWidth(MATCH_PARENT);
        setHeight(MATCH_PARENT);

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", alertListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", alertListener);

        UserInfoBean userInfo=UserConfig.getUserInfoBean();
        GlideUtil.load(binding.userHead, userInfo.getAvatars());
        binding.userName.setText(userInfo.getNick_name());
        binding.promotionCode.setText("邀请码："+userInfo.getPromotion_code());
        String contentEtString="api.qihuobense.com/api/v1/promotion_code/"+userInfo.getPromotion_code();
        Bitmap qrBitmap = CodeCreator.createQRCode(contentEtString, 400,400, null);
        binding.qrCodeImage.setImageBitmap(qrBitmap);
        if(videoInfoBean!=null) {
            if (!TextUtils.isEmpty(videoInfoBean.getVideo_cover())) {
                GlideUtil.load(binding.pageShot, videoInfoBean.getVideo_cover());
            }
        }

//        setOnDismissListener(() -> WindowUtil.setBackgroundAlpha((Activity) context, 1f));

        setListener();
    }

    private void setListener() {
        binding.closeBtn.setOnClickListener(v -> dismiss());
        binding.unSubBtn.setOnClickListener(v -> listener.onSendSub());
        binding.subBtn.setOnClickListener(v -> listener.onSendSub());
        binding.payBtn.setOnClickListener(v -> listener.onPay());
        binding.reportBtn.setOnClickListener(v -> listener.onReport());
        binding.unlikeBtn.setOnClickListener(v -> ToastUtil.showToast("已提交"));
        binding.shareTimeLineBtn.setOnClickListener(v -> saveImageAndShare(0));
        binding.shareWeChatBtn.setOnClickListener(v -> saveImageAndShare(1));
        binding.shareWeiboBtn.setOnClickListener(v -> saveImageAndShare(2));
        binding.shareQQBtn.setOnClickListener(v -> saveImageAndShare(4));
        binding.shareQZoneBtn.setOnClickListener(v -> saveImageAndShare(5));

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {//有封面图就加载封面图，没有就视频截图
                if(videoInfoBean !=null && !TextUtils.isEmpty(videoInfoBean.getVideo_cover())){
                    new Handler().postDelayed(() -> setImage(),500); // 延时1秒
                }else{
                    if(videoInfoBean !=null) {
                        try {
                            Runnable bmpR = () -> {
                                final Bitmap bitmap = MediaUtil.getBitmapForVideo(videoInfoBean.getVideo_preview_url());
                                if (bitmap != null) {
                                    binding.pageShot.post(() -> {
                                        binding.pageShot.setImageBitmap(bitmap);
                                        setImage();
                                    });
                                }
                            };
                            ThreadPoolUtils.execute(bmpR);
                        } catch (Exception e) {
                            System.out.println("UserVideoPlayerAdapter getBitmap error:" + e.toString());
                            binding.shareImage.setVisibility(View.VISIBLE);
                            setImage();
                        }
                    }
                }
                //处理完后remove掉
                binding.getRoot().getViewTreeObserver() .removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setImage(){
        binding.shareImage.setVisibility(View.VISIBLE);
        layoutBitmap= ScreenShotUtils.getViewBitmap(binding.shareLayout);
        binding.shareImage.setImageBitmap(layoutBitmap);
        binding.shareImage.getLayoutParams().width= DensityUtil.getScreenWidth(context)*2/5;
        binding.shareImage.getLayoutParams().height= (DensityUtil.getScreenWidth(context)*2/5)*layoutBitmap.getHeight()/layoutBitmap.getWidth();
    }

    private void saveImageAndShare(int index){
        shareType=index;
        //保存图片
        if(layoutBitmap!=null) {
            shareFile = MediaUtil.saveImageToGallery(context, layoutBitmap);
            if (shareFile != null) {
                switch (shareType) {
                    case 0:
                        showAlert("图片已存入相册，要去朋友圈分享吗？");
                        break;
                    case 1:
                        showAlert("图片已存入相册，要去微信分享吗？");
                        break;
                    case 2:
                        showAlert("图片已存入相册，要去微博分享吗？");
                        break;
                    case 3:
                        showAlert("图片已存入相册，要去QQ分享吗？");
                        break;
                    case 4:
                        showAlert("图片已存入相册，要去QQ空间分享吗？");
                        break;
                }
                listener.onShare(index);
            } else {
                ToastUtil.showToast("图片保存失败");
            }
        }
    }

    //显示警告消息框
    private void showAlert(String msg){
        alertDialog.setMessage(msg);
        alertDialog.show();
    }

    //隐藏警告消息框
    private void hideAlert(){
        alertDialog.hide();
    }

    //警告框监听
    DialogInterface.OnClickListener alertListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                switch (shareType){
                    case 0:
                        ShareUtils.shareToTimeLine(context,"",shareFile);
                        break;
                    case 1:
                        ShareUtils.shareToWxFriend(context,"",shareFile);
                        break;
                    case 2:
                        ShareUtils.shareToWeibo(context,false,shareFile);
                        break;
                    case 3:
                        ShareUtils.shareToQQFriend(context,bitmap);
                        break;
                    case 4:
                        ShareUtils.shareToQzone(context,shareFile);
                        break;
                }
                hideAlert();
                shareCount();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                hideAlert();
                break;
            default:
                break;
        }
    };

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        setTouchable(true);
        super.showAtLocation(parent, gravity, x, y);
//        WindowUtil.setBackgroundAlpha((Activity) parent.getContext(), 0.5f);
    }

    public interface ShareWindowListener {
        void onSendSub();
        void onPay();
        void onReport();
        void onShare(int index);
    }

    public VideoInfoBean getvInfo() {
        return vInfo;
    }

    public void setvInfo(VideoInfoBean vInfo) {
        this.vInfo = vInfo;
        binding.setData(vInfo);
        if(vInfo.isTo_subscribe()){
            binding.subBtn.setVisibility(View.GONE);
            binding.unSubBtn.setVisibility(View.VISIBLE);
        }else{
            binding.subBtn.setVisibility(View.VISIBLE);
            binding.unSubBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 分享统计
     */
    public void shareCount(){
        Map<String, String> map = new HashMap<>();
        map.put("user_id", UserConfig.getUserId());
        map.put("target",target);
        map.put("target_id",target_id);

        Map paramMap= HttpUtil.getSign(map);
        if(paramMap==null)
            return;
        HttpUtils.doHttp(new CompositeDisposable(),
                RxRequest.create(MainApi.class, HttpVersionConfig.API_DEFAULT).shareCount(paramMap),
                new ServiceCallback<BaseData<ShareBean>>() {
                    @Override
                    public void onError(Throwable error) {
                    }

                    @Override
                    public void onSuccess(BaseData<ShareBean> data) {
                        if(data.getRespCode().equals("0")){
                            System.out.println("share count:"+data.getData().getTotal_share());
                        }else{
                            ToastUtil.showToast(data.getRespMsg(),true);
                        }
                    }

                    @Override
                    public void onDefeated(BaseData<ShareBean> data) {
                    }
                });
    }

    public String getTarget() {
        return target;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }
}
