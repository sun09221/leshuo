package com.example.feng.xgs.main.mine.product;

import android.support.annotation.Nullable;
import android.view.View;
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
 * Created by feng on 2018/5/30 0030.
 */

public class ProductAdapter extends BaseEntityAdapter{


    private String mType;
    public ProductAdapter(int layoutResId, @Nullable List<BaseEntity> data, String type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        TextView tvType = holder.getView(R.id.tv_mine_product_item_type);
//        String type = entity.getField(EntityKeys.TYPE);
        if(ContentKeys.MINE_PRODUCT_CHECK_FINISH.equals(mType)){
            tvType.setVisibility(View.GONE);
        }else {
            tvType.setVisibility(View.VISIBLE);
        }

        ImageView ivDetail = holder.getView(R.id.iv_mine_product_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), ivDetail);

        holder.setText(R.id.tv_mine_product_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_mine_product_item_price, entity.getField(EntityKeys.PRICE))
                .setText(R.id.tv_mine_product_item_info, entity.getField(EntityKeys.INFO));

    }
}
