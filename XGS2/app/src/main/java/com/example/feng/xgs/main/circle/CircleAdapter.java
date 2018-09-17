package com.example.feng.xgs.main.circle;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;

import java.util.List;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class CircleAdapter extends BaseEntityAdapter {


    public CircleAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseEntity baseEntity) {
        baseViewHolder.addOnClickListener(R.id.tv_add_circle);
        baseViewHolder.addOnClickListener(R.id.iv_circle_logo);
        baseViewHolder.addOnClickListener(R.id.item_circle_layout);
        baseViewHolder.setText(R.id.tv_circle_title,baseEntity.getField("name"));
        baseViewHolder.setText(R.id.tv_watch_num,baseEntity.getField("peoplenum"));
//        baseViewHolder.setText(R.id.tv_comment_num,baseEntity.getField(""));
        ImageView imageViewHead = baseViewHolder.getView(R.id.iv_circle_logo);
        loadImg(baseEntity.getField("images"),imageViewHead);
        if(baseEntity.getField("state").equals("1")){
            baseViewHolder.getView(R.id.tv_add_circle).setBackgroundResource(R.drawable.blue_line_shape);
            baseViewHolder.setText(R.id.tv_add_circle,"已关注");
            ((TextView)baseViewHolder.getView(R.id.tv_add_circle)).setTextColor(mContext.getResources().getColor(R.color.blue_original));
        }else  if(baseEntity.getField("state").equals("0")){
            baseViewHolder.getView(R.id.tv_add_circle).setBackgroundResource(R.drawable.blue_add_shape);
            baseViewHolder.setText(R.id.tv_add_circle,"关注");
            ((TextView)baseViewHolder.getView(R.id.tv_add_circle)).setTextColor(mContext.getResources().getColor(R.color.white));
        }
    }



}
