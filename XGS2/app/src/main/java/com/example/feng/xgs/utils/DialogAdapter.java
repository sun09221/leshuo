package com.example.feng.xgs.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.xgs.R;

import java.util.List;

/**
 * Created by JY on 2018/4/11.
 */

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder>{
    private List<String> mData;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public DialogAdapter(List<String> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_day.setText(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v, position);
                // holder.tvPay.setBackgroundResource(mData.get(position).getColor());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // ImageView ivMatchVideoImg;
        TextView tv_day;

        public ViewHolder(View itemView) {

            super(itemView);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
        }
    }
}
