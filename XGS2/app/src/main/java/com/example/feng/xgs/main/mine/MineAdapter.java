package com.example.feng.xgs.main.mine;

import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.SpannableUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.List;

/**
 * Created by feng on 2018/3/19 0019.
 * 个人中心adapter
 * 注: 使用recyclerView有些小问题，暂时放弃该方法
 */

public class MineAdapter extends BaseMultipleEntityAdapter {
    public MineAdapter(List<BaseEntity> data) {
        super(data);
        String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
        if (type.equals("1")){
            addItemType(ItemTypeKeys.MINE_HEAD, R.layout.head_mine_vip);
        }else{
            addItemType(ItemTypeKeys.MINE_HEAD, R.layout.head_mine);
        }

        addItemType(ItemTypeKeys.MINE_ITEM, R.layout.item_mine);
        addItemType(ItemTypeKeys.DIVIDER_10_DP, R.layout.item_divider_10_dp);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        switch (holder.getItemViewType()){

            case ItemTypeKeys.MINE_HEAD://头布局
                TextView tvLike = holder.getView(R.id.tv_mine_like);
                TextView tvAttention = holder.getView(R.id.tv_mine_attention);
                TextView tvComment = holder.getView(R.id.tv_mine_comment);

                String like = mContext.getString(R.string.mine_like) + "\n" + entity.getField(EntityKeys.COUNT_LIKE);
                String attention = mContext.getString(R.string.mine_attention) + "\n" + entity.getField(EntityKeys.COUNT_ATTENTION);
                String comment = mContext.getString(R.string.mine_comment) + "\n" + entity.getField(EntityKeys.COUNT_COMMENT);

                setTextCount(tvLike, like);
                setTextCount(tvAttention, attention);
                setTextCount(tvComment, comment);
                ImageView ivHead = holder.getView(R.id.iv_mine_head);
                loadImg(entity.getField(EntityKeys.IMG_URL), ivHead, R.mipmap.icon_head_default);
                holder.setText(R.id.tv_mine_name, entity.getField(EntityKeys.NAME));

//                holder.addOnClickListener(R.id.iv_mine_head);
//                holder.addOnClickListener(R.id.tv_mine_name);
                holder.addOnClickListener(R.id.iv_mine_head_setting);//设置
                holder.addOnClickListener(R.id.tv_mine_info);//查看个人信息
                holder.addOnClickListener(R.id.tv_mine_name);//查看个人信息
                holder.addOnClickListener(R.id.iv_mine_head);//查看个人信息
                holder.addOnClickListener(R.id.tv_mine_attention);//查看个人信息
                holder.addOnClickListener(R.id.tv_mine_comment);//查看个人信息
                break;

            case ItemTypeKeys.MINE_ITEM://item布局
                holder.setText(R.id.tv_mine_item, entity.getField(EntityKeys.NAME));
                holder.setImageResource(R.id.iv_mine_item, (Integer) entity.getFieldObject(EntityKeys.IMG_URL));

                break;

            case ItemTypeKeys.DIVIDER_10_DP:

                break;
                default:
                    break;
        }
    }

    private void setTextCount(TextView textView, String info){
        SpannableStringBuilder builderComment = SpannableUtil.setTextAndColor(info, "\n", DensityUtils.dp2px(mContext, 16));
        textView.setText(builderComment);
    }
}
