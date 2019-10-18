package com.codepig.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.codepig.common.R;

public class TagView extends android.support.v7.widget.AppCompatTextView {
    private boolean isSelected;
    private int unSelectedTextColor;
    private int selectedTextColor;
    private Drawable selectedBackground;
    private Drawable unSelectedBackground;
    private TagClickListener tagClickListener;
    private int mode = 0;
    public static final int MULTIYMODE = 0;
    public static final int SINGLEMODE = 1;

    private String tagId;

    public TagView(Context context) {
        super(context);
        initView(null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        initView(typedArray);
    }

    private void initView(TypedArray typedArray) {
        if (typedArray == null) {
            selectedBackground = typedArray.getDrawable(R.styleable.TagView_selected_background);
            unSelectedBackground = typedArray.getDrawable(R.styleable.TagView_unselected_background);
            selectedTextColor = typedArray.getColor(R.styleable.TagView_selected_textcolor, getResources().getColor(R.color.text_black));
            unSelectedTextColor = typedArray.getColor(R.styleable.TagView_unselected_textcolor, getResources().getColor(R.color.text_black));
        } else {
            selectedBackground = getResources().getDrawable(R.drawable.round_button_with_stroke);
            unSelectedBackground = getResources().getDrawable(R.drawable.round_shape_gray);
            selectedTextColor = getResources().getColor(R.color.text_black);
            unSelectedTextColor = getResources().getColor(R.color.text_black);
        }
        setTextColor(unSelectedTextColor);
        setBackgroundDrawable(unSelectedBackground);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == MULTIYMODE) {
                    if (isSelected) {
                        setTextColor(unSelectedTextColor);
                        setBackgroundDrawable(unSelectedBackground);
                    } else {
                        setTextColor(selectedTextColor);
                        setBackgroundDrawable(selectedBackground);
                    }
                    isSelected = !isSelected;
                } else if (mode == SINGLEMODE) {
                    if (!isSelected) {
                        setTextColor(selectedTextColor);
                        setBackgroundDrawable(selectedBackground);
                    }
                    isSelected = true;
                }
                if (tagClickListener != null) {
                    tagClickListener.onTagClick();
                }
            }
        });
    }

    private void refreshTagState() {
        if (isSelected) {
            setTextColor(selectedTextColor);
            setBackgroundDrawable(selectedBackground);
        } else {
            setTextColor(unSelectedTextColor);
            setBackgroundDrawable(unSelectedBackground);
        }
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        refreshTagState();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public TagClickListener getTagClickListener() {
        return tagClickListener;
    }

    public void setTagClickListener(TagClickListener tagClickListener) {
        this.tagClickListener = tagClickListener;
    }

    public interface TagClickListener {
        void onTagClick();
    }
}
