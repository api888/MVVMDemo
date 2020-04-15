package com.codepig.customerview.windows;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.codepig.common.util.ToastUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.adapter.EvoSetMenuAdapter;
import com.codepig.customerview.databinding.WindowSetEvoBinding;
import com.codepig.customerview.listener.OnMenuClickListener;
import com.everyone.net.bean.MenuBean;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 设置私聊EVO
 */
public class SetEvoWindow extends PopupWindow implements OnMenuClickListener {
    private Context mContent;
    private WindowSetEvoBinding binding;
    private SetEvoWindowListener listener;
    private List<MenuBean> evoNums=new ArrayList<>();
    private String[] menuNames={"0","1","2","3","5","8","10","20"};

    private EvoSetMenuAdapter evoSetMenuAdapter;
    private double myEvo;

    private int position=0;

    public SetEvoWindow(final Context context, SetEvoWindowListener setEvoWindowListener) {
        this.listener = setEvoWindowListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.window_set_evo, null, false);
        setContentView(binding.getRoot());
        setWidth(MATCH_PARENT);

        binding.crv.setLayoutManager(new GridLayoutManager(mContent,4));
        evoSetMenuAdapter=new EvoSetMenuAdapter(new ArrayList<>(),this);
        binding.crv.setAdapter(evoSetMenuAdapter);

        setMenuList();
//        setOnDismissListener(() -> WindowUtil.setBackgroundAlpha((Activity) context, 1f));
        setListener();
        setHeight(WRAP_CONTENT);
    }

    private void setListener() {
        binding.closeBtn.setOnClickListener(v -> dismiss());
        binding.enterBtn.setOnClickListener(v -> {
            dismiss();
            listener.SetEvo(evoNums.get(position).getName());
        });
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        setTouchable(true);
        super.showAtLocation(parent, gravity, x, y);
//        WindowUtil.setBackgroundAlpha((Activity) parent.getContext(), 0.5f);
    }

    public interface SetEvoWindowListener {
        void SetEvo(String num);
    }

    public void setMenuList() {
        for(int i=0;i<menuNames.length;i++){
            MenuBean menuBean=new MenuBean();
            menuBean.setName(menuNames[i]);
            evoNums.add(menuBean);
        }
        evoSetMenuAdapter.setNewData(evoNums);
        evoSetMenuAdapter.setSelectedPosition(0);
        evoSetMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void menuBtnClick(int position){
        evoSetMenuAdapter.setSelectedPosition(position);
        this.position=position;
        evoSetMenuAdapter.notifyDataSetChanged();
    }
}
