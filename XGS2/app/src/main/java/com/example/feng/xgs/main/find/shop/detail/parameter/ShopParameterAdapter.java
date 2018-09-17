package com.example.feng.xgs.main.find.shop.detail.parameter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopParameterAdapter extends BaseEntityAdapter{

    public ShopParameterAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_shop_detail_parameter_item_left, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_shop_detail_parameter_item_right, entity.getField(EntityKeys.INFO));
    }
}
