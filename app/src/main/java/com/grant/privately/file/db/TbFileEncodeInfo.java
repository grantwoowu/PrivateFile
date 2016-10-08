package com.grant.privately.file.db;

/**
 * Created by grant on 16-10-7.
 */

public class TbFileEncodeInfo {
    public final static String TB_NAME = "tb_file_encode_info";

    public final static String COL_NAME_ID = "_id";
    public final static String COL_NAME_FILE_OLD_PATH = "old_file_path";//文件路径
    public final static String COL_NAME_FILE_OLD_NAME = "old_file_name";//文件名
    public final static String COL_NAME_FILE_NEW_PATH = "new_file_path";//文件路径
    public final static String COL_NAME_FILE_CRC32 = "file_crc32";//文件crc32
    public final static String COL_NAME_FILE_HEAD = "file_head";//文件内容前数个字节
    public final static String COL_NAME_MEDIA_TYPE = "media_type";//文件类型
    public final static String COL_NAME_MEDIA_SHOT = "shot_img";//文件第一帧

    public final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TB_NAME+" ("
            +COL_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT"
            +","+ COL_NAME_FILE_OLD_PATH +" TEXT"
            +","+ COL_NAME_FILE_OLD_NAME +" TEXT"
            +","+ COL_NAME_FILE_NEW_PATH +" TEXT"
            +","+COL_NAME_FILE_CRC32+" LONG"
            +","+COL_NAME_FILE_HEAD+" TEXT"
            +","+COL_NAME_MEDIA_TYPE+" INTEGER"
            +","+COL_NAME_MEDIA_SHOT+" TEXT"
            +")";
    public final static String SQL_DROP_TABLE = "DROP TABLE "+ TB_NAME;

}
