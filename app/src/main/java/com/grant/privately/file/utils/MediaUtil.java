package com.grant.privately.file.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.grant.privately.file.fragment.dummy.MediaPathEntry;
import com.grant.privately.file.fragment.dummy.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by grant on 16-10-6.
 */

public class MediaUtil {

    private int filterImageSize =  0 /*1024* 20*/;
    public List<MediaPathEntry> scanVideo(Context context) {
        List<MediaPathEntry> mediaPathEntryList= null;
        Cursor cursor = null;
        try {
            /**
             * 存储文件路径，和真实图片id
             */
            Map<String, String> tempMap = new HashMap<String, String>();
            int entryIndex = 0;
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, null,
                    null, null, MediaStore.Video.Thumbnails.VIDEO_ID + " DESC");
            int count = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                int index = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
                int id = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Thumbnails.VIDEO_ID);// 获取真实路径的ID;
                String imageThumbPath = cursor.getString(index);// 缩略图路径
                String image_id = cursor.getString(id);
                tempMap.put(image_id, imageThumbPath);
                cursor.moveToNext();
            }

            if (cursor!=null){
                cursor.close();
            }
            cursor = resolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                    null, null, MediaStore.Video.Media._ID + " DESC");
            int imgCount = cursor.getCount();
            cursor.moveToFirst();
            mediaPathEntryList = new ArrayList<>();
            for (int i = 0; i < imgCount; i++) {
                int id_Index = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int data_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String id = cursor.getString(id_Index);
                String imgPath = cursor.getString(data_index);

                if (imgPath != null) {
                    File imageFile = new File(imgPath);
                    if (imageFile.exists()
                            && imageFile.isFile()
                            && (imageFile.length() > filterImageSize)) {
                        String imageThumbPath = tempMap.get(id);
                        if (imageThumbPath == null) {
                            imageThumbPath = imgPath;// 如果没有缩略图，则用原图代替
                        }

                        MediaPathEntry entry = new MediaPathEntry();
                        entry.setImgPath(imgPath);
                        entry.setThumbPath(imageThumbPath);
                        entry.setMediaType(MediaType.VIDEO);
                        mediaPathEntryList.add(entry);
                        Log.i("Grant","scanVideo>>"+entry.getThumbPath());

                    }
                }

                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }

        return mediaPathEntryList;
    }

    /**
     * 图片 扫描
     *
     * @date 2015-6-27上午11:30:35
     */
    public List<MediaPathEntry> scanImages(Context context) {

        List<MediaPathEntry> mediaPathEntryList= null;
        Cursor cursor = null;
        try {
            /**
             * 存储文件路径，和真实图片id
             */
            Map<String, String> tempMap = new HashMap<String, String>();
            int entryIndex = 0;
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    null, null, null, MediaStore.Images.Thumbnails.IMAGE_ID +" DESC");
            int count = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                int index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
                int id = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);// 获取真实路径的ID;
                String imageThumbPath = cursor.getString(index);// 缩略图路径
                String image_id = cursor.getString(id);
                tempMap.put(image_id, imageThumbPath);
                cursor.moveToNext();
            }

            if (cursor!=null){
                cursor.close();
            }

            cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    null, null, MediaStore.Images.Media._ID +" DESC");
            int imgCount = cursor.getCount();
            cursor.moveToFirst();
            mediaPathEntryList = new ArrayList<>();

            for (int i = 0; i < imgCount; i++) {
                int id_Index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int data_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String id = cursor.getString(id_Index);
                String imgPath = cursor.getString(data_index);

                if (imgPath != null) {
                    File imageFile = new File(imgPath);
                    if (imageFile.exists()
                            && imageFile.isFile()
                            && (imageFile.length() > filterImageSize)) {
                        String imageThumbPath = tempMap.get(id);
                        if (imageThumbPath == null) {
                            imageThumbPath = imgPath;// 如果没有缩略图，则用原图代替
                        }
                        MediaPathEntry entry = new MediaPathEntry();
                        entry.setImgPath(imgPath);
                        entry.setThumbPath(imageThumbPath);
                        entry.setMediaType(MediaType.IMAGE);
                        mediaPathEntryList.add(entry);

                        Log.i("Grant","scanImages>>"+entry.getThumbPath());
                    }
                }

                cursor.moveToNext();
            }
            cursor.close();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }

        return mediaPathEntryList;
    }
}
