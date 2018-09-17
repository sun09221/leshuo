package com.example.feng.xgs.main.move.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ContentKeys;

/**
 * Created by feng on 2018/5/17 0017.
 */

public class NaturalPublishDialog implements View.OnClickListener {

    private Activity mActivity;
    private AlertDialog mDialog;
    private String circle_id = "";

    private NaturalPublishDialog(Activity activity) {
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();
    }

    public static NaturalPublishDialog create(Activity activity) {
        return new NaturalPublishDialog(activity);
    }

    public void beginShow(String circle_id) {
        mDialog.show();
        this.circle_id = circle_id;
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_bottom_animation);
            window.setBackgroundDrawable(new ColorDrawable());

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            TextView tvVideo = window.findViewById(R.id.tv_dialog_camera_photo);
            tvVideo.setText(mActivity.getString(R.string.dynamic_video));
            TextView tvImage = window.findViewById(R.id.tv_dialog_camera_take);
            tvImage.setText(mActivity.getString(R.string.dynamic_image));
            tvVideo.setOnClickListener(this);
            tvImage.setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_camera_cancel).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //发布图文动态
            case R.id.tv_dialog_camera_take:
                intent.setClass(mActivity, NaturalPublishActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_TYPE, ContentKeys.NATURAL_PUBLISH_IMAGE);
                intent.putExtra(ContentKeys.ACTIVITY_TITLE, "图文动态");
                intent.putExtra("circleid", this.circle_id);
                mActivity.startActivity(intent);
                mDialog.cancel();
                break;
            //发布视频动态
            case R.id.tv_dialog_camera_photo:
                intent.setClass(mActivity, NaturalPublishVideoActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_TYPE, ContentKeys.NATURAL_PUBLISH_VIDEO);
                intent.putExtra(ContentKeys.ACTIVITY_TITLE, "视频动态");
                intent.putExtra("circleid",  this.circle_id);
                mActivity.startActivity(intent);
                mDialog.cancel();
                break;
            case R.id.tv_dialog_camera_cancel:
                mDialog.cancel();
                break;
            default:
                break;
        }
    }
}
