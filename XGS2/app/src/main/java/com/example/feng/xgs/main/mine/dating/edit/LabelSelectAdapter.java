package com.example.feng.xgs.main.mine.dating.edit;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/11 0011.
 */

public class LabelSelectAdapter extends BaseMultipleEntityAdapter{

    public LabelSelectAdapter(List<BaseEntity> data) {
        super(data);
        addItemType(ItemTypeKeys.DATING_LABEL_SELECT_HEAD, R.layout.head_mine_dating_label_select);
        addItemType(ItemTypeKeys.DATING_LABEL_SELECT_ITEM, R.layout.item_mine_dating_label_select);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {

        if(holder.getItemViewType() == ItemTypeKeys.DATING_LABEL_SELECT_HEAD){
            holder.setText(R.id.tv_label_select_head, entity.getField(EntityKeys.NAME));
            holder.addOnClickListener(R.id.tv_label_select_head);
        }else {
            holder.setText(R.id.tv_label_select_item, entity.getField(EntityKeys.NAME));
            TextView textView = holder.getView(R.id.tv_label_select_item);
            String type = entity.getField(EntityKeys.TYPE);
            if(ContentKeys.SELECT.equals(type)){
                textView.setSelected(true);
            }else {
                textView.setSelected(false);
            }
            holder.addOnClickListener(R.id.tv_label_select_item);
        }

    }
}
