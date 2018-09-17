package com.example.feng.xgs.main.find.shop;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.core.banner.BannerCreator;
import com.example.feng.xgs.main.find.shop.list.ShopListActivity;

import java.util.List;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopAdapter extends BaseMultipleEntityAdapter implements OnItemClickListener {

    public ShopAdapter(List<BaseEntity> data) {
        super(data);
        setSpanSizeLookup(this);
//        addItemType(ItemTypeKeys.FIND_SHOP_IMAGE_SMALL, R.layout.item_find_shop_image_small);
        addItemType(ItemTypeKeys.FIND_SHOP_IMAGE_BIG, R.layout.item_find_shop_image_big);
        addItemType(ItemTypeKeys.BANNER, R.layout.item_banner);
        addItemType(ItemTypeKeys.FIND_SHOP_TITLE, R.layout.item_find_shop_title);
        addItemType(ItemTypeKeys.FIND_SHOP_MORE, R.layout.item_find_shop_more);
        addItemType(ItemTypeKeys.FIND_SHOP_HOT_TAB, R.layout.item_find_shop_hot_tab);
    }

    @Override
    protected void convert(BaseViewHolder holder, final BaseEntity entity) {
        switch (holder.getItemViewType()) {
//            //图文
//            case ItemTypeKeys.FIND_SHOP_IMAGE_SMALL:
//                holder.setText(R.id.tv_small_item_name_center, entity.getField(EntityKeys.NAME))
//                        .setText(R.id.tv_small_item_price_center, entity.getField(EntityKeys.PRICE))
//                        .setText(R.id.tv_small_item_count_center, entity.getField(EntityKeys.COUNT));
//                ImageView imageSmall = holder.getView(R.id.iv_small_item_center);
//                loadImg(entity.getField(EntityKeys.IMG_URL), imageSmall);
//
//                break;

            case ItemTypeKeys.FIND_SHOP_HOT_TAB:
                holder.setText(R.id.tv_shop_hot_tab_item, entity.getField(EntityKeys.NAME));
                ImageView ivHotTab = holder.getView(R.id.iv_shop_hot_tab_item);
                loadImg(entity.getField(EntityKeys.IMG_URL), ivHotTab);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String info = entity.getField(EntityKeys.ID_ONLY);
                        String title = entity.getField(EntityKeys.NAME);
                        Intent intent = new Intent(mContext, ShopListActivity.class);
                        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.SHOP_LIST_TYPE);
                        intent.putExtra(ContentKeys.ACTIVITY_TITLE, title);
                        intent.putExtra(ContentKeys.ACTIVITY_INFO, info);
                        mContext.startActivity(intent);
                    }
                });
                break;

            //图文
            case ItemTypeKeys.FIND_SHOP_IMAGE_BIG:
                holder.setText(R.id.tv_shop_big_item_name, entity.getField(EntityKeys.NAME))
                        .setText(R.id.tv_shop_big_item_price, entity.getField(EntityKeys.PRICE))
                        .setText(R.id.tv_shop_big_item_count, entity.getField(EntityKeys.COUNT));
                ImageView imageBig = holder.getView(R.id.iv_big_item_right);
                loadImg(entity.getField(EntityKeys.IMG_URL), imageBig);

                break;
            case ItemTypeKeys.BANNER:
                ConvenientBanner<String> banner = holder.getView(R.id.banner_item);
                List<String> imgList = entity.getFieldObject(EntityKeys.IMG_URL_S);
                BannerCreator.setDefault(banner, imgList, this);
                break;
            //小标题
            case ItemTypeKeys.FIND_SHOP_TITLE:
                holder.setText(R.id.tv_shop_title_item, entity.getField(EntityKeys.NAME));
                break;
            //查看更多
            case ItemTypeKeys.FIND_SHOP_MORE:
                holder.getView(R.id.tv_shop_more_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShopListActivity.class);
                        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.SHOP_LIST_MORE);
                        intent.putExtra(ContentKeys.ACTIVITY_TITLE, "更多商品");
                        mContext.startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}
