package com.codepig.customerview.windows;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.codepig.common.util.ToastUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.WindowPay4VideoBinding;
import com.everyone.net.bean.VideoOrderBean;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * EVO订单
 */
public class Pay4VideoWindow extends PopupWindow {
    private WindowPay4VideoBinding binding;
    private Pay4VideoWindowListener listener;
    private VideoOrderBean _info;
    private double myEvo;

    public Pay4VideoWindow(final Context context, Pay4VideoWindowListener pay4VideoWindowListener,String title) {
        this.listener = pay4VideoWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_pay_4_video, null, false);
        setContentView(binding.getRoot());
        setWidth(MATCH_PARENT);
        binding.titleText.setText(title);

//        setOnDismissListener(() -> WindowUtil.setBackgroundAlpha((Activity) context, 1f));
        setListener();
        setHeight(WRAP_CONTENT);
    }

    private void setListener() {
        binding.closeBtn.setOnClickListener(v -> dismiss());
        binding.enterBtn.setOnClickListener(v -> {
            if(_info.getAmount()>myEvo){
                ToastUtil.showToast("余额不足");
            }else{
                dismiss();
                listener.onPay(_info.getOrder_id());
            }
        });
        binding.helpBtn.setOnClickListener(v -> listener.showEvoHelp());
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        setTouchable(true);
        super.showAtLocation(parent, gravity, x, y);
//        WindowUtil.setBackgroundAlpha((Activity) parent.getContext(), 0.5f);
    }

    public interface Pay4VideoWindowListener {
        void onPay(int id);
        void showEvoHelp();
    }

    public VideoOrderBean getvInfo() {
        return _info;
    }

    public void setPayInfo(VideoOrderBean _info,double myEVO) {
        this._info = _info;
        myEvo=myEVO;
        binding.setData(_info);
        binding.MyEvoText.setText("EVO余额："+myEVO);
        binding.videoPriceText.setText("消耗EVO："+_info.getAmount()+"个");

        if(myEVO<_info.getAmount()){
            binding.helpBtn.setVisibility(View.VISIBLE);
            binding.noMoneyAlert.setVisibility(View.VISIBLE);
        }else{
            binding.helpBtn.setVisibility(View.INVISIBLE);
            binding.noMoneyAlert.setVisibility(View.INVISIBLE);
        }
    }
}
