package com.example.feng.xgs.ui.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.utils.DensityUtils;

/**
 * Created by feng on 2018/1/10.
 * 删除提示dailog
 */

public class DeleteHintDialog implements View.OnClickListener {

    private Context mContext;
    private AlertDialog mDialog;
    private String mTitle, mContent;
    private DeleteHintDialog(Context context){
        this.mContext = context;
        mDialog = new AlertDialog.Builder(context).create();
    }

    public static DeleteHintDialog create(Context context){
        return new DeleteHintDialog(context);
    }

    public void showDialog(IOnDialogDeleteListener listener){
        this.mListener = listener;
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_delete_hint);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.dialog_scale_animation);
            window.setBackgroundDrawableResource(R.drawable.white_solid_radius_shape);
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            int screenWidth = DensityUtils.getScreenWidth(mContext);
            params.width = screenWidth/7*5;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            TextView tvSure = (TextView) window.findViewById(R.id.tv_dialog_delete_sure);
            TextView tvCancel = (TextView) window.findViewById(R.id.tv_dialog_delete_cancel);

            if(!TextUtils.isEmpty(mTitle)){
                TextView tvTitle = window.findViewById(R.id.tv_dialog_delete_title);
                tvTitle.setText(mTitle);
            }

            if(!TextUtils.isEmpty(mTitle)){
                TextView tvContent = window.findViewById(R.id.tv_dialog_delete_content);
                tvContent.setText(mContent);
            }

            tvSure.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
        }
    }

    public DeleteHintDialog setTitle(String title){
        this.mTitle = title;
        return this;
    }

    public DeleteHintDialog setContent(String content){
        this.mContent = content;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_delete_cancel:
                mDialog.cancel();
                break;
            case R.id.tv_dialog_delete_sure:
                mDialog.cancel();
                if(mListener != null){
                    mListener.onSure();
                }
                break;
            default:break;
        }
    }

    private IOnDialogDeleteListener mListener = null;

    public interface IOnDialogDeleteListener {
        void onSure();
    }

}
