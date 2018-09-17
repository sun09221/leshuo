package com.example.feng.xgs.main.mine.setting.help;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.main.nearby.search.New2Activity;

import java.util.List;

/**
 * Created by feng on 2018/6/27 0027.
 */

public class HelpAdapter extends BaseEntityAdapter{

    public HelpAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BaseEntity entity) {

        holder.setText(R.id.tv_help_item_position, entity.getField(EntityKeys.POSITION))
                .setText(R.id.tv_help_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_help_item_info, entity.getField(EntityKeys.INFO));

        RelativeLayout item_layout = holder.getView(R.id.item_mine_help_layout);
        ImageView ivArrow = holder.getView(R.id.iv_help_item_arrow);
        item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext, New2Activity.class);
                String a= entity.getField("info");
                String b= entity.getField("name");
                String c= entity.getField(EntityKeys.POSITION);
                Bundle bundle = new Bundle();
                bundle.putString("1",a);
                bundle.putString("type",b);
                bundle.putString("position",c);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
//                String type = entity.getField(EntityKeys.TYPE);
//                TextView tvInfo = holder.getView(R.id.tv_help_item_info);
//                ImageView imageView = holder.getView(R.id.iv_help_item_arrow);
//                if(ContentKeys.NORMAL.equals(type)){
//                    tvInfo.setVisibility(View.VISIBLE);
//                    imageView.setImageResource(R.mipmap.icon_mine_help_arrow_down);
//                    entity.setField(EntityKeys.TYPE, ContentKeys.SELECT);
//                }else if(ContentKeys.SELECT.equals(type)){
//                    tvInfo.setVisibility(View.GONE);
//                    imageView.setImageResource(R.mipmap.icon_mine_help_arrow_right);
//                    entity.setField(EntityKeys.TYPE, ContentKeys.NORMAL);
//                }

            }
        });
    }
}
