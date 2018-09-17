package com.example.feng.xgs.main.mine.product.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.ui.dialog.IDialogStringListener;

/**
 * Created by feng on 2018/6/19 0019.
 */

public class ProductPublishDialog implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private Activity mActivity;
    private AlertDialog mDialog;
    private IDialogStringListener mListener;
    private ProductPublishDialogDataConverter mDataConverter;
    private ProductPublishDialogAdapter mAdapter;
    private String info;

    private ProductPublishDialog(Activity activity){
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();
    }

    public static ProductPublishDialog create(Activity activity){
        return new ProductPublishDialog(activity);
    }

    public void beginShow(String response, IDialogStringListener listener){
        this.mListener = listener;
        mDialog.show();

        Window window = mDialog.getWindow();
        if(window != null){
            window.setContentView(R.layout.dialog_mine_product_publish);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_bottom_animation);
            window.setBackgroundDrawable(new ColorDrawable());

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.iv_dialog_product_publish_error).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_product_publish_sure).setOnClickListener(this);

            RecyclerView recyclerView = window.findViewById(R.id.rv_dialog_product_publish);

            initRecyclerView(recyclerView, response);
        }
    }


    private void initRecyclerView(RecyclerView recyclerView, String response){
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mDataConverter = new ProductPublishDialogDataConverter();
        mAdapter = new ProductPublishDialogAdapter(R.layout.item_mine_product_publish_dialog,
                mDataConverter.setJsonData(response).convert());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_dialog_product_publish_error:
                mDialog.cancel();
                break;
            case R.id.tv_dialog_product_publish_sure:
                mDialog.cancel();
                break;
            default:

                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String info = mAdapter.getData().get(position).getField(EntityKeys.INFO);
        if(mListener != null){
            mListener.onSure(info);
        }
        mDialog.cancel();
    }
}
