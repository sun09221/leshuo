package com.example.feng.xgs.main.move.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.ui.dialog.IDialogListener;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2018/5/7 0007.
 * 留言dialog
 */

public class RadioSendCommentDialog implements View.OnClickListener {

    private Activity mActivity;
    private AlertDialog mDialog;
    private EditText mEtContent;
    private String mDynamicId;
    private IDialogListener mListener;

    private RadioSendCommentDialog(Activity activity) {
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();
    }

    public static RadioSendCommentDialog create(Activity activity) {
        return new RadioSendCommentDialog(activity);
    }

    public void beginShow(String dynamicId, IDialogListener listener) {
        this.mListener = listener;
        this.mDynamicId = dynamicId;
        mDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showInputMethod();
            }
        },100);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_send_comment);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_bottom_animation);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
            mEtContent = window.findViewById(R.id.et_dialog_send_content);
            window.findViewById(R.id.tv_dialog_send_sure).setOnClickListener(this);
        }

    }
    private void showInputMethod() {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_send_sure:
              //  sendComment();
                break;
            default:
                break;

        }
    }

    private void sendComment(){
        String content = mEtContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showText(mActivity, "请输入评论内容");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.DYNAMIC_ID, mDynamicId);
        map.put(ParameterKeys.CONTENT, content);

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.COMMENT)
                .raw(raw)
                .loader(mActivity)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(mActivity, "评论成功");
                        if(mListener != null){
                            mListener.onSure();
                        }
                        mDialog.cancel();
                    }
                })
                .build().post();
    }
}
