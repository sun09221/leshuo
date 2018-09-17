package com.example.feng.xgs.main.move.reward;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/2 0002.
 */

public class RewardAdapter extends BaseEntityAdapter{


    public RewardAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        holder.setText(R.id.tv_reward_item_dialog_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_reward_item_dialog_price, entity.getField(EntityKeys.PRICE) + "钻石");

        ImageView imageView = holder.getView(R.id.iv_reward_item_dialog);
        loadImg(entity.getField(EntityKeys.IMG_URL), imageView);

    }
}
