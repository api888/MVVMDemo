package com.codepig.common.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.codepig.common.R;
import com.codepig.common.util.glide.RoundTransform;
import com.codepig.common.view.GlideImageView;

/**
 * Create by huscarter@163.com on 2017/11/20
 * <p>
 * glide data binding 适配器
 */
public class ImageViewBinding {
    private static final String TAG = ImageViewBinding.class.getSimpleName();
    public static final String centerCrop = "centerCrop";
    public static final String centerInside = "centerInside";
    public static final String fitCenter = "fitCenter";

    @BindingAdapter(value = {"src", "placeholderImage", "error", "roundAsCircle", "roundRadius", "scaleType"}, requireAll = false)
    public static void loadImage(final GlideImageView view, String url, Drawable holderDrawable,
                                 Drawable errorDrawable, boolean roundAsCircle, float radius, String scaleType) {
        try {


            RequestBuilder requestBuilder = Glide.with(view.getContext()).asDrawable().load(url);
            RequestOptions requestOptions = new RequestOptions()
                    .priority(Priority.HIGH);
            if (scaleType == null) {
                requestOptions.centerCrop();
            } else {
                switch (scaleType) {
                    case centerInside:
                        requestOptions.centerInside();
                        break;
                    case fitCenter:
                        requestOptions.fitCenter();
                        break;
                    default:
                        requestOptions.centerCrop();
                        break;
                }
            }
            errorDrawable = errorDrawable == null ? view.getContext().getResources().getDrawable(R.drawable.no_pic) : errorDrawable;
            if (roundAsCircle) {
                // To set the src circle
                requestOptions = requestOptions.circleCrop();
                // To set the placeHolderImage circle
                if (holderDrawable != null) {
                    RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), ((BitmapDrawable) holderDrawable).getBitmap());
                    bitmapDrawable.setCircular(true);
                    requestOptions = requestOptions.placeholder(bitmapDrawable);
                }
                // To set the errorImage circle
                if (errorDrawable != null) {
                    RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), ((BitmapDrawable) errorDrawable).getBitmap());
                    bitmapDrawable.setCircular(true);
                    requestOptions = requestOptions.error(bitmapDrawable);
                }
            } else {
                if (radius > 0) {
                    // To set the src round
                    requestOptions = requestOptions.transform(new RoundTransform(view.getContext(), radius));
                    // To set the placeHolderImage round
                    if (holderDrawable != null) {
                        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), ((BitmapDrawable) holderDrawable).getBitmap());
                        bitmapDrawable.setCornerRadius(radius);
                        requestOptions = requestOptions.placeholder(bitmapDrawable);
                    }
                    // To set the errorImage round
                    if (errorDrawable != null) {
                        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), ((BitmapDrawable) errorDrawable).getBitmap());
                        bitmapDrawable.setCornerRadius(radius);
                        requestOptions = requestOptions.error(bitmapDrawable);
                    }
                } else {
                    if (holderDrawable != null) {
                        requestOptions = requestOptions.placeholder(holderDrawable);
                    }
                    if (errorDrawable != null) {
                        requestOptions = requestOptions.error(errorDrawable);
                    }
                }
            }
            requestBuilder.apply(requestOptions).into(view);
        } catch (Exception e) {

        }
    }

    /**
     * 绑定本地资源图片用的,因为本地图片不存在加载失败的情况
     *
     * @param view
     * @param resId
     */
    @BindingAdapter(value = {"src", "scaleType"}, requireAll = false)
    public static void loadImage(GlideImageView view, int resId, String scaleType) {
        try {
            RequestBuilder requestBuilder = Glide.with(view.getContext()).asDrawable().load(resId);
            RequestOptions requestOptions = new RequestOptions()
                    .priority(Priority.HIGH);
            if (scaleType == null) {
                requestOptions.centerCrop();
            } else {
                switch (scaleType) {
                    case centerInside:
                        requestOptions.centerInside();
                        break;
                    case fitCenter:
                        requestOptions.fitCenter();
                        break;
                    default:
                        requestOptions.centerCrop();
                        break;
                }
            }
            requestBuilder.apply(requestOptions).into(view);
        } catch (Exception e) {

        }
    }

}
