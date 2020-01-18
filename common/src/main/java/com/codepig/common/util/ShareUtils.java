package com.codepig.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * 非第三方分享(无申请腾讯开发平台账号)
 */
public class ShareUtils {
    /**
     * 分享到QQ好友
     *
     */
    public static void shareToQQFriend(Context content, Bitmap bitmap) {
        if (PlatformUtil.isInstallApp(content,PlatformUtil.PACKAGE_MOBILE_QQ)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        content.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_MOBILE_QQ, PlatformUtil.ACTIVITY_SHARE_QQ);

                shareIntent.setComponent(componentName);
                // mContext.startActivity(shareIntent);
                content.startActivity(Intent.createChooser(shareIntent, "Share"));
            } catch (Exception e) {
//            ContextUtil.getInstance().showToastMsg("分享图片到**失败");
            }
        }
    }

    /**
     * 分享到微信朋友
     */
    public static void shareToWxFriend(Context context,String content ,File picFile){
        if (PlatformUtil.isInstallApp(context,PlatformUtil.PACKAGE_WECHAT)){
            Intent intent = new Intent();
            ComponentName cop = new ComponentName(PlatformUtil.PACKAGE_WECHAT,PlatformUtil.ACTIVITY_SHARE_WECHAT_IMAGEUI);
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            if (picFile != null) {
                if (picFile.isFile() && picFile.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, context.getPackageName()+".FileProvider", picFile);
                    } else {
                        uri = Uri.fromFile(picFile);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
//                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri);
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // context.startActivity(intent);
            context.startActivity(Intent.createChooser(intent, "Share"));
        }else{
            ToastUtil.showToast("您需要安装微信客户端");
        }
    }

    /**
     * 分享信息到朋友圈
     */
    public static void shareToTimeLine(Context context, String content, File picFile) {
        if (PlatformUtil.isInstallApp(context,PlatformUtil.PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName(PlatformUtil.PACKAGE_WECHAT, PlatformUtil.ACTIVITY_SHARE_WECHAT_TIMELINE);
            intent.setComponent(comp);
//            intent.setAction(Intent.ACTION_SEND_MULTIPLE);// 分享多张图片时使用
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            //添加Uri图片地址--用于添加多张图片
            //ArrayList<Uri> imageUris = new ArrayList<>();
            //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            if (picFile != null) {
                if (picFile.isFile() && picFile.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, context.getPackageName()+".FileProvider", picFile);
                    } else {
                        uri = Uri.fromFile(picFile);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
            }
            // 微信现不能进行标题同时分享
            // intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            ToastUtil.showToast("您需要安装微信客户端");
        }
    }

    /**
     * 分享信息到微博
     */
    public static void shareToWeibo(Context context,boolean isFriends, File file) {
            if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_SINA)) {
                if (!file.exists()) {
                    ToastUtil.showToast("文件不存在");
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                // 使用以下两种type有一定的区别，"text/plain"分享给指定的粉丝或好友 ；"image/*"分享到微博内容
                if (isFriends) {
                    intent.setType("text/plain");
                }else {
                    intent.setType("image/*");// 分享文本|文本+图片|图片 到微博内容时使用
                }
                PackageManager packageManager = context.getPackageManager();
                List<ResolveInfo> matchs = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                ResolveInfo resolveInfo = null;
                for (ResolveInfo each : matchs) {
                    String pkgName = each.activityInfo.applicationInfo.packageName;
                    if (PlatformUtil.PACKAGE_SINA.equals(pkgName)) {
                        resolveInfo = each;
                        break;
                    }
                }
                // type = "image/*"--->com.sina.weibo.composerinde.ComposerDispatchActivity
                // type = "text/plain"--->com.sina.weibo.weiyou.share.WeiyouShareDispatcher
                intent.setClassName(PlatformUtil.PACKAGE_SINA, resolveInfo.activityInfo.name);// 这里在使用resolveInfo的时候需要做判空处理防止crash
                intent.putExtra(Intent.EXTRA_TEXT, "Test Text String !!");
                if (file.isFile() && file.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, context.getPackageName()+".FileProvider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                context.startActivity(intent);
            }else{
                ToastUtil.showToast("您需要安装sina客户端");
            }
    }

    /**
     * 分享信息到QQ空间
     */
    public static void shareToQzone(Context context,File file) {
        try {
            if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_QZONE)) {
                if (!file.exists()) {
                    ToastUtil.showToast("文件不存在");
                    return;
                }

                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_QZONE, PlatformUtil.ACTIVITY_SHARE_QQ_ZONE);
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm so tired!!");
                if (file.isFile() && file.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, context.getPackageName()+".FileProvider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                context.startActivity(intent);
            } else {
                ToastUtil.showToast("您需要安装QQ空间客户端");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
