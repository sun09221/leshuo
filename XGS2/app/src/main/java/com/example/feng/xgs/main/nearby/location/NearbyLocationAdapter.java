package com.example.feng.xgs.main.nearby.location;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/6/9 0009.
 */

public class NearbyLocationAdapter extends BaseMultipleEntityAdapter{

    public NearbyLocationAdapter(List<BaseEntity> data) {
        super(data);
        addItemType(ItemTypeKeys.LIST_HEAD, R.layout.item_nearby_location_head);
        addItemType(ItemTypeKeys.LIST_ITEM, R.layout.item_nearby_location_content);

    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        switch (holder.getItemViewType()){
            case ItemTypeKeys.LIST_HEAD:
                holder.setText(R.id.tv_nearby_location_item_head, entity.getField(EntityKeys.CITY));
                break;
            case ItemTypeKeys.LIST_ITEM:
                holder.setText(R.id.tv_nearby_location_item_content, entity.getField(EntityKeys.CITY));
                holder.addOnClickListener(R.id.tv_nearby_location_item_content);
                break;
            default:

                break;
        }
    }
}
