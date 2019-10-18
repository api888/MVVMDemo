package com.codepig.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.codepig.customerview.R;

/**
 * 顶部搜索输入框
 */
public class SearchEditText extends AppCompatEditText {
    private static final String TAG = SearchEditText.class.getSimpleName();

    private Drawable drawable_left; // 搜索图标
    private Drawable drawable_del; // 搜索删除按钮图标
    private Rect rect_del; // 控件区域
    private final static int FINGER = 10; // 手指触摸范围扩充

    private int padding_move = 0;// 为了居中显示所设置的padding
    private float hint_width = 0;
    private int drawable_width = 0;
    private int padding_old = 0;// 记录原来的padding
    private boolean is_open_keyboard = false;
    /**
     * 是否是可编辑状态
     */
    private boolean editable = true;
    /**
     * 输入款组件OnTouch事件回调
     */
    private OnTouchCallback on_touch_callback;
    /**
     * 初始是否需要将提示信息剧中
     */
    private boolean centerStatus = true;

    public SearchEditText(Context context) {
        this(context, null);
        initParams(context, null);
        setListener();
    }


    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        initParams(context, attrs);
        setListener();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
        setListener();
    }

    /**
     * 初始化数值
     *
     * @param context
     * @param attrs
     */
    private void initParams(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText);
            editable = typedArray.getBoolean(R.styleable.SearchEditText_editable, true);
            drawable_del = typedArray.getDrawable(R.styleable.SearchEditText_drawable_del);
            drawable_left = getCompoundDrawables()[0];
            padding_old = getCompoundDrawablePadding();

            typedArray.recycle();
        }
        if (!editable) setKeyListener(null);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                if (length() < 1) {
                    setCompoundDrawablesWithIntrinsicBounds(drawable_left, null, null, null);
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(drawable_left, null, drawable_del, null);
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (getPaddingLeft() != padding_old && editable) {
                    setEdit();
                }
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //
            }

        });
        // 监听文本变化，决定是否展示删除图标
//        setOnKeyListener(this);// 监听键盘操作，决定是否按了返回键，如果是则需要居中显示组建

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (padding_move == 0 && getWidth() > 0) {
                    hint_width = getPaint().measureText(getHint() + "");
                    drawable_width = drawable_left == null ? 0 : drawable_left.getIntrinsicWidth();
                    padding_move = (int) (getWidth() - hint_width - drawable_width - padding_old) / 2;

                    if (!editable || centerStatus) {
                        // 如果不可编辑或则被设置了centerStatus=true，提示剧中
                    } else { // 处于编辑状态
                        setEdit();
                    }
                }
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!editable && on_touch_callback == null) return false;
                    // 被点击时，恢复默认样式
                    if (editable || !TextUtils.isEmpty(getText()))
                        setEdit();
                    // 清空edit内容
                    if (drawable_del != null) {
                        rect_del = drawable_del.getBounds();
                        final int x = (int) event.getX(); // 相对事件view的x坐标
                        final int y = (int) event.getY(); // 相对事件view的y坐标
                        /**
                         * dRight:点击x所能达到的最小值。
                         * 因为getX是相对EditText组件的，所以通过（组件的宽度-rightPadding-dRight宽度-手指扩充范围）可得到有效的触摸的最小值。
                         * 注意：不能采用getRawX()和getLoactionInScreen()方法计算差值，因为getLoactionInScreen()会算入手机顶部状态栏和底部菜单栏的高度，
                         * 而不同厂家的手机底部菜单栏样式不一，有些可能是可隐藏的（比如华为），这样会导致计算不准确。
                         */
                        int vxl = getWidth() - rect_del.width() - getPaddingRight() - FINGER;
                        /**
                         * dRight 点击x所能达到的最大值
                         */
                        int vxr = getWidth() - getPaddingRight() + FINGER;
                        int vht = getHeight() - getPaddingTop();
                        int vhb = getHeight() - getHeight() - getPaddingTop();
                        //check to make btn_my_order_float the touch event was within the bounds of the drawable
                        //JLog.i(TAG, "x:" + x + ",vxl:" + vxl + ",vxr:" + vxr + ",y:" + y + ",vhb:" + vhb + ",vht:" + vht);
                        if ((x >= vxl && x <= vxr) && (y >= vhb && y <= vht)) {
                            setText("");
                            if (on_touch_callback != null) {
                                on_touch_callback.clearEditText();
                            }
                            event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
                        }
                    }

                    v.requestFocus();
                    openKeyboard(v);
                }
                if (on_touch_callback != null) {
                    on_touch_callback.onTouchCallback(v, event);
                }
                return false;
            }
        });
    }


    public void setDrawableDel(Drawable drawable) {
        this.drawable_del = drawable;
    }

    /**
     * 将组建恢复初始值状态,使drawableLeft和hint显示在中间
     */
    public void reset() {

        setText("");

        setPadding(padding_move, 0, padding_old, 0);
    }

    /**
     * 使组建处于编辑状态
     */
    public void setEdit() {
        setPadding(padding_old, 0, padding_old, 0);
        if (TextUtils.isEmpty(getText().toString())) {
            setCompoundDrawablesWithIntrinsicBounds(drawable_left, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(drawable_left, null, drawable_del, null);
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * 打开软键盘
     *
     * @param v
     */
    public void openKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            is_open_keyboard = true;
        }
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            is_open_keyboard = false;
        }
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        drawable_left = left;
    }

    public void setOnTouchCallback(OnTouchCallback callback) {
        this.on_touch_callback = callback;
    }

    public interface OnTouchCallback {
        void onTouchCallback(View v, MotionEvent event);

        void clearEditText();
    }

    // The getters and setters
    public boolean isCenterStatus() {
        return centerStatus;
    }

    public void setCenterStatus(boolean centerStatus) {
        this.centerStatus = centerStatus;
        setEdit();
    }
}
