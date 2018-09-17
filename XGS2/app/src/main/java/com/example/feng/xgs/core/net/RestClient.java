package com.example.feng.xgs.core.net;

import android.content.Context;
import android.util.Log;


import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.loader.LoaderStyle;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.IFailure;
import com.example.feng.xgs.core.net.callback.IRequest;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.net.callback.RequestCallBack;
import com.example.feng.xgs.core.net.download.DownloadHandler;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class RestClient {

    private String mUrl;
    private ISuccess iSuccess;
    private IError iError;
    private IFailure iFailure;
    private IRequest iRequest;
    private Context mContext;
    private LoaderStyle mLoaderStyle;
    private boolean isToastError;
    private boolean isToastAll;
    private boolean isRequestAll;
    private String mDownloadDir;
    private String mExtension;
    private String mName;
    private WeakHashMap<String, Object> mParams = RestCreator.getParams();
    private RequestBody mRequestBody;
    private File mFile;
    private List<File> mFiles;
    private Map<String,RequestBody> mBodyParams;
    private List<MultipartBody.Part> mParts;

    public RestClient(String mUrl, Map<String, Object> params, RequestBody requestBody,
                      ISuccess iSuccess, IError iError, IFailure iFailure, IRequest iRequest,
                      Context mContext, LoaderStyle loaderStyle,
                      boolean isToastError, boolean isToastAll, boolean isRequestAll,
                      String mDownloadDir, String mExtension, String mName,
                      File file) {
        this.mParams.putAll(params);
        this.mUrl = mUrl;
        this.iSuccess = iSuccess;
        this.iError = iError;
        this.iFailure = iFailure;
        this.iRequest = iRequest;
        this.mContext = mContext;
        this.mLoaderStyle = loaderStyle;
        this.isToastError = isToastError;
        this.isToastAll = isToastAll;
        this.isRequestAll = isRequestAll;
        this.mDownloadDir = mDownloadDir;
        this.mExtension = mExtension;
        this.mName = mName;
        this.mRequestBody = requestBody;
        this.mFile = file;
    }

    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    private void request(String httpMethod){
        RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if(iRequest != null){
            iRequest.onRequestStart();
        }

        if(mLoaderStyle != null){
            NaturalLoader.showLoading(mContext);
        }

        switch (httpMethod){
            case HttpMethod.GET:
                call = service.get(mUrl,mParams);
                break;
            case HttpMethod.POST:
                call = service.post(mUrl,mParams);
                break;
            case HttpMethod.POST_RAW:
                Log.d("RestClient", "requestBody: "+mRequestBody+",url: "+mUrl);
                call = service.postRaw(mUrl,mRequestBody);
                break;
            case HttpMethod.PUT:
                call = service.put(mUrl,mParams);
                break;
            case HttpMethod.PUT_RAW:
                call = service.putRaw(mUrl,mRequestBody);
                break;
            case HttpMethod.DELETE:
                call = service.delete(mUrl,mParams);
                break;
            case HttpMethod.UPLOAD_FORM:
//                final RequestBody formBody =
//                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()),mFile);
                final RequestBody formBody =
                        RequestBody.create(MediaType.parse("image/png"),mFile);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file",mFile.getName(),formBody);
                call = service.upload(mUrl,body);
                break;
            default:
                break;
        }

        if(call != null){
            call.enqueue(getRequestCallBack());
        }
    }

    private Callback<String> getRequestCallBack(){
        return new RequestCallBack(mContext,iSuccess,iError,iFailure,iRequest,mLoaderStyle,isToastError, isToastAll, isRequestAll);
    }

    public void get(){
        request(HttpMethod.GET);
    }


    public final void post(){
        if(mRequestBody == null){
            request(HttpMethod.POST);
        }else {
            if(!mParams.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put(){
        if(mRequestBody == null){
            request(HttpMethod.PUT);
        }else {
            if(!mParams.isEmpty()){
                throw new RuntimeException("params must be null");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete(){
        request(HttpMethod.DELETE);
    }

    public final void uploadForm(){
        request(HttpMethod.UPLOAD_FORM);
    }

    public final void download(){
        if(mLoaderStyle != null){
            NaturalLoader.showLoading(mContext);
        }
        new DownloadHandler(mUrl,iSuccess,iError, iFailure, iRequest, mLoaderStyle, mDownloadDir,
                mExtension,mName).handlerDownload();
    }


}
