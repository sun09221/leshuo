package com.example.feng.xgs.core.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;


import com.example.feng.xgs.config.IPerfectConfig;
import com.example.feng.xgs.core.net.callback.IRequest;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;


public class SaveFileTask extends AsyncTask<Object, Void, File>{

    private ISuccess mISuccess;
    private IRequest mIRequest;

    public SaveFileTask(ISuccess mISuccess, IRequest mIRequest) {
        this.mISuccess = mISuccess;
        this.mIRequest = mIRequest;
    }

    @Override
    protected File doInBackground(Object... params) {
        ResponseBody body = (ResponseBody) params[0];
        String downloadDir = (String) params[1];
        String extension = (String) params[2];
        String name = (String) params[3];
        InputStream is = body.byteStream();
        if(downloadDir == null || downloadDir.equals("")){
            downloadDir = "down_loads";
        }

        if(extension == null || extension.equals("")){
            extension = "";
        }

        if(name == null || name.equals("")){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            return FileUtil.writeToDisk(is,downloadDir,name);
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(mISuccess != null){
            mISuccess.onSuccess(file.getPath());
        }

        if(mIRequest != null){
            mIRequest.onRequestEnd();
        }

    }

    private void autoInstallApk(File file){
        if(FileUtil.getExtension(file.getPath()).equals("apk")){
            final Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            IPerfectConfig.getApplicationContext().startActivity(install);
        }

    }
}
