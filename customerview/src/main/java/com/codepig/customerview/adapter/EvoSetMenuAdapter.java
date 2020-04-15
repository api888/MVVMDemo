package com.codepig.customerview.adapter;

import android.support.annotation.Nullable;

import com.codepig.common.databindingbase.BaseBindingViewHolder;
import com.codepig.common.databindingbase.BaseDataBindingAdapter;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.ItemEvoSetMenuBinding;
import com.codepig.customerview.listener.OnMenuClickListener;
import com.everyone.net.bean.MenuBean;

import java.util.List;

/**
 * EVO设置菜单项adapter
 */
public class EvoSetMenuAdapter extends BaseDataBindingAdapter<MenuBean, ItemEvoSetMenuBinding> {

    private OnMenuClickListener listener;
    private int selectedPosition=0;

    public EvoSetMenuAdapter(@Nullable List<MenuBean> data, OnMenuClickListener listener) {
        super(R.layout.item_evo_set_menu, data);
        this.listener=listener;
    }

    @Override
    protected void convert2(BaseBindingViewHolder<ItemEvoSetMenuBinding> helper, final MenuBean item, int position) {
        ItemEvoSetMenuBinding binding = helper.getBinding();
        if(item.getName().equals("0")) {
            binding.nameText.setText("免费");
        }else{
            binding.nameText.setText(item.getName()+" EVO");
        }
        if(position==selectedPosition){
            binding.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.evo_menu_selected));
        }else{
            binding.rootLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_box_with_black_stroke));
        }
//        binding.setData(item);

//        setListener
        binding.getRoot().setOnClickListener(v -> listener.menuBtnClick(position));
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
