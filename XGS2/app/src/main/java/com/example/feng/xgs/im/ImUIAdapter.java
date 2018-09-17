package com.example.feng.xgs.im;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.core.GlideApp;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.List;

/**
 * Created by feng on 2018/1/25.
 */

public class ImUIAdapter extends BaseMultiItemQuickAdapter<BaseEntity, BaseViewHolder> {

    private String mImgUrlLeft;
    public ImUIAdapter(List<BaseEntity> data, String imgUrlLeft) {
        super(data);
        this.mImgUrlLeft = imgUrlLeft;
        init();
        addItemType(ItemTypeKeys.ROBOT_HEAD, R.layout.item_robot_head);
        addItemType(ItemTypeKeys.ROBOT_LEFT, R.layout.item_robot_left);
        addItemType(ItemTypeKeys.ROBOT_RIGHT, R.layout.item_robot_right);
    }

    private void init() {
        openLoadAnimation();
        isFirstOnly(false);
    }


    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        switch (holder.getItemViewType()) {
            case ItemTypeKeys.ROBOT_LEFT:
                holder.setText(R.id.tv_robot_item, entity.getField(EntityKeys.INFO));
                ImageView ivHeadLeft = holder.getView(R.id.iv_robot_item);
                loadImg(mImgUrlLeft, ivHeadLeft, R.mipmap.icon_head_default);
                break;
            case ItemTypeKeys.ROBOT_RIGHT:
                holder.setText(R.id.tv_robot_item, entity.getField(EntityKeys.INFO));
                ImageView ivHeadRight = holder.getView(R.id.iv_robot_item);
                String imgUrlRight = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_HEAD);
                loadImg(imgUrlRight, ivHeadRight, R.mipmap.icon_head_default);
                break;
            case ItemTypeKeys.ROBOT_HEAD:
                holder.setText(R.id.iv_im_time_item, entity.getField(EntityKeys.TIME));
                break;
            default:
                break;
        }

    }


    public void loadImg(String imgUrl, ImageView view) {
        GlideApp.with(mContext)
                .load(imgUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public void loadImg(String imgUrl, ImageView view, int drawable) {
        GlideApp.with(mContext)
                .load(imgUrl)
                .placeholder(drawable)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }
}
