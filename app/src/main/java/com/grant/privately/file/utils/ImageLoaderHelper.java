package com.grant.privately.file.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.grant.privately.file.universalimageloader.core.DisplayImageOptions;
import com.grant.privately.file.universalimageloader.core.ImageLoader;
import com.grant.privately.file.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.grant.privately.file.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.grant.privately.file.universalimageloader.core.listener.ImageLoadingListener;
import com.grant.privately.file.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



public class ImageLoaderHelper {
	/** 网络加载路径前缀：http:// */
	public static String PRIFIX_HTTP = "http://";
	/** 本地加载路径前缀：file:// */
	public static String PRIFIX_FILE = "file://";
	/** drawables (non-9patch images) 加载路径前缀:drawable:// */
	public static String PRIFIX_DRAWABLE = "drawable://";
	/** from assets ,assets目录下的图片加载路径前缀：assets:// */
	public static String PRIFIX_ASSETS = "assets://";
	
	private static ImageLoader loader;
	private static DisplayImageOptions option;
	private static ImageLoadingListener animateFirstListener;
	/***
	 * 获取一个ImageLoader对象
	 * @return
	 * @throws Exception
	 */
	public static ImageLoader getInstance() {
		if (loader == null) {
			synchronized (ImageLoaderHelper.class) {
				if (loader == null) {
					loader = ImageLoader.getInstance();
					animateFirstListener = new AnimateFirstDisplayListener();
				}
			}
		}
		return loader;
	}
	
	/**
	 * 
	 * 获取imageload缓存的文件
	 * @author lhjtianji
	 * 
	 * */
	public static File getImageLoadCacheFile(String url){
		if(loader == null){
			loader = ImageLoader.getInstance() ;
		}
		File file = loader.getDiskCache().get(url) ;
		return file ;
	}
	
	/***
	 * 加载图片
	 * 
	 * @param uri
	 *            图片地址
	 * @param imageView
	 *            图片显示的View
	 * @param stubImage
	 *            加载中显示的图片
	 * @param imageForEmptyUri
	 *            uri为空时显示的图片
	 * @param imageOnFail
	 *            图片加载失败时显示的图片
	 * @param cacheInMemory
	 *            是否缓存在内存中
	 * @param cacheOnDisk
	 *            是否缓存在SD卡
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void displayImageView(String uri, ImageView imageView,
			int stubImage, int imageForEmptyUri, int imageOnFail,
			boolean cacheInMemory, boolean cacheOnDisk) throws Exception{
			displayImageView(uri, imageView, stubImage, imageForEmptyUri, imageOnFail, cacheInMemory, cacheOnDisk, 0);
	}
	/***
	 * 加载图片
	 * 
	 * @param uri
	 *            图片地址
	 * @param imageView
	 *            图片显示的View
	 * @param stubImage
	 *            加载中显示的图片
	 * @param imageForEmptyUri
	 *            uri为空时显示的图片
	 * @param imageOnFail
	 *            图片加载失败时显示的图片
	 * @param cacheInMemory
	 *            是否缓存在内存中
	 * @param cacheOnDisk
	 *            是否缓存在SD卡
	 * @param filletRadius
	 *            圆角,如不需要请设为0
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void displayImageView(String uri, ImageView imageView,
			int stubImage, int imageForEmptyUri, int imageOnFail,
			boolean cacheInMemory, boolean cacheOnDisk, int filletRadius)
			throws Exception {
		if(option == null){
			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
			option = builder.showStubImage(stubImage)
					.showImageForEmptyUri(imageForEmptyUri)
					.showImageOnFail(imageOnFail)
					.cacheInMemory(cacheInMemory)
					.cacheOnDisk(cacheOnDisk)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(filletRadius)).build();
		}
		
		getInstance()
				.displayImage(uri, imageView, option, animateFirstListener);
	}
	
	public static DisplayImageOptions getDisplayImageOptions(){
		return option;
	}

	/**
	 * 展示ImageView的快捷方法
	 * 
	 * @date 2015-5-29下午8:47:58
	 * @param uri
	 *            图片URL
	 * @param imageView
	 *            要显示图片的ImageView
	 */
	public static void displayImageView(String uri, ImageView imageView) {
		try {
			displayImageView(uri, imageView, android.R.drawable.ic_menu_gallery,
					android.R.drawable.ic_menu_gallery, android.R.drawable.ic_menu_gallery, true, true,10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** sdk选择图片页面 */
	public static void displayImageViewNoRadius(String uri, ImageView imageView) {
		try {
			displayImageView(uri, imageView, android.R.drawable.ic_menu_gallery,
					android.R.drawable.ic_menu_gallery, android.R.drawable.ic_menu_gallery, true, true, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void displayImageView(String uri, ImageView imageView, boolean cacheInMemory, boolean cacheOnDisk) {
		try {
			displayImageView(uri, imageView, android.R.drawable.ic_menu_gallery,
					android.R.drawable.ic_menu_gallery, android.R.drawable.ic_menu_gallery, cacheInMemory, cacheOnDisk,10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void displayUserImageView(String uri, ImageView imageView) {
		try {
			displayImageView(uri, imageView, android.R.drawable.ic_menu_gallery,
					android.R.drawable.ic_menu_gallery, android.R.drawable.ic_menu_gallery, true, true,10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public static void clearCache() {

		try {
			if(loader!=null){
				loader.clearDiscCache();
				loader.clearDiskCache();
				loader.clearMemoryCache();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
