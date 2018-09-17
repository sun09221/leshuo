package com.example.feng.xgs.core.pay.alipay;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by feng on 2018/3/10 0010.
 * 调起支付宝支付不能在主线程中操作，所以使用异步任务
 */

public class PayAsyncTask extends AsyncTask<String, Void, Map<String, String>>{
    private final Activity ACTIVITY;
    private final IAlPayResultListener LISTENER;

    //订单支付成功
    private static final String AL_PAY_STATUS_SUCCESS = "9000";
    //订单处理中
    private static final String AL_PAY_STATUS_PAYING = "8000";
    //订单支付失败
    private static final String AL_PAY_STATUS_FAIL = "4000";
    //用户取消
    private static final String AL_PAY_STATUS_CANCEL = "6001";
    //支付网络错误
    private static final String AL_PAY_STATUS_CONNECT_ERROR = "6002";

    public PayAsyncTask(Activity activity, IAlPayResultListener listener) {
        this.ACTIVITY = activity;
        this.LISTENER = listener;
    }

    @Override
    protected Map<String, String> doInBackground(String... params) {
        for (int i = 0; i < params.length; i++) {
            Log.d("fengXDD", "doInBackground: "+params[i]);
        }

        final String alPaySign = params[0];
        Log.d("fengXDD", "doInBackground: paySign: "+alPaySign);
        final PayTask payTask = new PayTask(ACTIVITY);
        return payTask.payV2(alPaySign, true);
    }

    @Override
    protected void onPostExecute(Map<String, String> result) {
        super.onPostExecute(result);
//        NewXSLoader.stopLoading();

        final PayResult payResult = new PayResult(result);
        // 支付宝返回此次支付结构及加签，建议对支付宝签名信息拿签约是支付宝提供的公钥做验签
        final String resultInfo = payResult.getResult();
        final String resultStatus = payResult.getResultStatus();
        Log.d("fengXDD", "onPostExecute: info: "+resultInfo);
        Log.d("fengXDD", "onPostExecute: status: "+resultStatus);
//        Log.d("AL_PAY_RESULT info", resultInfo);
//        Log.d("AL_PAY_RESULT code", resultStatus);

        switch (resultStatus) {
            case AL_PAY_STATUS_SUCCESS:
                if (LISTENER != null) {
                    LISTENER.onPaySuccess();
                }
                break;
            case AL_PAY_STATUS_FAIL:
                if (LISTENER != null) {
                    LISTENER.onPayFail();
                }
                break;
            case AL_PAY_STATUS_PAYING:
                if (LISTENER != null) {
                    LISTENER.onPaying();
                }
                break;
            case AL_PAY_STATUS_CANCEL:
                if (LISTENER != null) {
                    LISTENER.onPayCancel();
                }
                break;
            case AL_PAY_STATUS_CONNECT_ERROR:
                if (LISTENER != null) {
                    LISTENER.onPayConnectError();
                }
                break;
            default:
                break;
        }
    }
}
