package com.example.feng.xgs.config.callback;

import android.support.annotation.Nullable;

public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
    void executeCallback1(@Nullable T args,long time);
    void executeCallback2(double latitude,double longitude,int mapview,String street,String path);
}
