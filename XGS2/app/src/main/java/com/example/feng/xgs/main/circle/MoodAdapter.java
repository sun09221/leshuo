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
public class MoodAdapter extends BaseEntityAdapter {

    public MoodAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseEntity baseEntity) {
        baseViewHolder.addOnClickListener(R.id.tv_natural_item_share);
        ImageView imageViewHead = baseViewHolder.getView(R.id.item_mood_picture);
        loadImg(baseEntity.getField("imgpath"), imageViewHead);


    }
}
