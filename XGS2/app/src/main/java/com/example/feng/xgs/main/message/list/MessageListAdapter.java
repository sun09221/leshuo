package com.example.feng.xgs.main.message.list;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.SpannableUtil;

import java.util.List;

/**
 * Created by feng on 2018/6/14 0014.
 */

public class MessageListAdapter extends BaseEntityAdapter{

    public MessageListAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_j_push_message_item_time, entity.getField(EntityKeys.TIME));

        TextView tvCount = holder.getView(R.id.tv_j_push_message_item_count);
        int count = entity.getFieldObject(EntityKeys.COUNT);
        if(count <= 0){
            tvCount.setVisibility(View.GONE);
        }else {
            tvCount.setText(String.valueOf(count));
        }

        ImageView ivHead = holder.getView(R.id.iv_j_push_message_item);
        String imgUrl = entity.getField(EntityKeys.IMG_URL);
        if(imgUrl != null){
            loadImg(imgUrl, ivHead, R.mipmap.icon_head_default);
        }

        String divider = "\n";
        String content = entity.getField(EntityKeys.NAME) + divider + entity.getField(EntityKeys.INFO);
        SpannableStringBuilder builder =
                SpannableUtil.setTextAndColor(content, divider, DensityUtils.dp2px(mContext, 12));
        holder.setText(R.id.tv_j_push_message_item_name, builder);

    }
}
