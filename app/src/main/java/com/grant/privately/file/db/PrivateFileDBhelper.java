package com.grant.privately.file.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by grant on 16-10-7.
 */

public class PrivateFileDBhelper extends SQLiteOpenHelper {

    private static final String DBNAME="db_private_file.db";
    private static final int DBVERSION = 1;   //数据库版本

    private AtomicInteger refenceCounter = new AtomicInteger(0);
    private static  PrivateFileDBhelper instance;
    private SQLiteDatabase db  = null;
    public static PrivateFileDBhelper instance(Context context){
        if(instance == null){
            synchronized (PrivateFileDBhelper.class) {
                if(instance == null){
                    instance = new PrivateFileDBhelper(context);
                }
            }
        }
        return instance;
    }


    private PrivateFileDBhelper (Context context){
        super(context, DBNAME, null, DBVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        //
        db.execSQL(TbFileEncodeInfo.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(TbFileEncodeInfo.SQL_DROP_TABLE);
        onCreate(db);
    }

    //复写下班的三个方法，加入计数器。close不是真正的管理,只有计数器减一后 等于0的时候，才关闭数据库
    @Override
    public  SQLiteDatabase getReadableDatabase() {
        return getWritableDatabase();
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if((db == null || !db.isOpen()) || refenceCounter.incrementAndGet() == 1){
            db = super.getWritableDatabase();
            refenceCounter.set(1);
        }
        return db ;
    }

    @Override
    public synchronized  void close() {
        if(db!=null && db.isOpen() && refenceCounter.decrementAndGet() == 0){
            super.close();
            db = null;
        }
    }
}
