package com.example.feng.xgs.main.mine.info;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.ui.dialog.IDialogStringListener;
import com.example.feng.xgs.utils.manager.LogUtils;

/**
 * Created by feng on 2018/6/27 0027.
 */

public class SexDialog implements View.OnClickListener {

    private AlertDialog mDialog;
    private IDialogStringListener mListener;

    private SexDialog(Activity activity) {
        mDialog = new AlertDialog.Builder(activity).create();
    }

    public static SexDialog create(Activity activity) {
        return new SexDialog(activity);
    }

    public void beginShow(IDialogStringListener listener) {
        this.mListener = listener;
        mDialog.show();

        Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_sex_select);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_bottom_animation);
            window.setBackgroundDrawable(new ColorDrawable());

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;

            window.setAttributes(params);

            window.findViewById(R.id.tv_dialog_sex_men).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_sex_women).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_sex_cancel).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_sex_men:
                if(mListener != null){
                    mListener.onSure(ContentKeys.SEX_MAN);
                    LogUtils.d("男");
                }
                mDialog.cancel();
                break;
            case R.id.tv_dialog_sex_women:
                LogUtils.d("女");
                if(mListener != null){
                    mListener.onSure(ContentKeys.SEX_WOMEN);
                }
                mDialog.cancel();
                break;

            case R.id.tv_dialog_sex_cancel:
                mDialog.cancel();
                break;
            default:
                break;
        }
    }
}
