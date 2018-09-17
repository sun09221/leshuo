package com.example.feng.xgs.main.mine.reward;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class MineRewardAdapter extends BaseEntityAdapter {

    public MineRewardAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_mine_reward_item_count, entity.getField(EntityKeys.COUNT))
                .setText(R.id.tv_mine_reward_item_price, "￥" + entity.getField(EntityKeys.PRICE));

        holder.addOnClickListener(R.id.tv_mine_reward_item_price);


        TextView tvCountGive = holder.getView(R.id.tv_mine_reward_item_count_give);
        String countGive = entity.getField(EntityKeys.COUNT_GIVE);

        if(TextUtils.isEmpty(countGive) || countGive.equals("0")){
            tvCountGive.setVisibility(View.GONE);
        }else {
            tvCountGive.setVisibility(View.VISIBLE);
            tvCountGive.setText("赠送" + countGive + "钻石");
        }
    }
}
