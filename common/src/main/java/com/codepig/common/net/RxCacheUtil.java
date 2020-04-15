package com.codepig.common.net;

import android.content.Context;
import android.util.Log;

import com.codepig.common.rxbus.EventInfo;
import com.codepig.common.rxbus.RxBus;
import com.codepig.common.rxbus.RxEvent;
import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.converter.GsonConverter;
import com.lei.lib.java.rxcache.entity.CacheResponse;
import com.lei.lib.java.rxcache.mode.CacheMode;
import com.lei.lib.java.rxcache.util.RxUtil;

import io.reactivex.functions.Consumer;

public class RxCacheUtil {
    private static RxCache rxCache;

    public static RxCache getRxCache() {
        return rxCache;
    }

    public static void setRxCache(Context context) {
        rxCache = new RxCache.Builder()
                .setDebug(true) //开启debug，开启后会打印缓存相关日志，默认为true
                .setConverter(new GsonConverter()) //设置转换方式，默认为Gson转换
                .setCacheMode(CacheMode.BOTH) //设置缓存模式，默认为二级缓存
                .setMemoryCacheSizeByMB(50) //设置内存缓存的大小，单位是MB
                .setDiskCacheSizeByMB(100) //设置磁盘缓存的大小，单位是MB
                .setDiskDirName("RxCache") //设置磁盘缓存的文件夹名称
                .build();
    }

    public static void writeData(String key,String data){
        rxCache.getInstance()
                .put(key, data, 3600 * 1000) //key:缓存的key data:具体的数据 time:缓存的有效时间
                .compose(RxUtil.<Boolean>io_main()) //线程调度
                .subscribe(aBoolean -> {
                    if (aBoolean) Log.d("Cache", "cache successful!");
                }, throwable -> throwable.printStackTrace());
    }

    public static void readData(String key,String pageName){
        rxCache.getInstance()
                .get(key,false,String.class) //key:缓存的key update:表示从缓存获取数据强行返回NULL
                .compose(RxUtil.io_main())
                .subscribe((Consumer<CacheResponse<String>>) stringCacheResponse -> {
                    if(stringCacheResponse.getData()!=null)
                            Log.d("Cache","data from cache : "+stringCacheResponse.getData());
                    RxBus.getInstance().send(RxEvent.CacheEvent.GET_CACHE_INFO, new EventInfo(pageName,stringCacheResponse.getData()));
                }, throwable -> throwable.printStackTrace());
    }

    public static void readData(String key,String pageName,int _index){
        rxCache.getInstance()
                .get(key,false,String.class) //key:缓存的key update:表示从缓存获取数据强行返回NULL
                .compose(RxUtil.io_main())
                .subscribe((Consumer<CacheResponse<String>>) stringCacheResponse -> {
                    if(stringCacheResponse.getData()!=null)
                        Log.d("Cache","data from cache : "+stringCacheResponse.getData());
                    RxBus.getInstance().send(RxEvent.CacheEvent.GET_CACHE_INFO_WITH_POSITION, new EventInfo(pageName,_index,stringCacheResponse.getData()));
                }, throwable -> throwable.printStackTrace());
    }
}
