package com.example.feng.xgs.core.net.download;

import android.os.AsyncTask;


import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.loader.LoaderStyle;
import com.example.feng.xgs.core.net.RestCreator;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.IFailure;
import com.example.feng.xgs.core.net.callback.IRequest;
import com.example.feng.xgs.core.net.callback.ISuccess;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DownloadHandler {

    private String mUrl;
    private static final WeakHashMap<String, Object> mParams = RestCreator.getParams();
    private ISuccess mISuccess;
    private IError mIError;
    private IFailure mIFailure;
    private IRequest mIRequest;
    private LoaderStyle mLoaderStyle;
    private String mDownloadDir;
    private String mExtension;
    private String mName;

    public DownloadHandler(String mUrl, ISuccess iSuccess, IError iError,
                           IFailure iFailure, IRequest iRequest, LoaderStyle loaderStyle,
                           String mDownloadDir, String mExtension, String mName) {
        this.mUrl = mUrl;
        this.mISuccess = iSuccess;
        this.mIError = iError;
        this.mIFailure = iFailure;
        this.mIRequest = iRequest;
        this.mLoaderStyle = loaderStyle;
        this.mDownloadDir = mDownloadDir;
        this.mExtension = mExtension;
        this.mName = mName;
    }


    public void handlerDownload(){
        if(mIRequest != null){
            mIRequest.onRequestStart();
        }

        RestCreator.getRestService().download(mUrl,mParams)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        stopLoader();
                        if(response.isSuccessful()){
                            ResponseBody body = response.body();
//                            body.contentLength();
                            SaveFileTask saveFileTask = new SaveFileTask(mISuccess, mIRequest);
                            saveFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    body,mDownloadDir,mExtension,mName);
                            //这里一定要判断，否则文件下载不全
                            if(saveFileTask.isCancelled()){
                                if(mIRequest != null){
                                    mIRequest.onRequestEnd();

                                }
                            }
                        }else {
                            if(mIError != null){
                                mIError.onError(response.code(),response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if(mIError != null){
                            mIError.onError(400, "http请求失败");
                        }
                        stopLoader();

                    }
                });
    }

    private void stopLoader(){
        if(mLoaderStyle != null){
            NaturalLoader.stopLoading();
        }
    }

}
