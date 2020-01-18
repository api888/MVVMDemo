package com.codepig.customerview.windows;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.codepig.common.bean.BaseData;
import com.codepig.common.net.HttpUtils;
import com.codepig.common.net.RxRequest;
import com.codepig.common.net.ServiceCallback;
import com.codepig.common.util.DensityUtil;
import com.codepig.common.util.MediaUtil;
import com.codepig.common.util.ScreenShotUtils;
import com.codepig.common.util.ShareUtils;
import com.codepig.common.util.ToastUtil;
import com.codepig.common.util.glide.GlideUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.WindowSimpleShareBinding;
import com.everyone.net.bean.ShareBean;
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

/**
 * 简易分享window
 */
public class SimpleShareWindow extends PopupWindow {
    private Context context;
    private WindowSimpleShareBinding binding;
    private SimpleShareWindowListener listener;
    private VideoInfoBean vInfo;

    private AlertDialog alertDialog;
    private File shareFile;
    private int shareType=1;

    private Bitmap bitmap;
    private Bitmap layoutBitmap;

    private String target;//1:视频;2:话题;3:社群
    private String target_id;

    public SimpleShareWindow(final Context context, SimpleShareWindowListener shareWindowListener) {
        this.context=context;
        this.listener = shareWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_simple_share, null, false);
        init(false);
    }

    /**
     * 带页面截图
     * @param context
     * @param shareWindowListener
     */
    public SimpleShareWindow(final Context context, SimpleShareWindowListener shareWindowListener, Bitmap _bitmap,boolean withPageShot,String _target,String _target_id) {
        this.context=context;
        target=_target;
        target_id=_target_id;
        bitmap=_bitmap;
        this.listener = shareWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_simple_share, null, false);

        init(withPageShot);
    }

    private void init(boolean withPageShot){
        setContentView(binding.getRoot());
        setWidth(MATCH_PARENT);
        setHeight(MATCH_PARENT);

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", alertListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", alertListener);
//        setOnDismissListener(() -> WindowUtil.setBackgroundAlpha((Activity) context, 1f));

        if(withPageShot){
            UserInfoBean userInfo= UserConfig.getUserInfoBean();
            if(userInfo!=null) {
                GlideUtil.load(binding.userHead, userInfo.getAvatars());
                binding.userName.setText(userInfo.getNick_name());
                binding.promotionCode.setText("邀请码：" + userInfo.getPromotion_code());
                String contentEtString = "api.myhost.com/api/v1/promotion_code/" + userInfo.getPromotion_code();
                Bitmap qrBitmap = CodeCreator.createQRCode(contentEtString, 400, 400, null);
                binding.qrCodeImage.setImageBitmap(qrBitmap);
                if (bitmap != null) {
                    binding.pageShot.setImageBitmap(bitmap);
                }
            }
        }else{
            if(bitmap!=null) {
                binding.shareImage.setImageBitmap(bitmap);
                binding.shareImage.setVisibility(View.VISIBLE);
                layoutBitmap = bitmap;
            }
        }
        new Handler().postDelayed(() -> setImage(withPageShot),500); // 延时1秒
        setListener();
    }

    private void setListener() {
        binding.closeBtn.setOnClickListener(v -> dismiss());
        binding.shareTimeLineBtn.setOnClickListener(v -> saveImageAndShare(0));
        binding.shareWeChatBtn.setOnClickListener(v -> saveImageAndShare(1));
        binding.shareWeiboBtn.setOnClickListener(v -> saveImageAndShare(2));
        binding.shareQQBtn.setOnClickListener(v -> saveImageAndShare(4));
        binding.shareQZoneBtn.setOnClickListener(v -> saveImageAndShare(5));

//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//            }
//        });
    }

    private void setImage(boolean withPageShot){
        binding.shareImage.setVisibility(View.VISIBLE);
        if(withPageShot) {
            layoutBitmap = ScreenShotUtils.getViewBitmap(binding.shareLayout);
            binding.shareImage.setImageBitmap(layoutBitmap);
            binding.shareImage.getLayoutParams().width= DensityUtil.getScreenWidth(context)/2;
            binding.shareImage.getLayoutParams().height= DensityUtil.getScreenWidth(context)/2*layoutBitmap.getHeight()/layoutBitmap.getWidth();
        }else{
            if (layoutBitmap!=null) {
                binding.shareImage.getLayoutParams().width = DensityUtil.getScreenWidth(context) / 2;
                binding.shareImage.getLayoutParams().height = (DensityUtil.getScreenWidth(context) / 2) * layoutBitmap.getHeight() / bitmap.getWidth();
            }
        }

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        setTouchable(true);
        super.showAtLocation(parent, gravity, x, y);
//        WindowUtil.setBackgroundAlpha((Activity) parent.getContext(), 0.5f);
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
                        ShareUtils.shareToTimeLine(context,"这是我的名片",shareFile);
                        break;
                    case 1:
                        ShareUtils.shareToWxFriend(context,"这是我的名片",shareFile);
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

    public interface SimpleShareWindowListener {
        void onShare(int index);//先保存图片再跳转
    }

    public void setvInfo(VideoInfoBean vInfo) {
        this.vInfo = vInfo;
        binding.setData(vInfo);
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
