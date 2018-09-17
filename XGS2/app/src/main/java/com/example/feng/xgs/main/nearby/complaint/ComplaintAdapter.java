package com.example.feng.xgs.main.nearby.complaint;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.List;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class ComplaintAdapter extends BaseEntityAdapter{

    public ComplaintAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
//        holder.setText(R.id.tv_nearby_complaint_item, entity.getField(EntityKeys.NAME));

        TextView textView = holder.getView(R.id.tv_nearby_complaint_item);
        textView.setText(entity.getField(EntityKeys.NAME));
        String type = entity.getField(EntityKeys.TYPE);
        if(ContentKeys.SELECT.equals(type)){
            textView.setSelected(true);
        }else {
            textView.setSelected(false);
        }
    }
}
