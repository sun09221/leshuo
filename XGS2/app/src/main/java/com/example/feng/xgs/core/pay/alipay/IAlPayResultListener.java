package com.example.feng.xgs.core.pay.alipay;

/**
 * Created by
 */

public interface IAlPayResultListener {

    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();
}
