package com.grant.privately.file;

import android.app.Application;

import com.grant.privately.file.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.grant.privately.file.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.grant.privately.file.universalimageloader.core.ImageLoader;
import com.grant.privately.file.universalimageloader.core.ImageLoaderConfiguration;
import com.grant.privately.file.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by grant on 16-10-6.
 */

public class PrivateFileApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init(){
        // imageloader初始化
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache()).build();
        if (!ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().init(config);
    }
}
