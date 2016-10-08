package com.grant.privately.file.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.grant.privately.file.db.TbFileEncodeInfoHelper;
import com.grant.privately.file.db.entry.FileEncodeInfo;
import com.grant.privately.file.fragment.dummy.MediaPathEntry;
import com.grant.privately.file.utils.CRC32Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**文件加密任务
 * Created by grant on 16-10-7.
 */

public class FileEncodeTask extends AsyncTask<List<MediaPathEntry>,Integer,Boolean> {
    private ProgressDialog progressDialog;
    private int totalEncode = 0;
    private int currentCount = 0;
    private byte[]buff;
    private byte[]head;


    public FileEncodeTask (ProgressDialog progressDialog){
        this.progressDialog = progressDialog;
        buff = new byte[64*1024];
        head = new byte[10];
    }
    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("正在准备...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(List<MediaPathEntry>... params) {

        List<MediaPathEntry> datas = params[0];
        if (datas!=null && !datas.isEmpty()){
            //计算需要加密的数量
            for (MediaPathEntry entry:datas){
                if (entry.isSelected()){
                    totalEncode++;
                }
            }
            if (totalEncode<=0){
                return false;
            }
            publishProgress(currentCount);

            TbFileEncodeInfoHelper helper = new TbFileEncodeInfoHelper(progressDialog.getContext());
            //开始加密
            for (MediaPathEntry entry:datas){
                if (entry.isSelected()){
                    currentCount++;
                    publishProgress(currentCount);
                    doEncode(progressDialog.getContext(),helper,entry);
                }
            }

            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setMessage("正在加密"+values[0]+"/"+totalEncode);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.setCancelable(true);
        progressDialog.dismiss();
        if (aBoolean){
            Toast.makeText(progressDialog.getContext(),"加密完成",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(progressDialog.getContext(),"请首先选择文件",Toast.LENGTH_LONG).show();
        }

    }

    private void doEncode(Context context,TbFileEncodeInfoHelper helper, MediaPathEntry entry){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File oldFile = new File(entry.getImgPath());
            if (oldFile.exists()&&oldFile.length()>0){
                FileEncodeInfo info = new FileEncodeInfo();
                info.setOldFilePath(oldFile.getAbsolutePath());
                info.setOldFileName(oldFile.getName());
                info.setFileShot(entry.getThumbPath());
                info.setMediaType(entry.getMediaType());
                info.setFileCrc32(CRC32Util.getCRC32(entry.getImgPath()));

                fis = new FileInputStream(oldFile);
                //读取前10个字节
                fis.read(head);
                info.setFileHead(head);
                //新文件保存
                File obbDir = context.getObbDir();
                if (!obbDir.exists()){
                    obbDir.mkdirs();
                }
                File newFile = new File(obbDir,"/"+entry.getMediaType().getIndex()+"/"+ UUID.randomUUID().toString());
                if (!newFile.getParentFile().exists()){
                    newFile.getParentFile().mkdirs();
                }
                newFile.createNewFile();
                info.setNewFilePath(newFile.getAbsolutePath());
                fos = new FileOutputStream(newFile);
                int readSize = 0;
                //跳过前10个字节
                while ((readSize = fis.read(buff))!=-1){
                    fos.write(buff,0,readSize);
                }
                fos.flush();

                if(helper.insert(info)){
                    //删除旧文件
                    oldFile.renameTo(new File(oldFile.getParentFile(),System.currentTimeMillis()+""));
                    oldFile.delete();

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
