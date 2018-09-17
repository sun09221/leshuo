package com.example.feng.xgs.main.find.shop.search;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.find.shop.list.ShopListActivity;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.List;

/**
 * Created by feng on 2018/6/11 0011.
 */

public class ShopSearchAdapter extends BaseMultipleEntityAdapter {

    public ShopSearchAdapter(List<BaseEntity> data) {
        super(data);
        addItemType(ItemTypeKeys.FIND_SHOP_SEARCH_HOT, R.layout.item_find_shop_search_hot);
        addItemType(ItemTypeKeys.FIND_SHOP_SEARCH_HISTORY, R.layout.item_find_shop_search_history);
    }

    @Override
    protected void convert(BaseViewHolder holder, final BaseEntity entity) {
        switch (holder.getItemViewType()) {
            case ItemTypeKeys.FIND_SHOP_SEARCH_HOT:
                final List<String> list = entity.getFieldObject(EntityKeys.ENTITY);
                ShopHotSearchLayout searchLayout = holder.getView(R.id.hot_layout_shop_search_item);

                searchLayout.setSelectedListener(new ShopHotSearchLayout.onSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        LogUtils.d("热搜内容： " + position);
                        String searchInfo = list.get(position);
                        startSearch(searchInfo);
                    }
                });

                searchLayout.setLabelData(list);
                break;

            case ItemTypeKeys.FIND_SHOP_SEARCH_HISTORY:

                holder.setText(R.id.tv_shop_search_history_item, entity.getField(EntityKeys.NAME));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String searchInfo = entity.getField(EntityKeys.NAME);
                        startSearch(searchInfo);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void startSearch(String searchInfo){
        Intent intent = new Intent(mContext, ShopListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.SHOP_LIST_SEARCH);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, searchInfo);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, searchInfo);
        mContext.startActivity(intent);
    }
}
