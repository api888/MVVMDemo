package com.codepig.customerview.windows;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.codepig.common.rxbus.RxBus;
import com.codepig.common.rxbus.RxEvent;
import com.codepig.common.util.DensityUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.WindowUploadBinding;
import com.everyone.net.task.VideoPostTask;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 上传进度窗口
 */
public class UploadWindow extends PopupWindow {
    private Context mContent;
    private WindowUploadBinding binding;
    private UploadWindowListener listener;

    private int timeCount=0;
    //计时处理
    private Handler timeHandle;
    //计时器
    private Timer presTimer=new Timer();
    private TimerTask presTask;

    public UploadWindow(final Context context, UploadWindowListener uploadWindowListener,boolean touchable) {
        mContent=context;
        this.listener = uploadWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_upload, null, false);
        setContentView(binding.getRoot());
        setWidth(MATCH_PARENT);
        setHeight(DensityUtil.dpToPx(context,150));//个别手机如三星必须设置大小且不能使用wrap_content属性

        setFocusable(false);
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(false);
        setTouchable(touchable);

        setListener();
    }

    private void setListener() {
        binding.closeBtn.setOnClickListener(v -> {
//            binding.loadCircle.clearAnimation();
            dismiss();
            RxBus.getInstance().send(RxEvent.MainEvent.UPLOAD_WINDOW_CLOSE, null);
        });
        binding.redoBtn.setOnClickListener(v -> {
//            binding.loadCircle.clearAnimation();
            VideoPostTask videoPostTask=new VideoPostTask(mContent);
            videoPostTask.execute();
            setState(0);
//            dismiss();
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(binding.getRoot(), gravity, x, y);
    }

    public interface UploadWindowListener {
        void close();
        void reload();
    }

    public void setCover(Bitmap _b){
        binding.smallCover.setImageBitmap(_b);
        binding.bigCover.setImageBitmap(_b);
    }

    /**
     * 设置进度
     * @param _p
     */
    public void setProgress(int _p){
        int pec=_p*100/8;
        binding.loadCircle.setCurrent(pec);
    }

    public void setState(int type){
        switch (type){
            case 0:
                binding.loadingArea.setVisibility(View.VISIBLE);
                binding.endArea.setVisibility(View.INVISIBLE);
                break;
            case 1:
                binding.codeText.setText("发布成功！");
                binding.loadingArea.setVisibility(View.INVISIBLE);
                binding.redoBtn.setVisibility(View.INVISIBLE);
                binding.endArea.setVisibility(View.VISIBLE);
                startPresTimer();
                break;
            case 2:
                binding.codeText.setText("啊喔， 发布失败！");
                binding.loadingArea.setVisibility(View.INVISIBLE);
                binding.endArea.setVisibility(View.VISIBLE);
                binding.redoBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 开始倒计时
     */
    private void startPresTimer(){
        timeCount=10;
        if(presTimer==null){
            presTimer=new Timer();
        }
        if(timeHandle==null) {
            timeHandle = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            timeCount--;
                            if(timeCount==0){
                                stopPresTimer();
                            }
                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }
        if(presTask==null){
            presTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    timeHandle.sendMessage(message);
                }
            };
        }
        presTimer.schedule(presTask, 1000, 1000);
    }

    /**
     * 结束倒计时
     */
    private void stopPresTimer(){
        this.dismiss();
        if(timeHandle!=null) {
            timeHandle.removeMessages(1);
            timeHandle=null;
        }
        if(presTask!=null){
            presTask.cancel();
            presTask=null;
        }
        if(presTimer!=null){
            presTimer.cancel();
            presTimer=null;
        }
    }
}
