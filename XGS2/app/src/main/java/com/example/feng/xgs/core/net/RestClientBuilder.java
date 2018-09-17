package com.example.feng.xgs.core.net;

import android.content.Context;

import com.example.feng.xgs.core.loader.LoaderStyle;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.IFailure;
import com.example.feng.xgs.core.net.callback.IRequest;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class RestClientBuilder {
    private String mUrl = null;
    //    private Map<String, Object> mParams;
    private static final Map<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IError mIError = null;
    private IFailure mIFailure = null;
    private RequestBody mBody = null;
    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;
    private Context mContext = null;
    private boolean isToastError = false;
    private boolean isToastAll = false;
    private boolean isRequestAll = false;

    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;

    RestClientBuilder(){
        PARAMS.clear();
    }


    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params){
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key,Object value){
        PARAMS.put(key,value);
        return this;
    }

    public final RestClientBuilder file(File file){
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String file){
        LogUtils.d("file: "+file);
        this.mFile = new File(file);
        return this;
    }

    public final RestClientBuilder dir(String downloadDir){
        this.mDownloadDir = downloadDir;
        return this;
    }

    public final RestClientBuilder extension(String extension){
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder name(String name){
        this.mName = name;
        return this;
    }

    public final RestClientBuilder raw(String raw){
        LogUtils.d("raw: "+raw);
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess = iSuccess;
        return this;
    }
    public final RestClientBuilder error(IError iError){
        this.mIError = iError;
        return this;
    }
    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure = iFailure;
        return this;
    }
    public final RestClientBuilder onRequest(IRequest iRequest){
        this.mIRequest = iRequest;
        return this;
    }



    public final RestClientBuilder loader(Context context, LoaderStyle style){
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }


    public final RestClientBuilder toast(Context context){
        this.mContext = context;
        this.isToastError = true;
        return this;
    }

    public final RestClientBuilder toastAll(Context context){
        this.mContext = context;
        this.isToastAll = true;
        return this;
    }

    //必须是已经传递过context才能调用
    public final RestClientBuilder toast(){
        this.isToastError = true;
        return this;
    }

    //必须是已经传递过context才能调用
    public final RestClientBuilder toastAll(){
        this.isToastAll = true;
        return this;
    }

    public final RestClientBuilder requestAll(){
        this.isRequestAll = true;
        return this;
    }


    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,mBody,mISuccess,mIError,mIFailure,mIRequest,
                mContext,mLoaderStyle,isToastError,isToastAll, isRequestAll, mDownloadDir,mExtension,mName,mFile);
    }
}
