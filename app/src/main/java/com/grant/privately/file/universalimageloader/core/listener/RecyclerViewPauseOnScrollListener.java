package com.grant.privately.file.universalimageloader.core.listener;

import android.support.v7.widget.RecyclerView;

import com.grant.privately.file.universalimageloader.core.ImageLoader;

/**
 * Created by wuning on 2016-10-09 14:49.
 */

public class RecyclerViewPauseOnScrollListener extends RecyclerView.OnScrollListener{
    private ImageLoader imageLoader;

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final RecyclerView.OnScrollListener externalListener;

    public RecyclerViewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling){
        this(imageLoader,pauseOnScroll,pauseOnFling,null);
    }

    public RecyclerViewPauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling,
                                             RecyclerView.OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (pauseOnScroll) {
                    imageLoader.pause();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if (pauseOnFling) {
                    imageLoader.pause();
                }
                break;
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        if (externalListener != null) {
            externalListener.onScrolled(recyclerView,dx,dy);
        }
    }
}
