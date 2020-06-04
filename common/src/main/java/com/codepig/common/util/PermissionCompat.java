package com.codepig.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.codepig.common.activity.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 权限处理类
 */
public class PermissionCompat {
    private final static String TAG = PermissionCompat.class.getSimpleName();
    private static PermissionCompat instance;

    //自己的项目包名
    private String packageName="com.everyone";

    /**
     * 检测文件的权限
     */
    private static String[] PERMISSIONS_EXTERNAL_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /**
     * 存储
     */
    public final static int REQUEST_EXTERNAL_STORAGE = 99;

    /**
     * 检测相机的
     */
    public static String[] PERMISSION_CAMERA = {
            Manifest.permission.CAMERA};

    /**
     * 相机请求
     */
    public final static int REQUEST_CAMERA = 98;//华为的requestCode不能大于128

    /**
     * 检测录音
     */
    public static String[] PERMISSION_RECODE = {
            Manifest.permission.RECORD_AUDIO};

    /**
     * 录音请求
     */
    public final static int REQUEST_RECODE = 96;

    /**
     * 检测图片选择的
     */
    public static String[] PERMISSION_GALLERY = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static String[] PERMISSION_FILE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /**
     * 相册选择
     */
    public final static int REQUEST_GALLERY = 97;

    /**
     * 检测拨打电话权限
     */
    private static String[] PERMISSION_CALL_PHONE = {
            Manifest.permission.CALL_PHONE,
    };

    /**
     * 拨打电话请求
     */
    public final static int REQUEST_CALL_PHONE = 10096;

    /**
     * 设备状态
     */
    public final static int REQUEST_PHONE_STATE = 100;

    private final static String[] PERMISSIONS_PHONE_STATE = {Manifest.permission.READ_PHONE_STATE};

    /**
     * 获取手机通讯录
     */
    private final static int REQUEST_READ_CONTACTS = 101;

    private final static String[] PERMISSIONS_READ_CONTACTS = {Manifest.permission.READ_CONTACTS};

    /**
     * 定位权限
     */
    private static String[] PERMISSION_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * 分享相关权限
     */
    String[] ABOUT_SHARE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private PermissionCompat() {
        //
    }

    public static PermissionCompat getInstance() {
        if (instance == null) {
            synchronized (PermissionCompat.class) {
                if (instance == null) {
                    instance = new PermissionCompat();
                }
            }
        }
        return instance;
    }

    public boolean checkCameraPermission(Activity activity) {
        return checkPermission(activity, PERMISSION_CAMERA, REQUEST_CAMERA);
    }

    public boolean checkRecodePermission(Activity activity) {
        return checkPermission(activity, PERMISSION_RECODE, REQUEST_RECODE);
    }

    /**
     * 检测文件读写
     */
    public boolean checkFilePermission(Activity activity) {
        return checkPermission(activity, PERMISSION_FILE, REQUEST_EXTERNAL_STORAGE);
    }

    /**
     * 检测相册
     */
    public boolean checkGalleryPermission(Activity activity) {
        return checkPermission(activity, PERMISSION_GALLERY, REQUEST_GALLERY);
    }

    /**
     * 检测电话
     */
    public boolean checkCallPhonePermission(Activity activity) {
        return checkPermission(activity, PERMISSION_CALL_PHONE, REQUEST_CALL_PHONE);
    }

    /**
     * 检测文件读写
     */
    public boolean checkStoragePermission(Activity activity) {
        return checkPermission(activity, PERMISSIONS_EXTERNAL_STORAGE, REQUEST_EXTERNAL_STORAGE);
    }

    /**
     * 检测手机状态权限
     * @param activity
     * @return
     */
    public boolean checkPhoneStatePermission(Activity activity) {
        return checkPermission(activity, PERMISSIONS_PHONE_STATE,REQUEST_PHONE_STATE);
    }

    /**
     * 检查手机通讯录权限
     * @param activity
     * @return
     */
    public boolean checkReadContactsPermission(Activity activity) {
        return checkPermission(activity, PERMISSIONS_READ_CONTACTS,REQUEST_READ_CONTACTS);
    }

    /**
     * 检测定位
     */
    public boolean checkLocationPermission(Activity activity) {
        return checkPermission(activity, PERMISSION_LOCATION, REQUEST_LOCATION);
    }

    /**
     * 定位权限
     */
    public final static int REQUEST_LOCATION = 10097;

    /**
     * 定位权限
     */
    public final static int REQUEST_SOCIAL_SHARE = 10098;

    /**
     * 分享相关权限
     * @param activity
     * @return
     */
    public boolean checkAboutSharePermission(Activity activity) {
        return checkPermission(activity, ABOUT_SHARE, REQUEST_SOCIAL_SHARE);
    }

    /**
     * @param activity
     * @param permission   需要检测的权限数组
     * @param request_code 请求的request code 自定义
     * @return
     */
    public boolean checkPermission(Activity activity, String[] permission, int request_code) {
        for (String str : permission) {
            int check = ContextCompat.checkSelfPermission(activity, str);
            boolean is_granted = (check == PackageManager.PERMISSION_GRANTED);
            if (!is_granted) { //
                BaseActivity baseActivity=(BaseActivity) activity;
                switch (request_code){
                    case REQUEST_RECODE:
                        baseActivity.showPermissionAlert("请在应用权限设置允许录音");
                        break;
                    case REQUEST_CAMERA:
                        baseActivity.showPermissionAlert("请在应用权限设置允许使用摄像头");
                        break;
                    case REQUEST_EXTERNAL_STORAGE:
                        baseActivity.showPermissionAlert("请在应用权限设置允许读写手机存储");
                        break;
                    case REQUEST_CALL_PHONE:
                        baseActivity.showPermissionAlert("请在应用权限设置允许拨打电话");
                        break;
                    case REQUEST_GALLERY:
                        baseActivity.showPermissionAlert("请在应用权限设置允许访问手机相册及摄像头");
                        break;
                }
                ActivityCompat.requestPermissions(
                        activity,
                        permission,
                        request_code
                );
                return false;
            }
        }
        return true;
    }


    /**
     * 跳转至权限设置页面
     * @param activity
     */
    public void goPermissionSet(Activity activity) {
        String name = Build.MANUFACTURER;
        switch (name) {
            case "HUAWEI":
                goHuaWeiManager(activity);
                break;
            case "vivo":
                goVivoManager(activity);
                break;
            case "OPPO":
                goOppoManager(activity);
                break;
            case "Coolpad":
                goCoolpadManager(activity);
                break;
            case "Meizu":
                goMeizuManager(activity);
                break;
            case "Xiaomi":
                goXiaoMiManager(activity);
                break;
            case "samsung":
                goSangXinManager(activity);
                break;
            case "Sony":
                goSonyManager(activity);
                break;
            case "LG":
                goLGManager(activity);
                break;
            default:
                goIntentSetting(activity);
                break;
        }
    }

    private void goLGManager(Activity activity){
        try {
            Intent intent = new Intent(packageName);
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showToast("跳转失败");
            e.printStackTrace();
            goIntentSetting(activity);
        }
    }
    private void goSonyManager(Activity activity){
        try {
            Intent intent = new Intent(packageName);
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showToast("跳转失败");
            e.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private void goHuaWeiManager(Activity activity) {
        try {
            Intent intent = new Intent(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showToast("跳转失败");
            e.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private static String getMiuiVersion(Activity activity) {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    private void goXiaoMiManager(Activity activity) {
        String rom = getMiuiVersion(activity);
        Intent intent=new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else {
            goIntentSetting(activity);
        }
    }

    private void goMeizuManager(Activity activity) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", packageName);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting(activity);
        }
    }

    private void goSangXinManager(Activity activity) {
        //三星4.3可以直接跳转
        goIntentSetting(activity);
    }

    private void goIntentSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goOppoManager(Activity activity) {
        doStartApplicationWithPackageName("com.coloros.safecenter",activity);
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     */
    private void goCoolpadManager(Activity activity) {
        doStartApplicationWithPackageName("com.yulong.android.security:remote",activity);
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private void goVivoManager(Activity activity) {
        doStartApplicationWithPackageName("com.bairenkeji.icaller",activity);
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    private void doStartApplicationWithPackageName(String packagename,Activity activity) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = activity.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = activity.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        System.out.println("resolve info List" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            System.out.println("PermissionPageManager"+resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityName]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                goIntentSetting(activity);
                e.printStackTrace();
            }
        }
    }

}
