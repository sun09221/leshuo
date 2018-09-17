package com.example.feng.xgs.main.mine.product.publish;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/6/19 0019.
 */

public class ProductPublishDialogAdapter extends BaseEntityAdapter {

    public ProductPublishDialogAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_product_publish_dialog_item, entity.getField(EntityKeys.INFO));
    }
}
