package com.codepig.customerview.windows;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cjt2325.cameralibrary.activity.CameraActivity;
import com.codepig.common.config.IntegerConfig;
import com.codepig.common.util.FileUtil;
import com.codepig.common.util.PermissionCompat;
import com.codepig.common.util.WindowUtil;
import com.codepig.customerview.R;
import com.codepig.customerview.databinding.WindowModifyAvatarBinding;
import com.codepig.customerview.callback.ModifyAvatarWindowCB;
import com.codepig.customerview.viewmodel.ModifyAvatarWindowVM;

import java.io.File;

import static java.lang.System.currentTimeMillis;

/**
 * Create by chenbin on 2018/12/9
 * <p>
 * <p>
 */
public class ModifyAvatarWindow extends PopupWindow implements ModifyAvatarWindowCB {
    private ModifyAvatarWindowVM vm;
    private Activity activity;
    private String image_path; // 拍照保存照片的地址
    public static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int GALLEY_RESULT = 123;
    private boolean forID;

    public ModifyAvatarWindow(final Activity activity) {
        this.activity = activity;
        vm = new ModifyAvatarWindowVM(this);
        WindowModifyAvatarBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.window_modify_avatar, null, false);
        binding.setVm(vm);
        setContentView(binding.getRoot());
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setOutsideTouchable(true);
        setOnDismissListener(() -> WindowUtil.setBackgroundAlpha(activity, 1f));
    }

    public ModifyAvatarWindow(final Activity activity,boolean forID) {
        this.activity = activity;
        vm = new ModifyAvatarWindowVM(this);
        WindowModifyAvatarBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.window_modify_avatar, null, false);
        binding.setVm(vm);
        setContentView(binding.getRoot());
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        this.forID=forID;

        setFocusable(true);
        setOutsideTouchable(true);
        setOnDismissListener(() -> WindowUtil.setBackgroundAlpha(activity, 1f));
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 设置PopupWindow是否能响应外部点击事件
        setOutsideTouchable(true);
        setTouchable(true);
        super.showAtLocation(parent, gravity, x, y);
        WindowUtil.setBackgroundAlpha((Activity) parent.getContext(), 0.5f);
    }

    @Override
    public void toTakePhoto() {
        if (!PermissionCompat.getInstance().checkGalleryPermission(activity)) {
            return;
        }
        image_path = FileUtil.getBaseImageDir() + currentTimeMillis() + ".jpg";
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//        if (Build.VERSION.SDK_INT >= 24) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, activity.getPackageName()+".FileProvider", new File(image_path)));
//        } else {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(image_path)));
//        }
//        activity.startActivityForResult(intent, IntegerConfig.REQUEST_CODE_CAMERA_IMAGE);

        Intent intent = new Intent(activity, CameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filePath", image_path);
        bundle.putString("videoPath", "");
        bundle.putBoolean("showHeadFrame",forID);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, IntegerConfig.REQUEST_CODE_CAMERA_IMAGE);
    }

    @Override
    public void toPhotoGallay() {
        if (!PermissionCompat.getInstance().checkGalleryPermission(activity)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= 24) {
            File imagePath = new File(activity.getFilesDir(), "mImages");
            File galleyFile = new File(imagePath, System.currentTimeMillis() + ".png");
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName()+".FileProvider", galleyFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        activity.startActivityForResult(intent, GALLEY_RESULT);
    }

    @Override
    public void toCancel() {
        dismiss();
    }

    public String getImage_path() {
        return image_path;
    }
}
