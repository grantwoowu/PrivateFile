package com.grant.privately.file.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.grant.privately.file.db.TbFileEncodeInfoHelper;
import com.grant.privately.file.db.entry.FileEncodeInfo;
import com.grant.privately.file.fragment.dummy.MediaPathEntry;
import com.grant.privately.file.utils.FileUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**文件解密任务
 * Created by grant on 16-10-7.
 */

public class FileDecodeTask extends AsyncTask <List<MediaPathEntry>,Integer,Boolean> {
    private OnTaskFinishedListener mOnTaskFinishedListener;
    private ProgressDialog progressDialog;
    private int totalEncode = 0;
    private int currentCount = 0;


    public FileDecodeTask (ProgressDialog progressDialog,OnTaskFinishedListener listener){
        mOnTaskFinishedListener = listener;
        this.progressDialog = progressDialog;
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
            //计算需要解密的数量
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
            //开始解密
            Iterator<MediaPathEntry> iterator = datas.iterator();
            while (iterator.hasNext()){
                MediaPathEntry entry = iterator.next();
                if (entry.isSelected()){
                    currentCount++;
                    publishProgress(currentCount);
                    boolean encodeOk = doDecode(entry,helper);
                    //解密处理成功
                    if (encodeOk){
                        iterator.remove();
                    }
                }
            }

            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setMessage("正在解密"+values[0]+"/"+totalEncode);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.setCancelable(true);
        progressDialog.dismiss();
        if (aBoolean){
            Toast.makeText(progressDialog.getContext(),"解密完成",Toast.LENGTH_LONG).show();
            if (mOnTaskFinishedListener!=null){
                mOnTaskFinishedListener.onFinished();
            }
        }else {
            Toast.makeText(progressDialog.getContext(),"请首先选择文件",Toast.LENGTH_LONG).show();
        }

    }

    private boolean doDecode(MediaPathEntry entry,TbFileEncodeInfoHelper helper){
        FileEncodeInfo info = helper.find(entry.getThumbPath());
        FileUtils.copyFile(new File(entry.getImgPath()),new File(info.getOldFilePath()));

        return false;
    }
}
