package com.grant.privately.file.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import com.grant.privately.file.db.entry.FileEncodeInfo;
import com.grant.privately.file.fragment.dummy.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grant on 16-10-7.
 */

public class TbFileEncodeInfoHelper {

    private PrivateFileDBhelper mDbOpenHelper;

    public TbFileEncodeInfoHelper(Context context){
        mDbOpenHelper =  PrivateFileDBhelper.instance(context);
    }

    public boolean insert(FileEncodeInfo info){
        if (info == null)
            return false;
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TbFileEncodeInfo.COL_NAME_FILE_OLD_PATH,info.getOldFilePath());
            contentValues.put(TbFileEncodeInfo.COL_NAME_FILE_OLD_NAME,info.getOldFileName());
            contentValues.put(TbFileEncodeInfo.COL_NAME_FILE_NEW_PATH,info.getNewFilePath());
            contentValues.put(TbFileEncodeInfo.COL_NAME_FILE_CRC32,info.getFileCrc32());
            contentValues.put(TbFileEncodeInfo.COL_NAME_FILE_HEAD, Base64.encodeToString(info.getFileHead(),Base64.DEFAULT));
            contentValues.put(TbFileEncodeInfo.COL_NAME_MEDIA_TYPE,info.getMediaType().getIndex());
            contentValues.put(TbFileEncodeInfo.COL_NAME_MEDIA_SHOT,info.getFileShot());

            db.insert(TbFileEncodeInfo.TB_NAME,null,contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    private boolean delete(int id){
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        try {
            String whereClause = TbFileEncodeInfo.COL_NAME_ID+" = ?";
            String []whereArgs = {""+id};
            db.delete(TbFileEncodeInfo.TB_NAME,whereClause,whereArgs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }

        return false;
    }

    public List<FileEncodeInfo> findAll(MediaType mediaType){
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            String selection = TbFileEncodeInfo.COL_NAME_MEDIA_TYPE+" = ?";
            String []selectionArgs = {""+mediaType.getIndex()};
            String orderBy = TbFileEncodeInfo.COL_NAME_ID+" desc ";

            cursor = db.query(TbFileEncodeInfo.TB_NAME,null,selection,selectionArgs,null,null,orderBy,null);
            if (cursor!=null && cursor.getCount()>0){
                List<FileEncodeInfo>datas = new ArrayList<>();
                cursor.moveToPrevious();
                while (cursor.moveToNext()){
                    FileEncodeInfo info = fillData(cursor);
                    datas.add(info);
                }

                return datas;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            close();
        }

        return null;
    }


    private FileEncodeInfo fillData(Cursor cursor) {
        FileEncodeInfo info = new FileEncodeInfo();
        info.setId(cursor.getInt(cursor.getColumnIndex(TbFileEncodeInfo.COL_NAME_ID)));
        info.setOldFilePath(cursor.getString(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_FILE_OLD_PATH)));
        info.setOldFileName(cursor.getString(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_FILE_OLD_NAME)));
        info.setNewFilePath(cursor.getString(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_FILE_NEW_PATH)));
        info.setFileCrc32(cursor.getLong(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_FILE_CRC32)));
        info.setMediaType(MediaType.values()[cursor.getInt(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_MEDIA_TYPE))]);
        info.setFileHead(Base64.decode(cursor.getString(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_FILE_HEAD)),Base64.DEFAULT));
        info.setFileShot(cursor.getString(cursor.getColumnIndexOrThrow(TbFileEncodeInfo.COL_NAME_MEDIA_SHOT)));

        return info;
    }




    private void close(){
        if (mDbOpenHelper!=null){
            mDbOpenHelper.close();
        }
    }
}
