package com.codepig.customerview.adapter;

import android.support.annotation.Nullable;

import com.codepig.common.databindingbase.BaseBindingViewHolder;
import com.codepig.common.databindingbase.BaseDataBindingAdapter;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.ItemVideoCateBinding;
import com.codepig.customerview.listener.OnMenuClickListener;
import com.everyone.net.bean.VideoTagBean;

import java.util.List;

/**
 * 分类菜单项adapter
 */
public class VideoCateAdapter extends BaseDataBindingAdapter<VideoTagBean, ItemVideoCateBinding> {

    private OnMenuClickListener listener;
    private int selectedPosition=0;

    public VideoCateAdapter(@Nullable List<VideoTagBean> data, OnMenuClickListener listener) {
        super(R.layout.item_video_cate, data);
        this.listener=listener;
    }

    @Override
    protected void convert2(BaseBindingViewHolder<ItemVideoCateBinding> helper, final VideoTagBean item, int position) {
        ItemVideoCateBinding binding = helper.getBinding();
        binding.nameText.setText(item.getTag_name());
        if(position==selectedPosition){
            binding.nameText.setTextColor(mContext.getResources().getColor(R.color.white));
            binding.nameText.setBackgroundColor(mContext.getResources().getColor(R.color.video_cate_selected));
            binding.rootLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_box_with_green_stroke));
        }else{
            binding.nameText.setTextColor(mContext.getResources().getColor(R.color.text_dark));
            binding.nameText.setBackgroundColor(mContext.getResources().getColor(R.color.video_cate_unselected));
            binding.rootLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_box_with_gray_light_stroke));
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
