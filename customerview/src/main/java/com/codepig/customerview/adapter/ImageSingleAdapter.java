package com.codepig.customerview.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.codepig.common.databindingbase.BaseBindingViewHolder;
import com.codepig.common.databindingbase.BaseDataBindingAdapter;
import com.codepig.common.util.DensityUtil;
import com.codepig.common.util.glide.GlideUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.ItemImageSingleBinding;
import com.codepig.customerview.listener.OnImageSelectorListener;

import java.util.List;

/**
 * 图片adapter
 */
public class ImageSingleAdapter extends BaseDataBindingAdapter<String, ItemImageSingleBinding> {
    private OnImageSelectorListener listener;

    public ImageSingleAdapter(@Nullable List<String> data, OnImageSelectorListener listener) {
        super(R.layout.item_image_single, data);
        this.listener=listener;
    }

    @Override
    protected void convert2(BaseBindingViewHolder<ItemImageSingleBinding> helper, final String item, int position) {
        ItemImageSingleBinding binding = helper.getBinding();
//        int imageWidth = DensityUtil.dpToPx(mContext,105);
//        binding.bodyLayout.getLayoutParams().width=imageWidth;
//        binding.bodyLayout.getLayoutParams().height=imageWidth;
//        binding.setData(item);
        if(!TextUtils.isEmpty(item)){
            GlideUtil.load(binding.ivImage, item);
        }

    }
}
