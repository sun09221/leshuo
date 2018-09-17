package com.example.feng.xgs.utils.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by feng on 2018/1/26.
 * 软引用handler
 */

public class HandlerUtils extends Handler{

    private WeakReference<Activity> weakReference;
    private IHandlerMessageListener mListener;

    public HandlerUtils(Activity activity, IHandlerMessageListener listener) {
        weakReference = new WeakReference<>(activity);
        this.mListener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        Activity activity = weakReference.get();
        if(activity == null || activity.isFinishing()){
            return;
        }

//        sendEmptyMessageDelayed(0, 1000);
        if(mListener != null){
            mListener.onHandlerMessage(msg);
        }
    }


}
