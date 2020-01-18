package com.codepig.customerview.adapter;

import android.support.annotation.Nullable;

import com.codepig.common.databindingbase.BaseBindingViewHolder;
import com.codepig.common.databindingbase.BaseDataBindingAdapter;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.ItemMenuCateBinding;
import com.codepig.customerview.listener.OnMenuClickListener;
import com.everyone.net.bean.MenuBean;

import java.util.List;

/**
 * 分类菜单项adapter
 */
public class MenuCateAdapter extends BaseDataBindingAdapter<MenuBean, ItemMenuCateBinding> {

    private OnMenuClickListener listener;
    private int selectedPosition=0;

    public MenuCateAdapter(@Nullable List<MenuBean> data, OnMenuClickListener listener) {
        super(R.layout.item_menu_cate, data);
        this.listener=listener;
    }

    @Override
    protected void convert2(BaseBindingViewHolder<ItemMenuCateBinding> helper, final MenuBean item, int position) {
        ItemMenuCateBinding binding = helper.getBinding();
        binding.nameText.setText(item.getName());
        if(position==selectedPosition){
            binding.nameText.setTextColor(mContext.getResources().getColor(R.color.white));
            binding.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.menu_selected));
        }else{
            binding.nameText.setTextColor(mContext.getResources().getColor(R.color.text_dark));
            binding.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.menu_unselected));
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
