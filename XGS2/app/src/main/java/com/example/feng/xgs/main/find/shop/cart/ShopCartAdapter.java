package com.example.feng.xgs.main.find.shop.cart;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.List;

/**
 * Created by feng on 2017/12/21.
 */

public class ShopCartAdapter extends BaseEntityAdapter {

    public ShopCartAdapter(@LayoutRes int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final BaseEntity entity) {
        holder.setText(R.id.tv_cart_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_cart_item_price, entity.getField(EntityKeys.PRICE));
        final TextView tvCount = holder.getView(R.id.tv_cart_item_count);
        final String count = entity.getField(EntityKeys.COUNT);
        tvCount.setText(count);

        ImageView image = holder.getView(R.id.iv_cart_item);
        loadImg(entity.getField(EntityKeys.IMG_URL),image);

        /**
         * 设置选中状态
         * */
        ImageView ivSelect = holder.getView(R.id.iv_cart_item_radio);

        String type = entity.getField(EntityKeys.TYPE);
        if(type.equals(ContentKeys.SELECT)){
            ivSelect.setSelected(true);
        }else {
            ivSelect.setSelected(false);
        }
        holder.addOnClickListener(R.id.iv_cart_item_radio);


        /**
         * 改变数量
         * */
        holder.addOnClickListener(R.id.iv_cart_item_add);
        holder.addOnClickListener(R.id.iv_cart_item_cut);

        holder.addOnClickListener(R.id.tv_cart_item_delete);
    }

}
