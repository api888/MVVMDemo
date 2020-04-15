package com.codepig.common.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codepig.common.R;

/**
 * 仅两点循环动画效果的加载提示框
 */
public class PointOnlyLoadingDialog extends Dialog {

    public PointOnlyLoadingDialog(@NonNull Context context) {
        super(context);
    }

    public PointOnlyLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PointOnlyLoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //2,创建静态内部类Builder，将dialog的部分属性封装进该类
    public static class Builder{

        private Context context;
        //是否按返回键取消
        private boolean isCancelable=true;
        //是否取消
        private boolean isCancelOutside=false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置是否可以按返回键取消
         * @param isCancelable
         * @return
         */
        public Builder setCancelable(boolean isCancelable){
            this.isCancelable=isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside){
            this.isCancelOutside=isCancelOutside;
            return this;
        }

        //创建Dialog
        public PointOnlyLoadingDialog create(){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.dialog_loading_point_only,null);//加载两点动画布局
            //设置带自定义主题的dialog
            PointOnlyLoadingDialog loadingDialog=new PointOnlyLoadingDialog(context,R.style.MyDialogStyle);
            loadingDialog.setContentView(view);
            loadingDialog.setCancelable(isCancelable);
            loadingDialog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDialog;
        }
    }

}
