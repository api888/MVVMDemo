package com.codepig.common.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.codepig.common.bean.MediaInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MediaUtil {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static File file;

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(Context context, int type) {
        Uri uri = null;
        //适配Android N
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
        } else {
            return Uri.fromFile(getOutputMediaFile(type));
        }
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "image");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        file = mediaFile;
        return mediaFile;
    }

    public static String getImageForVideo(String videoPath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = videoPath;
        if (path.startsWith("http"))
            //获取网络视频第一帧图片
            mmr.setDataSource(path, new HashMap());
        else
            //本地视频
            mmr.setDataSource(path);
        Bitmap bitmap = mmr.getFrameAtTime(0);
        //保存图片
        File f = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmr.release();
        return f.getAbsolutePath();
    }

    /**
     * 获取第一帧截图
     * @param videoPath
     * @return
     */
    public static Bitmap getBitmapForVideo(String videoPath) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            String path = videoPath;
            if (path.startsWith("http"))
                //获取网络视频第一帧图片
                mmr.setDataSource(path, new HashMap());
            else
                //本地视频
                mmr.setDataSource(path);
            Bitmap bitmap = mmr.getFrameAtTime(0);//截图位置的微秒数
            return bitmap;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取分辨率
     */
    public static int[] getVideoSize(String videoPath){
        int[] vSize=new int[2];
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            if (videoPath != null)
            {
                if (videoPath.startsWith("http"))
                    //网络视频
                    mmr.setDataSource(videoPath, new HashMap());
                else
                    //本地视频
                    mmr.setDataSource(videoPath);
            }
//            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
            vSize[0] = Integer.parseInt(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));//宽
            vSize[1] = Integer.parseInt(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));//高
        } catch (Exception ex)
        {
            return null;
        } finally {
            mmr.release();
        }

        return vSize;
    }

    /**
     * 获取码率
     * @param videoPath
     * @return
     */
    public static String getBitrate(String videoPath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            String path = videoPath;
            if (path.startsWith("http"))
                //获取网络视频第一帧图片
                mmr.setDataSource(path, new HashMap());
            else
                //本地视频
                mmr.setDataSource(path);
            return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        }catch (Exception e){
            return null;
        } finally {
            mmr.release();
        }
    }

    public static Bitmap getBitmapForVideo(String videoPath,long _t) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            String path = videoPath;
            if (path.startsWith("http"))
                //获取网络视频第一帧图片
                mmr.setDataSource(path, new HashMap());
            else
                //本地视频
                mmr.setDataSource(path);
            Bitmap bitmap = mmr.getFrameAtTime(_t);//截图位置的微秒数
            return bitmap;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取视频的第一帧图片
     */
    public static void getImageForVideo(String videoPath, OnLoadVideoImageListener listener) {
        LoadVideoImageTask task = new LoadVideoImageTask(listener);
        task.execute(videoPath);
    }

    public static class LoadVideoImageTask extends AsyncTask<String, Integer, File> {
        private OnLoadVideoImageListener listener;

        public LoadVideoImageTask(OnLoadVideoImageListener listener) {
            this.listener = listener;
        }

        @Override
        protected File doInBackground(String... params) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            String path = params[0];
            if (path.startsWith("http"))
                //获取网络视频第一帧图片
                mmr.setDataSource(path, new HashMap());
            else
                //本地视频
                mmr.setDataSource(path);
            Bitmap bitmap = mmr.getFrameAtTime(0);
            //保存图片
            File f = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (f.exists()) {
                f.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmr.release();
            return f;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (listener != null) {
                listener.onLoadImage(file);
            }
        }
    }

    public interface OnLoadVideoImageListener {
        void onLoadImage(File file);
    }

    /**
     * 获取视频信息
     * @param mUri
     */
    public static MediaInfo getMp4Info(String mUri){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(mUri);

            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高

            MediaInfo mediaInfo=new MediaInfo();
            mediaInfo.setDuration(duration);
            mediaInfo.setHeight(height);
            mediaInfo.setWidth(width);
            return mediaInfo;
        } catch (Exception ex) {
            return null;
        } finally {
            mmr.release();
        }
    }

    /**
     * 保存图片到相册
     */
    public static File saveImageToGallery(Context context,Bitmap bitmap){
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.everyone.share";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return file;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
