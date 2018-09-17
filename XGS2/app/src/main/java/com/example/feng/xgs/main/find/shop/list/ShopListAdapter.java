package com.example.feng.xgs.main.find.shop.list;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopListAdapter extends BaseEntityAdapter{

    public ShopListAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        holder.setText(R.id.tv_small_item_name_center, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_small_item_price_center, entity.getField(EntityKeys.PRICE))
                .setText(R.id.tv_small_item_count_center, entity.getField(EntityKeys.COUNT));
        ImageView imageSmall = holder.getView(R.id.iv_small_item_center);
        loadImg(entity.getField("sowingmap"), imageSmall);
    }
}
