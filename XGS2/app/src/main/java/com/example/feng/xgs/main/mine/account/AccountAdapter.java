package com.example.feng.xgs.main.mine.account;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class AccountAdapter extends BaseEntityAdapter{

    public AccountAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        holder.setText(R.id.tv_account_item_number, entity.getField(EntityKeys.NUMBER));
        String type = entity.getField(EntityKeys.TYPE);
        if(ContentKeys.ALI_PAY.equals(type)){
            holder.setText(R.id.tv_account_item_name, "支付宝");
            holder.setImageResource(R.id.iv_account_item, R.mipmap.icon_ali_withdraw);
        }else if(ContentKeys.WE_CHAT.equals(type)){
            holder.setText(R.id.tv_account_item_name, "微信");
            holder.setImageResource(R.id.iv_account_item, R.mipmap.icon_mine_account_we_chat);
        }
    }
}
