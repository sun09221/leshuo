package com.example.feng.xgs.core.net.callback;

import android.content.Context;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.loader.LoaderStyle;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallBack implements Callback<String> {

    private ISuccess iSuccess = null;
    private IError iError = null;
    private IFailure iFailure = null;
    private IRequest iRequest = null;
    private Context mContext;
    private LoaderStyle mLoaderStyle;
    private boolean isToastError = false;
    private boolean isToastAll = false;
    private boolean isRequestAll;//是否不处理请求结果，将参数直接返回

    public RequestCallBack(Context context, ISuccess success, IError error,
                           IFailure failure, IRequest request,
                           LoaderStyle loaderStyle,
                           boolean isToastError, boolean isToastAll, boolean isRequestAll) {
        this.mContext = context;
        this.iSuccess = success;
        this.iError = error;
        this.iFailure = failure;
        this.iRequest = request;
        this.mLoaderStyle = loaderStyle;
        this.isToastError = isToastError;
        this.isToastAll = isToastAll;
        this.isRequestAll = isRequestAll;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        LogUtils.d("onResponse: "+response);
        LogUtils.d("onResponse: "+response.body());
        stopLoading();
        if(response.isSuccessful() && call.isExecuted()){

            if(isRequestAll){
                if(iSuccess != null){
                    iSuccess.onSuccess(response.body());
                }
                return;
            }

            JSONObject object = JSON.parseObject(response.body());
            if(object == null) return;

            int code = object.getInteger("result");

            if(code == 1){
                if(iSuccess != null){
                    LogUtils.d("onResponse: 成功");
                    iSuccess.onSuccess(response.body());
                }
            }else {
                String message = object.getString("message");
                if(isToastError){
                    LogUtils.d("isToastError: " + isToastError);
                    ToastUtils.showText(mContext, message);
                }
                if(iError != null){
                    iError.onError(code, message);
                }
            }

        }else {
            if(iError != null){
                iError.onError(400, "HTTP请求错误信息: "+response.message());
            }
        }

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        LogUtils.d("onFailure: "+t);
        stopLoading();
        if(iFailure != null){
            iFailure.onFailure();
        }

        if(iRequest != null){
            iRequest.onRequestEnd();
        }

        if(iError != null){
            iError.onError(401, "HTTP请求错误信息: onFailure");
            LogUtils.d("onError: ");
        }

    }

    private void stopLoading(){
        if(mLoaderStyle != null){
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    NaturalLoader.stopLoading();
//                }
//            },1000);
        }
    }
}
