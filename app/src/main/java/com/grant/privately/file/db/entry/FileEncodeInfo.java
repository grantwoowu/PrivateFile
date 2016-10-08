package com.grant.privately.file.db.entry;

import com.grant.privately.file.fragment.dummy.MediaType;

/**
 * Created by grant on 16-10-7.
 */

public class FileEncodeInfo {

    private int id;
    private String oldFilePath;
    private String oldFileName;
    private String newFilePath;
    private long fileCrc32;
    private byte[] fileHead;
    private String fileShot;
    private MediaType mediaType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public void setOldFilePath(String oldFilePath) {
        this.oldFilePath = oldFilePath;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public long getFileCrc32() {
        return fileCrc32;
    }

    public void setFileCrc32(long fileCrc32) {
        this.fileCrc32 = fileCrc32;
    }

    public byte[] getFileHead() {
        return fileHead;
    }

    public void setFileHead(byte[] fileHead) {
        this.fileHead = fileHead;
    }

    public String getFileShot() {
        return fileShot;
    }

    public void setFileShot(String fileShot) {
        this.fileShot = fileShot;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }
}
