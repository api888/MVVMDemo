package com.codepig.common.util;

import android.content.Context;
import android.os.AsyncTask;

import com.codepig.common.listener.CompressorListener;
import com.hw.videoprocessor.VideoProcessor;

import java.io.File;

/**
 * 视频压缩线程
 */
public class VideoCompressAsyncTask2 extends AsyncTask<String, String, String> {
    Context mContext;
    private CompressorListener listener;
    private int type;

    public static final int PREV=0;
    public static final int REAL=1;

    public VideoCompressAsyncTask2(Context context, CompressorListener listener, int type) {
        mContext = context;
        this.listener=listener;
        this.type=type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... paths) {
        String filePath = null;
        String outPath;
        if(type== PREV){
            outPath=paths[1] + "/" + "compressPrev.mp4";
        }else{
            outPath=paths[1] + "/" + "compressReal.mp4";
        }
        File f = new File(outPath);//压缩文件地址
        if(f.exists()){
            f.delete();
        }
        //码率比预设小的不压缩
//        System.out.println("MediaUtil.getBitrate(paths[0]):"+MediaUtil.getBitrate(paths[0]));
        if(Long.parseLong(MediaUtil.getBitrate(paths[0]))>6400000) {
            try {
                VideoProcessor.processor(mContext)
                        .input(paths[0])
                        .output(outPath)
                        //以下参数全部为可选
//                    .outWidth(Integer.parseInt(paths[2]))
//                    .outHeight(Integer.parseInt(paths[3]))
                        .bitrate(6400000)       //输出视频比特率
//                    .progressListener(listener)      //可输出视频处理进度
                        .process();
            } catch (Exception e) {
                System.out.println("compress error:"+e.toString());
            }
            if(type== PREV){
                return paths[1] + "/" + "compressPrev.mp4";
            }else{
                return paths[1] + "/" + "compressReal.mp4";
            }
        }else{
            return paths[0];
        }
    }

    @Override
    protected void onPostExecute(String compressedFilePath) {
        super.onPostExecute(compressedFilePath);
        File imageFile = new File(compressedFilePath);
        float length = imageFile.length() / 1024f; // Size in KB
        String value;
        if (length >= 1024)
            value = length / 1024f + " MB";
        else
            value = length + " KB";
        listener.compressorComplete(compressedFilePath,type);
    }
}
