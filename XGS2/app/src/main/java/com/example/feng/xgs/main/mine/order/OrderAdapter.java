package com.example.feng.xgs.main.mine.order;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class OrderAdapter extends BaseEntityAdapter{

    public OrderAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        holder.setText(R.id.tv_order_item_time, entity.getField(EntityKeys.TIME))
                .setText(R.id.tv_order_item_price, entity.getField(EntityKeys.PRICE))
                .setText(R.id.tv_order_item_number, entity.getField(EntityKeys.NUMBER))
                .setText(R.id.tv_order_item_tag, entity.getField(EntityKeys.TYPE));

    }
}
