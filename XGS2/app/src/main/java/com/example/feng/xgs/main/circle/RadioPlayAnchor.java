package com.example.feng.xgs.main.circle;

import android.support.annotation.Nullable;
import android.widget.ImageView;

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
public class RadioPlayAnchor extends BaseEntityAdapter {


    public RadioPlayAnchor(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseEntity baseEntity) {
        baseViewHolder.setText(R.id.radioplay_item_date,baseEntity.getField("releasetime"));
        baseViewHolder.setText(R.id.radioplay_item_name,baseEntity.getField("nickname"));
        ImageView imageViewdetail = baseViewHolder.getView(R.id.radioplay_item_anchor);
        loadImg(baseEntity.getField("labelimg"),imageViewdetail);


    }



}
