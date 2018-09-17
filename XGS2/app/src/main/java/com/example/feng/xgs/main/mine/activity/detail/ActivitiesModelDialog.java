package com.example.feng.xgs.main.mine.activity.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.ui.dialog.IDialogStringListener;

/**
 * Created by feng on 2018/5/7 0007.
 * 选择活动类型
 */

public class ActivitiesModelDialog implements View.OnClickListener {

    private IDialogStringListener mListener;
    private Activity mActivity;
    private AlertDialog mDialog;
    private TextView mTvEnroll;
    private TextView mTvNotEnroll;

    private ActivitiesModelDialog(Activity activity){
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();

    }

    public static ActivitiesModelDialog create(Activity activity){
        return new ActivitiesModelDialog(activity);
    }

    public void beginShow(IDialogStringListener listener){
        mDialog.show();
        this.mListener = listener;

        Window window = mDialog.getWindow();
        if(window != null){
            window.setContentView(R.layout.dialog_find_activities_model);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.dialog_scale_animation);
            window.setBackgroundDrawable(new ColorDrawable());

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            mTvEnroll = window.findViewById(R.id.tv_dialog_activities_enroll);
            mTvNotEnroll = window.findViewById(R.id.tv_dialog_activities_not_enroll);
            mTvEnroll.setSelected(true);

            mTvEnroll.setOnClickListener(this);
            mTvNotEnroll.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_activities_enroll:
                mTvEnroll.setSelected(true);
                mTvNotEnroll.setSelected(false);
                if(mListener != null){
                    mListener.onSure(ContentKeys.FIND_ACTIVITY_ENROLL);
                }
                mDialog.cancel();
                break;
            case R.id.tv_dialog_activities_not_enroll:

                mTvEnroll.setSelected(false);
                mTvNotEnroll.setSelected(true);
                if(mListener != null){
                    mListener.onSure(ContentKeys.FIND_ACTIVITY_NOT_ENROLL);
                }
                mDialog.cancel();
                break;

            default:
                break;
        }
    }
}
