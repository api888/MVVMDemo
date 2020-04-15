package com.codepig.customerview.windows;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.codepig.customerview.R;
import com.codepig.customerview.databinding.WindowInputBinding;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 输入框PopupWindow
 */
public class InputWindow extends PopupWindow {
    private Context mContent;
    private WindowInputBinding binding;
    private InputWindowListener listener;

    public InputWindow(final Context context, InputWindowListener inputWindowListener,int inputHeight) {
        this.listener = inputWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_input, null, false);
        setContentView(binding.getRoot());
        setWidth(MATCH_PARENT);
        setHeight(WRAP_CONTENT);
        setListener();
    }

    private void setListener() {
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        setTouchable(true);
        super.showAtLocation(parent, gravity, x, y);
//        WindowUtil.setBackgroundAlpha((Activity) parent.getContext(), 0.5f);
    }

    public interface InputWindowListener {
        void SetEvo(String num);
    }
}
