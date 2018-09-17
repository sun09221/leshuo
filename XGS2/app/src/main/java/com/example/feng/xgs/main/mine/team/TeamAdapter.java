package com.example.feng.xgs.main.mine.team;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/9 0009.
 * 我的团队
 */

public class TeamAdapter extends BaseEntityAdapter{

    public TeamAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_team_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_team_item_sex, entity.getField(EntityKeys.SEX))
                .setText(R.id.tv_team_item_address, entity.getField(EntityKeys.ADDRESS))
                .setText(R.id.tv_team_item_count, entity.getField(EntityKeys.COUNT));
        ImageView ivHead = holder.getView(R.id.iv_team_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), ivHead, R.mipmap.icon_head_default);

    }
}
