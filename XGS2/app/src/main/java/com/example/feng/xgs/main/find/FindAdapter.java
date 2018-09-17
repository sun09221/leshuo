package com.example.feng.xgs.main.find;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.area.ContestAreaActivity;

import java.util.List;

/**
 * Created by feng on 2018/4/27 0027.
 * 发现adapter
 */

public class FindAdapter extends BaseEntityAdapter{

    public FindAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final BaseEntity entity) {
        holder.setText(R.id.tv_find_item, entity.getField(EntityKeys.NAME));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContestAreaActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_TITLE, entity.getField(EntityKeys.NAME));
                intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
                mContext.startActivity(intent);
            }
        });

    }
}
