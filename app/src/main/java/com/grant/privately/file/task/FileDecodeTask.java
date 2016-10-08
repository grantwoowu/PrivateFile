package com.grant.privately.file.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.grant.privately.file.fragment.dummy.MediaPathEntry;

import java.util.List;

/**文件解密任务
 * Created by grant on 16-10-7.
 */

public class FileDecodeTask extends AsyncTask <List<MediaPathEntry>,Integer,Boolean> {
    private ProgressDialog progressDialog;
    private int totalEncode = 0;
    private int currentCount = 0;


    public FileDecodeTask (ProgressDialog progressDialog){
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

            //开始加密
            for (MediaPathEntry entry:datas){
                if (entry.isSelected()){

                    SystemClock.sleep(1234);
                    currentCount++;
                    publishProgress(currentCount);
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
        }else {
            Toast.makeText(progressDialog.getContext(),"请首先选择文件",Toast.LENGTH_LONG).show();
        }

    }
}
