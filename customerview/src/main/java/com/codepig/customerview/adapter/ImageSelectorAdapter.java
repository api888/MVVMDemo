package com.codepig.customerview.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepig.common.bean.SelectImageBean;
import com.codepig.common.databindingbase.BaseBindingViewHolder;
import com.codepig.common.databindingbase.BaseDataBindingAdapter;
import com.codepig.common.util.glide.GlideUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.ItemImageSelectorBinding;
import com.codepig.customerview.listener.OnImageSelectorListener;

import java.util.List;

/**
 * 图片选择adapter
 */
public class ImageSelectorAdapter extends BaseDataBindingAdapter<SelectImageBean, ItemImageSelectorBinding> {
    private OnImageSelectorListener listener;

    public ImageSelectorAdapter(@Nullable List<SelectImageBean> data, OnImageSelectorListener listener) {
        super(R.layout.item_image_selector, data);
        this.listener=listener;
    }

    @Override
    protected void convert2(BaseBindingViewHolder<ItemImageSelectorBinding> helper, final SelectImageBean item, int position) {
        ItemImageSelectorBinding binding = helper.getBinding();
//        binding.setData(item);
        if(TextUtils.isEmpty(item.getImagePath())){
            binding.ivImage.setVisibility(View.GONE);
            binding.btnRemove.setVisibility(View.GONE);
        }else {
            binding.ivImage.setVisibility(View.VISIBLE);
            GlideUtil.load(binding.ivImage, item.getImagePath());
            binding.btnRemove.setVisibility(View.VISIBLE);
        }

//        setListener
//        binding.getRoot().setOnClickListener(v -> listener.menuBtnClick(position+""));
        binding.btnRemove.setOnClickListener(v -> {
            listener.deleteImage(position);
            binding.ivImage.setVisibility(View.GONE);
        });
        binding.addImageBtn.setOnClickListener(v -> listener.chooseImage(position));
    }
}
