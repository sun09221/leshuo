package com.example.feng.xgs.utils.manager;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by feng on 2017/12/19.
 */

public class ToastUtils {
    protected static final String TAG = "AppToast";
    public static Toast toast;
    /**
     * 信息提示
     *
     * @param context
     * @param text
     */
    public static void showText(Context context, CharSequence text) {
        if(context==null)return;
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongText(Context context, CharSequence text) {
        if(context==null)return;
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showText(Context context, int resId) {
        try {
            if(context==null)return;
            if(toast != null)
                toast.cancel();
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void showLongText(Context context, int resId) {
        try {
            if(context==null)return;
            if(toast != null)
                toast.cancel();
            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
            toast.show();

        } catch (Exception e) {
        }
    }
}
