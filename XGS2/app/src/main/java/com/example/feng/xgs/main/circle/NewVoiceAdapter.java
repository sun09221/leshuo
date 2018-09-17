package com.example.feng.xgs.main.circle;

import android.support.annotation.Nullable;
import android.widget.ImageView;

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
public class NewVoiceAdapter extends BaseEntityAdapter {


    public NewVoiceAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BaseEntity baseEntity) {
        baseViewHolder.addOnClickListener(R.id.iv_natural_item_head);
        baseViewHolder.addOnClickListener(R.id.tv_natural_item_share);
       // baseViewHolder.addOnClickListener(R.id.tv_natural_item_likes);
        baseViewHolder.addOnClickListener(R.id.tv_natural_item_leave_messages);
        baseViewHolder.addOnClickListener(R.id.iv_natural_item_head_layout);
        baseViewHolder.addOnClickListener(R.id.tv_natural_item_likes_image);
        baseViewHolder.addOnClickListener(R.id.playmusic);
        baseViewHolder.setText(R.id.tv_natural_item_name,baseEntity.getField("nickname"));
        baseViewHolder.setText(R.id.tv_natural_item_sex,baseEntity.getField("sex"));
        baseViewHolder.setText(R.id.tv_natural_item_constellation,baseEntity.getField("starsign"));
        if (baseEntity.getField("sex").equals("")){
            baseViewHolder.setGone(R.id.tv_natural_item_sex,false);
        }else{
            baseViewHolder.setGone(R.id.tv_natural_item_sex,true);
        }

        baseViewHolder.setText(R.id.tv_natural_item_time,baseEntity.getField("releasetime"));
        ImageView imageViewdetail = baseViewHolder.getView(R.id.iv_natural_item_head_detail);
        loadImg(baseEntity.getField("labelimg"),imageViewdetail);
        baseViewHolder.setText(R.id.tv_natural_item_name_detail,baseEntity.getField("title"));
        baseViewHolder.setText(R.id.tv_natural_item_type,baseEntity.getField("labelname"));
        baseViewHolder.setText(R.id.voice_time,baseEntity.getField("lengthtime"));
        baseViewHolder.setText(R.id.tv_natural_item_times,"被听"+baseEntity.getField("time")+"次");
        baseViewHolder.setText(R.id.tv_natural_item_leave_messages,"("+baseEntity.getField("comNum")+")");
        try {
            if (baseEntity.getField("starsign").equals("")){
                baseViewHolder.setGone(R.id.tv_natural_item_constellation,false);
            }else{
                baseViewHolder.setGone(R.id.tv_natural_item_constellation,true);
            }
        } catch (Exception e) {
            baseViewHolder.setGone(R.id.tv_natural_item_constellation,false);
        }
        if (baseEntity.getField("zan").equals("1")){
           baseViewHolder.setText(R.id.tv_natural_item_likes,"("+baseEntity.getField("praiseNum")+")");
           baseViewHolder.getView(R.id.tv_natural_item_likes_image).setSelected(true);
       } else if (baseEntity.getField("zan").equals("2")){
            int a = Integer.parseInt(baseEntity.getField("praiseNum"));
            String num =String.valueOf(a+1);
            baseViewHolder.setText(R.id.tv_natural_item_likes,"("+num+")");
            baseViewHolder.getView(R.id.tv_natural_item_likes_image).setSelected(true);
        }
       else{
           baseViewHolder.setText(R.id.tv_natural_item_likes,"("+baseEntity.getField("praiseNum")+")");
            baseViewHolder.getView(R.id.tv_natural_item_likes_image).setSelected(false);
       }
        //        baseViewHolder.setText(R.id.tv_comment_num,baseEntity.getField(""));
        ImageView imageViewHead = baseViewHolder.getView(R.id.iv_natural_item_head);
        loadImg(baseEntity.getField("header"),imageViewHead);
    }



}
