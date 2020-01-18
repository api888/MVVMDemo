package com.codepig.common.net;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.codepig.common.rxbus.RxBus;
import com.codepig.common.rxbus.RxEvent;
import com.codepig.common.util.FileUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * 下载文件(使用系统downloadManager)
 */
public class DownloadFile {
    private static Context _context;
    //获取SDCard根目录
    private static String sdcardRoot;
    //要保存的目录
    public static String filepath="/com.everyone/download/";
    private static Boolean hadSdcard=false;
    //最大下载任务数
    private static int maxFile=1;
    //当前下载任务总数
    private static int downLoadTotal=0;
    //下载管理类
    private static DownloadManager mDownMag=null;
    //视频下载进行列表
    private static JSONObject[] DownList;
    //列表序号
    private static int currentFile=0;

    //初始化,获得系统下载管理
    public static void init(DownloadManager sysmag, Context _c){
        _context=_c;
        mDownMag=sysmag;
        DownList=new JSONObject[maxFile];
    }

    public static boolean isReady(){
        return mDownMag != null;
    }

    //判断sd卡是否存在
    public static boolean ExistSDCard() {
        String _path="";
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            hadSdcard=true;
            sdcardRoot= android.os.Environment.getExternalStorageDirectory().toString();
            _path=sdcardRoot+filepath;
            createDir(_path);
        } else{
            hadSdcard=false;
            //data目录通常不允许访问
//            phoneRoot=Environment.getDataDirectory().toString();
//            _path=phoneRoot+filepath;
        }
        return hadSdcard;
    }

    //创建文件目录
    private static boolean createDir(String _path){
        try{
            File file = new File(_path);
            if (!file.exists())
            {
                if (file.mkdirs())
                {
                    return true;
                }
            }else{
                return true;
            }
        }catch (Exception e){
        }
        return false;
    }

    //sd卡剩余大小
    public static long getSDFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }

    //下载
    public static String downloadFile(String _downloadPath, String _fName){
        if(downLoadTotal<maxFile){
            try{
                String _url=_downloadPath;
                String _name=_fName;
                DownList[currentFile]=new JSONObject();
                DownList[currentFile].put("id", _name + currentFile);
                //下载预览图
                saveToSDCard(_url, _name);
            }catch (Exception e){
                return "下载失败";
            }
            return "开始下载文件";
        }else{
            return "下载进程已满";
        }
    }

    //存到sd卡(DownloadManager方式，某些网络环境下无法下载，原因不明)
    private static void saveToSDCard(String _url, String _filename){
//        Log.d("LOGCAT","startLoad:"+_filename+"\n"+_url);
        final String filename=_filename;
        final String vurl=_url;
        Runnable downLoadRun= () -> {
            try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(vurl));
                request.setDestinationInExternalPublicDir(filepath, filename);
                request.setTitle("大家链安装包:" + filename);
                request.setVisibleInDownloadsUi(true);  //设置显示下载界面
                long downloadId = mDownMag.enqueue(request);
                Log.d("LOGCAT", "reqId:" + downloadId + ":" + currentFile);
                DownList[currentFile].put("Downid", downloadId);
                if (currentFile < maxFile - 1) {
                    currentFile++;
                } else {
                    currentFile = 0;
                }
                downLoadTotal++;
                //添加监听器
                final long _downloadId=downloadId;
                IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        Log.d("LOGCAT","download id:"+ID);
                        if (ID == _downloadId) {
                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(ID);
                            Cursor c = mDownMag.query(query);
                            if(c.moveToFirst()) {
                                //获取文件下载路径
//                                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                RxBus.getInstance().send(RxEvent.MainEvent.NEW_APK_DOWNLOADED, null);
                            }
                        }
                    }
                };
                _context.registerReceiver(broadcastReceiver, intentFilter);
            }catch (Exception e){
                Log.d("LOGCAT",e.toString());
            }
        };
        ThreadPoolUtils.execute(downLoadRun);
    }

    //删除SD卡上的文件
    public static boolean delSDFile(String fileName) {
        File file = new File(sdcardRoot + filepath + fileName);
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        file.delete();
        return true;
    }

    //下载完成
    public  static void downloadComplete(long _id){
        for (int i=0;i<DownList.length;i++){
            try {
                if (DownList[i].getLong("Downid")==_id) {
                    break;
                }
            }catch (Exception e){
            }
        }
    }

    //取消下载
    public static void removeDownload(String _vid){
        for (int i=0;i<DownList.length;i++){
            try {
                if (DownList[i].getString("id").equals(_vid)) {
                    if(mDownMag.remove(DownList[i].getLong("Downid"))>0){
                        downLoadTotal--;
                    }
                    break;
                }
            }catch (Exception e){
            }
        }
    }

    //获得sd卡地址
    public static String getSdPath(){
        return sdcardRoot;
    }

    //获得文件下载地址
    public static String getDownloadPath(){
        return sdcardRoot+filepath;
    }

    //获得当前是否有文件在下载
    public static Boolean isDownLoading(){
        Boolean ifLoading=false;
        if(downLoadTotal>0){
            ifLoading=true;
        }
        return ifLoading;
    }

    //取消当前全部下载
    public static void clearAllDown(){
        for(int i=0;i<DownList.length;i++){
            try{
                mDownMag.remove(DownList[i].getLong("Downid"));
            }catch (Exception e){}
        }
    }

    //查询下载id
    public static long getDownloadId(String _vid){
        for (int i=0;i<DownList.length;i++){
            try {
                if (DownList[i].getString("id").equals(_vid)) {
                    return DownList[i].getLong("Downid");
                }
            }catch (Exception e){
            }
        }
        return -1;
    }
}