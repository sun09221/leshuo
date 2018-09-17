package com.example.feng.xgs.main.circle;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;

import java.util.List;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class RadioAdapter extends BaseEntityAdapter {


    public RadioAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseEntity baseEntity) {
        baseViewHolder.addOnClickListener(R.id.radio_anchor_relayout);
        baseViewHolder.setText(R.id.radio_item_all_date,baseEntity.getField("releasetime"));
        baseViewHolder.setText(R.id.radio_item_all_message,baseEntity.getField("title"));
        baseViewHolder.setText(R.id.adio_item_all__playtime,baseEntity.getField("lengthtime"));
        baseViewHolder.setText(R.id.adio_item_all__playnum,baseEntity.getField("time"));
        baseViewHolder.setText(R.id.adio_item_all__playmessage,baseEntity.getField("comNum"));

    }



}
